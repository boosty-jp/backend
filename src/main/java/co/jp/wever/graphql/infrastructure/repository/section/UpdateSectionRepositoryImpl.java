package co.jp.wever.graphql.infrastructure.repository.section;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.domainmodel.section.UpdateSection;
import co.jp.wever.graphql.domain.repository.section.UpdateSectionRepository;
import co.jp.wever.graphql.infrastructure.connector.AlgoliaClient;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToSectionProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.SectionVertexProperty;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.SectionSearchEntity;
import co.jp.wever.graphql.infrastructure.util.EdgeIdCreator;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;
import static org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality.single;

@Component
public class UpdateSectionRepositoryImpl implements UpdateSectionRepository {

    private final NeptuneClient neptuneClient;
    private final AlgoliaClient algoliaClient;

    public UpdateSectionRepositoryImpl(
        NeptuneClient neptuneClient, AlgoliaClient algoliaClient) {
        this.neptuneClient = neptuneClient;
        this.algoliaClient = algoliaClient;
    }

    @Override
    public void updateOne(UpdateSection updateSection) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(updateSection.getId())
         .property(single, SectionVertexProperty.TITLE.getString(), updateSection.getTitle())
         .property(single, SectionVertexProperty.TEXT.getString(), updateSection.getText())
         .property(single, SectionVertexProperty.UPDATED_TIME.getString(), now)
         .next();
    }

    @Override
    public void likeOne(String sectionId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.E(EdgeIdCreator.createId(userId, sectionId, UserToSectionEdge.LIKED.getString()))
         .fold()
         .coalesce(unfold(),
                   g.V(sectionId)
                    .addE(UserToSectionEdge.LIKED.getString())
                    .property(T.id, EdgeIdCreator.createId(userId, sectionId, UserToSectionEdge.LIKED.getString()))
                    .property(UserToSectionProperty.CREATED_TIME.getString(), now)
                    .property(UserToSectionProperty.UPDATED_TIME.getString(), now)
                    .from(g.V(userId).hasLabel(VertexLabel.USER.getString())))
         .iterate();

        long like = g.V(sectionId).inE(UserToSectionEdge.LIKED.getString()).count().next();
        g.V(sectionId).property(single, SectionVertexProperty.LIKED.getString(), like).next();
        algoliaClient.getSectionIndex()
                     .partialUpdateObjectAsync(SectionSearchEntity.builder().objectID(sectionId).like(like).build());

    }

    @Override
    public void deleteLikeOne(String sectionId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        g.E(EdgeIdCreator.createId(userId, sectionId, UserToSectionEdge.LIKED.getString())).drop().iterate();

        long like = g.V(sectionId).inE(UserToSectionEdge.LIKED.getString()).count().next();
        g.V(sectionId).property(single, SectionVertexProperty.LIKED.getString(), like).next();
        algoliaClient.getSectionIndex()
                     .partialUpdateObjectAsync(SectionSearchEntity.builder().objectID(sectionId).like(like).build());
    }
}
