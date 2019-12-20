package co.jp.wever.graphql.infrastructure.repository.tag;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.repository.tag.TagQueryRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.EdgeLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.converter.entity.tag.TagEntityConverter;
import co.jp.wever.graphql.infrastructure.datamodel.tag.TagEntity;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.select;

@Component
public class TagQueryRepositoryImpl implements TagQueryRepository {
    private final NeptuneClient neptuneClient;

    public TagQueryRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    public List<TagEntity> famousTags() {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> results = g.V()
                                             .hasLabel(VertexLabel.TAG.getString())
                                             .project("tag", "relatedCount")
                                             .by(__.valueMap().with(WithOptions.tokens))
                                             .by(__.in(EdgeLabel.RELATED_TO.getString())
                                                   .hasLabel(VertexLabel.ARTICLE.getString(),
                                                             VertexLabel.COURSE.getString())
                                                   .count())
                                             .order()
                                             .by(select("relatedCount"), Order.desc)
                                             .limit(5)
                                             .toList();

        return results.stream()
                      .map(r -> TagEntityConverter.toTagEntityWithRelatedCount((Map<Object, Object>) r.get("tag")))
                      .collect(Collectors.toList());
    }
}
