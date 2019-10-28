package co.jp.wever.graphql.infrastructure.repository.user;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.user.CreateUserRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.UserVertexProperty;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;


@Component
public class CreateUserRepositoryImpl implements CreateUserRepository {

    private final NeptuneClient neptuneClient;

    public CreateUserRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public String createOne(UserEntity userEntity) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis();

        String userId = g.V(userEntity.getUserId())
         .fold()
         .coalesce(unfold(),
                   g.addV(VertexLabel.USER.getString())
                    .property(T.id, userEntity.getUserId())
                    .property(UserVertexProperty.DISPLAY_NAME.getString(), userEntity.getUserId())
                    .property(UserVertexProperty.CREATED_TIME.getString(), now))
         .next()
         .id()
         .toString();

        //TODO: Algoliaにデータ追加する
        return userId;
    }
}
