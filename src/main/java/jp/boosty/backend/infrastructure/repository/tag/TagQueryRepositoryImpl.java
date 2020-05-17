package jp.boosty.backend.infrastructure.repository.tag;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.domain.repository.tag.TagQueryRepository;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jp.boosty.backend.infrastructure.connector.NeptuneClient;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;
import jp.boosty.backend.infrastructure.constant.edge.EdgeLabel;
import jp.boosty.backend.infrastructure.constant.vertex.label.VertexLabel;
import jp.boosty.backend.infrastructure.converter.entity.tag.TagEntityConverter;
import jp.boosty.backend.infrastructure.datamodel.tag.TagEntity;

import lombok.extern.slf4j.Slf4j;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.select;

@Slf4j
@Component
public class TagQueryRepositoryImpl implements TagQueryRepository {
    private final NeptuneClient neptuneClient;

    public TagQueryRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    public List<TagEntity> famousTags() {
        GraphTraversalSource g = neptuneClient.newTraversal();

        List<Map<String, Object>> results;
        try{
        results = g.V()
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
        } catch (Exception e) {
            log.error("famousTags error: {}",  e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        return results.stream()
                      .map(r -> TagEntityConverter.toTagEntityWithRelatedCount((Map<Object, Object>) r.get("tag")))
                      .collect(Collectors.toList());
    }
}
