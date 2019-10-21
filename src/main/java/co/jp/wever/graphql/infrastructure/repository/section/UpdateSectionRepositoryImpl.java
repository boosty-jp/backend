package co.jp.wever.graphql.infrastructure.repository.section;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.section.UpdateSectionRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToSectionProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.SectionVertexProperty;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionEntity;

@Component
public class UpdateSectionRepositoryImpl implements UpdateSectionRepository {

    private final NeptuneClient neptuneClient;

    public UpdateSectionRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public void updateOne(SectionEntity sectionEntity) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis() / 1000L;
        g.V(sectionEntity.getId())
         .property(SectionVertexProperty.TITLE.name(), sectionEntity.getTitle())
         .property(SectionVertexProperty.TEXT.name(), sectionEntity.getText())
         .property(SectionVertexProperty.UPDATE_TIME.name(), now)
         .next();

        //TODO: Algoliaのデータ更新する
    }

    @Override
    public void likeOne(String sectionId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis() / 1000L;
        g.V(sectionId)
         .addE(UserToSectionEdge.LIKE.name())
         .property(UserToSectionProperty.LIKED_DATE, now)
         .from(g.V(userId))
         .next();
        //TODO: Algoliaのデータ更新する
    }
}
