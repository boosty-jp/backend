package co.jp.wever.graphql.infrastructure.repository.tag;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.tag.FindTagRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.TagVertexProperty;

@Component
public class FindTagRepositoryImpl implements FindTagRepository {
    private final NeptuneClient neptuneClient;

    public FindTagRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    public boolean exists(String name) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        return g.V().hasLabel(VertexLabel.TAG.getString()).has(TagVertexProperty.NAME.getString(), name).hasNext();
    }
}
