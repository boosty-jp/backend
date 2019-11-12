package co.jp.wever.graphql.infrastructure.repository.section;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.repository.section.FindSectionRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.ArticleToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.ArticleToSectionProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.converter.entity.section.SectionEntityConverter;
import co.jp.wever.graphql.infrastructure.converter.entity.section.SectionNumberEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionEntity;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionNumberEntity;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.constant;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.outV;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.coalesce;

@Component
public class FindSectionRepositoryImpl implements FindSectionRepository {

    private final NeptuneClient neptuneClient;

    public FindSectionRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public SectionEntity findOne(String sectionId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        Map<String, Object> result = g.V(sectionId)
                                      .project("base", "author", "status", "like", "number")
                                      .by(__.valueMap().with(WithOptions.tokens))
                                      .by(__.in(UserToSectionEdge.CREATED.getString(),
                                                UserToSectionEdge.DELETED.getString())
                                            .hasLabel(VertexLabel.USER.getString())
                                            .id())
                                      .by(__.inE(UserToSectionEdge.CREATED.getString(),
                                                 UserToSectionEdge.DELETED.getString()).label())
                                      .by(__.in(UserToSectionEdge.LIKED.getString()).count())
                                      .by(__.inE(ArticleToSectionEdge.INCLUDE.getString())
                                            .values(ArticleToSectionProperty.NUMBER.getString()))
                                      .next();


        return SectionEntityConverter.toSectionEntity(result, true);
    }

    public List<SectionEntity> findAllDetailOnArticle(String articleId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> results = g.V(articleId)
                                             .out(ArticleToSectionEdge.INCLUDE.getString())
                                             .hasLabel(VertexLabel.SECTION.getString())
                                             .project("base", "author", "status", "like", "number", "liked")
                                             .by(__.valueMap().with(WithOptions.tokens))
                                             .by(__.in(UserToSectionEdge.CREATED.getString(),
                                                       UserToSectionEdge.DELETED.getString())
                                                   .hasLabel(VertexLabel.USER.getString())
                                                   .id())
                                             .by(__.inE(UserToSectionEdge.CREATED.getString(),
                                                        UserToSectionEdge.DELETED.getString()).label())
                                             .by(__.in(UserToSectionEdge.LIKED.getString()).count())
                                             .by(__.inE(ArticleToSectionEdge.INCLUDE.getString())
                                                   .values(ArticleToSectionProperty.NUMBER.getString()))
                                             .by(coalesce(__.inE(UserToSectionEdge.LIKED.getString())
                                                            .where(outV().hasId(userId)
                                                                         .hasLabel(VertexLabel.USER.getString()))
                                                            .limit(1)
                                                            .constant(true), constant(false)))
                                             .toList();

        return results.stream().map(r -> SectionEntityConverter.toSectionEntity(r, false)).collect(Collectors.toList());
    }

    public List<SectionNumberEntity> findAllNumberOnArticle(String articleId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> allResults = g.V(articleId)
                                                .out(ArticleToSectionEdge.INCLUDE.getString())
                                                .hasLabel(VertexLabel.SECTION.getString())
                                                .project("id", "number")
                                                .by(__.id())
                                                .by(__.inE(ArticleToSectionEdge.INCLUDE.getString())
                                                      .values(ArticleToSectionProperty.NUMBER.getString()))
                                                .toList();

        return allResults.stream()
                         .map(e -> SectionNumberEntityConverter.toSectionNumberEntity(e))
                         .collect(Collectors.toList());
    }

    public String findAuthorId(String sectionId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        return (String) g.V(sectionId)
                         .in(UserToSectionEdge.CREATED.getString())
                         .hasLabel(VertexLabel.USER.getString())
                         .id()
                         .next();
    }

    @Override
    public List<SectionEntity> findAllLiked(String userId) {
        return null;
    }

    @Override
    public List<SectionEntity> findAllBookmarked(String userId) {
        return null;
    }

    @Override
    public List<SectionEntity> findFamous() {
        return null;
    }

    @Override
    public List<SectionEntity> findRelated(String userId) {
        return null;
    }

}
