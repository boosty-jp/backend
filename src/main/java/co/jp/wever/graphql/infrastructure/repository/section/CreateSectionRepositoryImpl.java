package co.jp.wever.graphql.infrastructure.repository.section;

import org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
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
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionNumberEntity;

import static org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality.single;

@Component
public class CreateSectionRepositoryImpl implements CreateSectionRepository {

    private final NeptuneClient neptuneClient;

    public CreateSectionRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public String addOne(
        String authorId, String articleId, SectionEntity sectionEntity, List<SectionNumberEntity> incrementNumbers) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis();

        String sectionId = g.addV(VertexLabel.SECTION.getString())
                            .property(single, SectionVertexProperty.TITLE.getString(), sectionEntity.getTitle())
                            .property(single, SectionVertexProperty.TEXT.getString(), sectionEntity.getText())
                            .property(single, SectionVertexProperty.CREATE_TIME.getString(), now)
                            .property(single, SectionVertexProperty.UPDATE_TIME.getString(), now)
                            .next()
                            .id()
                            .toString();

        g.V(sectionId)
         .addE(ArticleToSectionEdge.INCLUDE.getString())
         .property(T.id, articleId + "-" + sectionId)
         .property(ArticleToSectionProperty.NUMBER.getString(), sectionEntity.getNumber())
         .property(ArticleToSectionProperty.CREATED_TIME.getString(), now)
         .property(ArticleToSectionProperty.UPDATED_TIME.getString(), now)
         .from(g.V(articleId))
         .iterate();


        // TODO: クエリがおもすぎるので改善する
        incrementNumbers.stream().forEach(s -> {
            g.E(articleId + "-" + s.getId())
             .property(ArticleToSectionProperty.NUMBER.getString(), s.getNumber())
             .property(ArticleToSectionProperty.UPDATED_TIME.getString(), now)
             .iterate();
        });

        g.V(authorId)
         .addE(UserToSectionEdge.CREATED.getString())
         .property(T.id, authorId + "-" + sectionId)
         .property(UserToSectionProperty.CREATED_TIME.getString(), now)
         .to(g.V(sectionId))
         .next();

        //TODO: Algoliaにデータ追加する
        return sectionId;
    }
}
