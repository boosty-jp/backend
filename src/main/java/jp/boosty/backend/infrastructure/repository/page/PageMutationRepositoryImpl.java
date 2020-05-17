package jp.boosty.backend.infrastructure.repository.page;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;
import jp.boosty.backend.infrastructure.constant.edge.EdgeLabel;
import jp.boosty.backend.infrastructure.constant.vertex.property.DateProperty;
import jp.boosty.backend.infrastructure.constant.vertex.property.PageVertexProperty;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import jp.boosty.backend.domain.domainmodel.page.Page;
import jp.boosty.backend.domain.repository.page.PageMutationRepository;
import jp.boosty.backend.infrastructure.connector.NeptuneClient;
import jp.boosty.backend.infrastructure.constant.vertex.label.VertexLabel;

import lombok.extern.slf4j.Slf4j;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.inV;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.user;
import static org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality.single;

@Slf4j
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

        try {
            g.V(pageId).hasLabel(VertexLabel.PAGE.getString()).drop().iterate();
        } catch (Exception e) {
            log.error("delete error: {} {} {}", pageId, userId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public void like(String pageId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        try {
            g.V(pageId)
             .hasLabel(VertexLabel.PAGE.getString())
             .addE(EdgeLabel.LIKE.getString())
             .from(g.V(userId).hasLabel(VertexLabel.USER.getString()))
             .next();
        } catch (Exception e) {
            log.error("like error: {} {} {}", pageId, userId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public void unLike(String pageId, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        try {
            g.V(userId)
             .hasLabel(VertexLabel.USER.getString())
             .outE(EdgeLabel.LIKE.getString())
             .where(inV().hasId(pageId).hasLabel(VertexLabel.PAGE.getString()))
             .drop()
             .iterate();
        } catch (Exception e) {
            log.error("unlike error: {} {} {}", pageId, userId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public void updateTrialRead(String pageId, boolean canPreview, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        try {
            g.V(pageId)
             .hasLabel(VertexLabel.PAGE.getString())
             .property(single, PageVertexProperty.CAN_PREVIEW.getString(), canPreview)
             .next();
        } catch (Exception e) {
            log.error("updateTrialRead error: {} {} {} {}", pageId, userId, canPreview, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    private void updatePageVertex(GraphTraversalSource g, String pageId, Page page, long now) {
        try {
            g.V(pageId)
             .hasLabel(VertexLabel.PAGE.getString())
             .property(single, PageVertexProperty.TITLE.getString(), page.getTitle().getValue())
             .property(single, PageVertexProperty.TEXT.getString(), page.getText().getValue())
             .property(single, DateProperty.UPDATE_TIME.getString(), now)
             .next();
        } catch (Exception e) {
            log.error("updatePageVertex error: {} {} {}", pageId, page, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
