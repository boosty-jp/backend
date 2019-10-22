package co.jp.wever.graphql.infrastructure.repository.section;

import org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import java.util.List;

import co.jp.wever.graphql.domain.repository.section.CreateSectionRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.ArticleToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.ArticleToSectionProperty;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToSectionProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.SectionVertexProperty;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionEntity;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.union;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.values;

@Component
public class CreateSectionRepositoryImpl implements CreateSectionRepository {

    private final NeptuneClient neptuneClient;

    public CreateSectionRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public String addOne(
        String authorId, String articleId, SectionEntity sectionEntity, List<String> incrementSectionIds) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis() / 1000L;
        String sectionId = g.addV(VertexLabel.SECTION.getString())
                            .property(SectionVertexProperty.TITLE.getString(), sectionEntity.getTitle())
                            .property(SectionVertexProperty.TEXT.getString(), sectionEntity.getText())
                            .property(SectionVertexProperty.CREATE_TIME.getString(), now)
                            .property(SectionVertexProperty.UPDATE_TIME.getString(), now)
                            .next()
                            .id()
                            .toString();


        g.V(sectionEntity.getId())
         .addE(ArticleToSectionEdge.INCLUDE.getString())
         .from(g.V(articleId))
         .property(ArticleToSectionProperty.NUMBER.getString(), sectionEntity.getNumber())
         .property(ArticleToSectionProperty.CREATED_TIME.getString(), now)
         .property(ArticleToSectionProperty.UPDATED_TIME.getString(), now)
         .next();

        // 追加するセクションより後のものは番号をインクリメントする
        g.V(incrementSectionIds)
         .inE(ArticleToSectionEdge.INCLUDE.getString())
         .property(ArticleToSectionProperty.NUMBER.getString(),
                   union(values(ArticleToSectionProperty.NUMBER.getString()), __.constant(1).sum()));

        g.V(authorId)
         .addE(UserToSectionEdge.CREATED.getString())
         .property(UserToSectionProperty.CREATED_TIME.getString(), now)
         .next();

        //TODO: Algoliaにデータ追加する
        return sectionId;
    }
}
