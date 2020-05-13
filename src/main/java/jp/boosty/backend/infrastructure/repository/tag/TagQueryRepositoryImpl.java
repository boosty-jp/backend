package jp.boosty.backend.infrastructure.repository.tag;

import jp.boosty.backend.domain.repository.tag.TagQueryRepository;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jp.boosty.backend.infrastructure.connector.NeptuneClient;
import jp.boosty.backend.infrastructure.constant.edge.EdgeLabel;
import jp.boosty.backend.infrastructure.constant.vertex.label.VertexLabel;
import jp.boosty.backend.infrastructure.converter.entity.tag.TagEntityConverter;
import jp.boosty.backend.infrastructure.datamodel.tag.TagEntity;

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
                                                   .hasLabel(VertexLabel.PAGE.getString(),
                                                             VertexLabel.BOOK.getString())
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
