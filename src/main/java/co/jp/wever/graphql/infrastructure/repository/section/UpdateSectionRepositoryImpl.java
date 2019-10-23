package co.jp.wever.graphql.infrastructure.repository.section;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.section.UpdateSectionRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToSectionEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToSectionProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.SectionVertexProperty;
import co.jp.wever.graphql.infrastructure.datamodel.section.SectionEntity;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.outV;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal.Symbols.outV;
import static org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality.single;

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
         .property(single, SectionVertexProperty.TITLE.getString(), sectionEntity.getTitle())
         .property(single, SectionVertexProperty.TEXT.getString(), sectionEntity.getText())
         .property(single, SectionVertexProperty.UPDATE_TIME.getString(), now)
         .next();

        //TODO: Algoliaのデータ更新する
    }

    @Override
    public void likeOne(String sectionId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis() / 1000L;

        g.V(sectionId)
         .addE(UserToSectionEdge.LIKED.getString())
         .property(T.id, sectionId + "-" + userId)
         .property(UserToSectionProperty.LIKED_TIME.getString(), now)
         .from(g.V(userId))
         .iterate();
        //TODO: Algoliaのデータ更新する
    }
}
