package co.jp.wever.graphql.infrastructure.repository.article;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.domainmodel.article.Article;
import co.jp.wever.graphql.domain.domainmodel.tag.TagId;
import co.jp.wever.graphql.domain.repository.article.ArticleMutationRepository;
import co.jp.wever.graphql.infrastructure.connector.AlgoliaClient;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.EdgeLabel;
import co.jp.wever.graphql.infrastructure.constant.edge.property.IncludeEdgeProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.ArticleBlockVertexProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.ArticleVertexProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.DateProperty;
import co.jp.wever.graphql.infrastructure.converter.entity.article.ArticleSearchEntityConverter;
import co.jp.wever.graphql.infrastructure.util.EdgeIdCreator;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.inV;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.outV;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;
import static org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality.single;

@Component
public class ArticleMutationRepositoryImpl implements ArticleMutationRepository {

    private final NeptuneClient neptuneClient;
    private final AlgoliaClient algoliaClient;

    public ArticleMutationRepositoryImpl(
        NeptuneClient neptuneClient, AlgoliaClient algoliaClient) {
        this.neptuneClient = neptuneClient;
        this.algoliaClient = algoliaClient;
    }

    @Override
    public void publish(String articleId, Article article, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        updateArticleVertex(g, articleId, article, now);

        clearTag(g, articleId);
        addTags(g, articleId, article, now);

        clearSkills(g, articleId);
        addSkills(g, articleId, article, now);

        clearBlocks(g, articleId, now);
        addBlocks(g, articleId, article, now);

        clearStatus(g, articleId, userId);
        addStatus(g, articleId, EdgeLabel.PUBLISH.getString(), userId, now);

        algoliaClient.getArticleIndex()
                     .saveObjectAsync(ArticleSearchEntityConverter.toArticleSearchEntity(articleId, article, now));
    }

    @Override
    public String publishByEntry(Article article, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();
        long now2 = System.nanoTime();

        String articleId = createArticleVertex(g, article, now);

        addTags(g, articleId, article, now);
        addSkills(g, articleId, article, now);
        addBlocks(g, articleId, article, now);
        addStatus(g, articleId, EdgeLabel.PUBLISH.getString(), userId, now);

        //        algoliaClient.getArticleIndex()
        //                     .saveObjectAsync(ArticleSearchEntityConverter.toArticleSearchEntity(articleId, article, now));

        long now3 = System.nanoTime();
        System.out.println((now3 - now2) / 1000000);
        return articleId;
    }

    @Override
    public void draft(String articleId, Article article, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        updateArticleVertex(g, articleId, article, now);

        clearTag(g, articleId);
        addTags(g, articleId, article, now);

        clearSkills(g, articleId);
        addSkills(g, articleId, article, now);

        clearBlocks(g, articleId, now);
        addBlocks(g, articleId, article, now);

        clearStatus(g, articleId, userId);
        addStatus(g, articleId,EdgeLabel.DRAFT.getString(), userId, now);

        algoliaClient.getArticleIndex().deleteObjectAsync(articleId);
    }

    @Override
    public String draftByEntry(Article article, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        String articleId = createArticleVertex(g, article, now);

        addTags(g, articleId, article, now);
        addSkills(g, articleId, article, now);
        addBlocks(g, articleId, article, now);
        addStatus(g, articleId, EdgeLabel.DRAFT.getString(), userId, now);

        algoliaClient.getArticleIndex().deleteObjectAsync(articleId);
        return articleId;
    }

    @Override
    public void delete(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        clearStatus(g, articleId, userId);
        addStatus(g, articleId, EdgeLabel.DELETE.getString(), userId, now);

        algoliaClient.getArticleIndex().deleteObjectAsync(articleId);
    }

    @Override
    public void like(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        addAction(g, articleId, EdgeLabel.LIKE.getString(), userId, now);
    }

    @Override
    public void clearLike(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        clearAction(g, articleId, EdgeLabel.LIKE.getString(), userId);
    }

    @Override
    public void learn(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        addAction(g, articleId, EdgeLabel.LEARN.getString(), userId, now);
    }

    @Override
    public void clearLearn(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        clearAction(g, articleId, EdgeLabel.LEARN.getString(), userId);
    }

    private void updateArticleVertex(GraphTraversalSource g, String articleId, Article article, long now) {
        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .property(single, ArticleVertexProperty.TITLE.getString(), article.getBase().getTitle().getValue())
         .property(single, ArticleVertexProperty.IMAGE_URL.getString(), article.getBase().getImageUrl().getValue())
         .property(single, DateProperty.UPDATE_TIME.getString(), now)
         .next();
    }

    private String createArticleVertex(GraphTraversalSource g, Article article, long now) {
        return g.addV(VertexLabel.ARTICLE.getString())
                .property(ArticleVertexProperty.TITLE.getString(), article.getBase().getTitle().getValue())
                .property(ArticleVertexProperty.IMAGE_URL.getString(), article.getBase().getImageUrl().getValue())
                .property(DateProperty.CREATE_TIME.getString(), now)
                .property(DateProperty.UPDATE_TIME.getString(), now)
                .next()
                .id()
                .toString();
    }

