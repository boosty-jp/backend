package jp.boosty.backend.infrastructure.repository.page;

import jp.boosty.backend.infrastructure.constant.edge.EdgeLabel;
import jp.boosty.backend.infrastructure.constant.vertex.property.DateProperty;
import jp.boosty.backend.infrastructure.constant.vertex.property.PageVertexProperty;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import jp.boosty.backend.domain.domainmodel.page.Page;
import jp.boosty.backend.domain.repository.page.PageMutationRepository;
import jp.boosty.backend.infrastructure.connector.NeptuneClient;
import jp.boosty.backend.infrastructure.constant.vertex.label.VertexLabel;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.inV;
import static org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality.single;

@Component
public class PageMutationRepositoryImpl implements PageMutationRepository {
    private final NeptuneClient neptuneClient;

    public PageMutationRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public void save(String bookId, String pageId, Page page) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        updatePageVertex(g, pageId, page, now);

    }

    @Override
    public void delete(String pageId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        g.V(pageId).hasLabel(VertexLabel.PAGE.getString()).drop().iterate();
    }

    @Override
    public void like(String pageId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        g.V(pageId)
         .hasLabel(VertexLabel.PAGE.getString())
         .addE(EdgeLabel.LIKE.getString())
         .from(g.V(userId).hasLabel(VertexLabel.USER.getString()))
         .next();
    }

    @Override
    public void unLike(String pageId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        g.V(userId)
         .hasLabel(VertexLabel.USER.getString())
         .outE(EdgeLabel.LIKE.getString())
         .where(inV().hasId(pageId).hasLabel(VertexLabel.PAGE.getString()))
         .drop()
         .iterate();
    }

    @Override
    public void updateTrialRead(String pageId, boolean canPreview, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        g.V(pageId)
         .hasLabel(VertexLabel.PAGE.getString())
         .property(single, PageVertexProperty.CAN_PREVIEW.getString(), canPreview)
         .next();
    }

    private void updatePageVertex(GraphTraversalSource g, String pageId, Page page, long now) {
        g.V(pageId)
         .hasLabel(VertexLabel.PAGE.getString())
         .property(single, PageVertexProperty.TITLE.getString(), page.getTitle().getValue())
         .property(single, PageVertexProperty.TEXT.getString(), page.getText().getValue())
         .property(single, DateProperty.UPDATE_TIME.getString(), now)
         .next();
    }
}
