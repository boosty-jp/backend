package jp.boosty.backend.infrastructure.repository.tag;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.domain.domainmodel.tag.TagName;
import jp.boosty.backend.domain.repository.tag.TagMutationRepository;
import jp.boosty.backend.infrastructure.connector.NeptuneClient;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;
import jp.boosty.backend.infrastructure.constant.vertex.label.VertexLabel;
import jp.boosty.backend.infrastructure.constant.vertex.property.DateProperty;
import jp.boosty.backend.infrastructure.constant.vertex.property.TagVertexProperty;

import lombok.extern.slf4j.Slf4j;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;

@Slf4j
@Component
public class TagMutationRepositoryImpl implements TagMutationRepository {
    private final NeptuneClient neptuneClient;

    public TagMutationRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public String createTag(TagName name) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        try {
            return g.V()
                    .hasLabel(VertexLabel.TAG.getString())
                    .has(TagVertexProperty.NAME.getString(), name.getValue())
                    .fold()
                    .coalesce(unfold(),
                              g.addV(VertexLabel.TAG.getString())
                               .property(TagVertexProperty.NAME.getString(), name.getValue())
                               .property(DateProperty.CREATE_TIME.getString(), now)
                               .property(DateProperty.UPDATE_TIME.getString(), now))
                    .next()
                    .id()
                    .toString();
        } catch (Exception e) {
            log.error("createTag error: {} {}", name, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }
}
