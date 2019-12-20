package co.jp.wever.graphql.infrastructure.repository.tag;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.tag.TagMutationRepository;
import co.jp.wever.graphql.infrastructure.connector.AlgoliaClient;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.DateProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.TagVertexProperty;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.TagSearchEntity;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;

@Component
public class TagMutationRepositoryImpl implements TagMutationRepository {
    private final NeptuneClient neptuneClient;
    private final AlgoliaClient algoliaClient;

    public TagMutationRepositoryImpl(
        NeptuneClient neptuneClient, AlgoliaClient algoliaClient) {
        this.neptuneClient = neptuneClient;
        this.algoliaClient = algoliaClient;
    }

    @Override
    public String createTag(String name) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        String tagId = g.V()
                        .hasLabel(VertexLabel.TAG.getString())
                        .has(TagVertexProperty.NAME.getString(), name)
                        .fold()
                        .coalesce(unfold(),
                                  g.addV(VertexLabel.TAG.getString())
                                   .property(TagVertexProperty.NAME.getString(), name)
                                   .property(DateProperty.CREATE_TIME.getString(), now)
                                   .property(DateProperty.UPDATE_TIME.getString(), now))
                        .next()
                        .id()
                        .toString();

        algoliaClient.getTagIndex()
                     .saveObjectAsync(TagSearchEntity.builder().objectID(tagId).name(name).relatedCount(0).build());
        return tagId;
    }
}
