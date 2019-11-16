package co.jp.wever.graphql.infrastructure.repository.tag;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.repository.tag.FindTagRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.TagVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.entity.tag.TagStatisticEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.tag.TagStatisticEntity;

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

    public List<TagStatisticEntity> famousTags() {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<Object, Object>> results = g.V()
                                             .hasLabel(VertexLabel.TAG.getString())
                                             .order()
                                             .by(TagVertexProperty.RELATED.getString(), Order.desc)
                                             .limit(5)
                                             .valueMap()
                                             .with(WithOptions.tokens)
                                             .toList();

        return results.stream()
                      .map(r -> TagStatisticEntityConverter.toTagStatisticEntity(r))
                      .collect(Collectors.toList());
    }
}