    private void addTags(GraphTraversalSource g, String articleId, Article article, long now) {
        List<String> tagIds =
            article.getBase().getTagIds().getTagIds().stream().map(TagId::getValue).collect(Collectors.toList());

        if (!tagIds.isEmpty()) {
            g.V(tagIds)
             .hasLabel(VertexLabel.TAG.getString())
             .addE(EdgeLabel.RELATED_TO.getString())
             .property(DateProperty.CREATE_TIME.getString(), now)
             .from(g.V(articleId).hasLabel(VertexLabel.ARTICLE.getString()))
             .iterate();
        }
    }

    private void clearTag(GraphTraversalSource g, String articleId) {
        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .outE(EdgeLabel.RELATED_TO.getString())
         .where(inV().hasLabel(VertexLabel.TAG.getString()))
         .drop()
         .iterate();
    }

    private void clearSkills(GraphTraversalSource g, String articleId) {
        g.V(articleId)
         .outE(EdgeLabel.TEACH.getString())
         .where(inV().hasLabel(VertexLabel.SKILL.getString()))
         .drop()
         .iterate();
    }

    private void addSkills(GraphTraversalSource g, String articleId, Article article, long now) {
        article.getSkills()
               .getSkills()
               .forEach(s -> g.V(articleId)
                              .hasLabel(VertexLabel.ARTICLE.getString())
                              .addE(EdgeLabel.TEACH.getString())
                              .property("level", s.getLevel().getValue())
                              .property(DateProperty.CREATE_TIME.getString(), now)
                              .to(g.V(s.getId().getValue()).hasLabel(VertexLabel.SKILL.getString()))
                              .next());
    }

    private void clearStatus(GraphTraversalSource g, String articleId, String authorId) {
        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .inE(EdgeLabel.PUBLISH.getString(), EdgeLabel.DRAFT.getString())
         .where(outV().hasLabel(VertexLabel.USER.getString()).hasId(authorId))
         .drop()
         .iterate();
    }

    private void addStatus(GraphTraversalSource g, String articleId, String status, String authorId, long now) {
        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .addE(status)
         .property(T.id, EdgeIdCreator.createId(authorId, articleId, status))
         .property(DateProperty.CREATE_TIME.getString(), now)
         .from(g.V(authorId).hasLabel(VertexLabel.USER.getString()))
         .next();
    }

    private void addAction(GraphTraversalSource g, String articleId, String action, String userId, long now) {
        g.E(EdgeIdCreator.createId(userId, articleId, action))
         .fold()
         .coalesce(unfold(),
                   g.V(articleId)
                    .hasLabel(VertexLabel.ARTICLE.getString())
                    .addE(action)
                    .property(T.id, EdgeIdCreator.createId(userId, articleId, action))
                    .property(DateProperty.CREATE_TIME.getString(), now)
                    .property(DateProperty.UPDATE_TIME.getString(), now)
                    .from(g.V(userId).hasLabel(VertexLabel.USER.getString())))
         .next();
    }

    private void clearAction(GraphTraversalSource g, String articleId, String action, String userId) {
        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .inE(action)
         .hasId(EdgeIdCreator.createId(userId, articleId, action))
         .drop()
         .iterate();
    }

    private void clearBlocks(GraphTraversalSource g, String articleId, long now) {
        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .out(EdgeLabel.INCLUDE.getString())
         .hasLabel(VertexLabel.ARTICLE_TEXT.getString())
         .addE(EdgeLabel.DELETE.getString())
         .property(DateProperty.CREATE_TIME.getString(), now)
         .iterate();

        g.V(articleId)
         .hasLabel(VertexLabel.ARTICLE.getString())
         .outE(EdgeLabel.INCLUDE.getString())
         .hasLabel(VertexLabel.ARTICLE_TEXT.getString())
         .drop()
         .iterate();
    }

    private void addBlocks(GraphTraversalSource g, String articleId, Article article, long now) {
        String textId = g.addV(VertexLabel.ARTICLE_TEXT.getString())
                         .property(DateProperty.CREATE_TIME.getString(), now)
                         .id()
                         .next()
                         .toString();

        g.V(textId)
         .hasLabel(VertexLabel.ARTICLE_TEXT.getString())
         .addE(EdgeLabel.INCLUDE.getString())
         .from(g.V(articleId).hasLabel(VertexLabel.ARTICLE.getString()))
         .next();

        AtomicInteger idx = new AtomicInteger();
        article.getBlocks().getBlocks().forEach(b -> {
            g.addV(VertexLabel.ARTICLE_BLOCK.getString())
             .property(ArticleBlockVertexProperty.TYPE.getString(), b.getType().getValue())
             .property(ArticleBlockVertexProperty.DATA.getString(), b.getData().getValue())
             .property(DateProperty.CREATE_TIME.getString(), now)
             .property(DateProperty.UPDATE_TIME.getString(), now)
             .addE(EdgeLabel.INCLUDE.getString())
             .property(IncludeEdgeProperty.NUMBER.getString(), idx.get())
             .property(DateProperty.CREATE_TIME.getString(), now)
             .from(g.V(textId).hasLabel(VertexLabel.ARTICLE_TEXT.getString()))
             .next();
            idx.getAndIncrement();
        });

    }
}
