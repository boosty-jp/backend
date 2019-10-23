package co.jp.wever.graphql.infrastructure.repository.tag;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.TagVertexProperty;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;

@Component
public class CreateTagRepositoryImpl {
    private final NeptuneClient neptuneClient;

    public CreateTagRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    public String createTag(String name) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis() / 1000L;

        // TODO: 同じタグがあったときのトランザクション制御したい
        String tagId = g.addV(VertexLabel.TAG.getString())
                        .property(TagVertexProperty.NAME.getString(), name)
                        .property(TagVertexProperty.CREATED_TIME.getString(), now)
                        .next()
                        .id()
                        .toString();

        //TODO: Algoliaにデータ追加する
        return tagId;
    }
}
