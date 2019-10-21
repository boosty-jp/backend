package co.jp.wever.graphql.infrastructure.repository.user;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.user.CreateUserRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.UserVertexProperty;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;


@Component
public class CreateUserRepositoryImpl implements CreateUserRepository {

    private final NeptuneClient neptuneClient;

    public CreateUserRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public String createOne(UserEntity userEntity) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis() / 1000L;
        String userId = g.addV(VertexLabel.USER.name())
                         .property(T.id, userEntity.getUserId())
                         .property(UserVertexProperty.DISPLAY_NAME.name(), userEntity.getUserId())
                         .property(UserVertexProperty.CREATE_DATE.name(), now)
                         .next()
                         .id()
                         .toString();

        //TODO: Algoliaにデータ追加する
        return userId;
    }
}
