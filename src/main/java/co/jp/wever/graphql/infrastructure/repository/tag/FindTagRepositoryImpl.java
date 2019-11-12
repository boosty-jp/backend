package co.jp.wever.graphql.infrastructure.repository.tag;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.repository.tag.FindTagRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.ArticleToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.PlanToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.TagVertexProperty;
import co.jp.wever.graphql.infrastructure.converter.entity.tag.TagStatisticEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.tag.TagEntity;
import co.jp.wever.graphql.infrastructure.datamodel.tag.TagStatisticEntity;

import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.inE;
import static org.apache.tinkerpop.gremlin.groovy.jsr223.dsl.credential.__.values;

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

        List<Map<String, Object>> results = g.V()
                                             .hasLabel(VertexLabel.TAG.getString())
                                             .project("tag", "count")
                                             .by(__.valueMap().with(WithOptions.tokens))
                                             .by(inE(PlanToTagEdge.RELATED.getString()).count().order().by())
                                             .toList();

        return results.stream()
                      .map(r -> TagStatisticEntityConverter.toTagStatisticEntity(r))
                      .collect(Collectors.toList());
    }
}
