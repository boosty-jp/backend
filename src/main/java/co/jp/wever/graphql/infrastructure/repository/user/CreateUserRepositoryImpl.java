package co.jp.wever.graphql.infrastructure.repository.user;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.repository.user.CreateUserRepository;
import co.jp.wever.graphql.infrastructure.connector.AlgoliaClient;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.UserVertexProperty;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.UserSearchEntity;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;


@Component
public class CreateUserRepositoryImpl implements CreateUserRepository {

    private final NeptuneClient neptuneClient;
    private final AlgoliaClient algoliaClient;

    public CreateUserRepositoryImpl(
        NeptuneClient neptuneClient, AlgoliaClient algoliaClient) {
        this.neptuneClient = neptuneClient;
        this.algoliaClient = algoliaClient;
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
                                    .property(UserVertexProperty.DISPLAY_NAME.getString(), userEntity.getDisplayName())
                                    .property(UserVertexProperty.IMAGE_URL.getString(), userEntity.getImageUrl())
                                    .property(UserVertexProperty.DESCRIPTION.getString(), "")
                                    .property(UserVertexProperty.URL.getString(), "")
                                    .property(UserVertexProperty.TWITTER_ID.getString(), "")
                                    .property(UserVertexProperty.FACEBOOK_ID.getString(), "")
                                    .property(UserVertexProperty.LEARN_PUBLIC.getString(), true)
                                    .property(UserVertexProperty.LIKE_PUBLIC.getString(), true)
                                    .property(UserVertexProperty.SKILL_PUBLIC.getString(), true)
                                    .property(UserVertexProperty.DELETED.getString(), false)
                                    .property(UserVertexProperty.CREATED_TIME.getString(), now)
                                    .property(UserVertexProperty.UPDATED_TIME.getString(), now))
                         .next()
                         .id()
                         .toString();

        algoliaClient.getUserIndex()
                     .saveObjectAsync(UserSearchEntity.builder()
                                                      .objectID(userId)
                                                      .displayName(userEntity.getDisplayName())
                                                      .imageUrl(userEntity.getImageUrl())
                                                      .description("")
                                                      .build());

        return userId;
    }
}
