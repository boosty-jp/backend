package co.jp.wever.graphql.infrastructure.repository.user;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.application.datamodel.request.user.UserSettingInput;
import co.jp.wever.graphql.domain.domainmodel.user.User;
import co.jp.wever.graphql.domain.repository.user.UserMutationRepository;
import co.jp.wever.graphql.infrastructure.connector.AlgoliaClient;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.UserVertexProperty;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.UserSearchEntity;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;
import static org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality.single;

@Component
public class UserMutationRepositoryImpl implements UserMutationRepository {
    private final NeptuneClient neptuneClient;
    private final AlgoliaClient algoliaClient;

    public UserMutationRepositoryImpl(
        NeptuneClient neptuneClient, AlgoliaClient algoliaClient) {
        this.neptuneClient = neptuneClient;
        this.algoliaClient = algoliaClient;
    }

    @Override
    public String createOne(String displayName, String imageUrl, String uid) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis();

        String userId = g.V(uid)
                         .fold()
                         .coalesce(unfold(),
                                   g.addV(VertexLabel.USER.getString())
                                    .property(T.id, uid)
                                    .property(UserVertexProperty.DISPLAY_NAME.getString(), displayName)
                                    .property(UserVertexProperty.IMAGE_URL.getString(), imageUrl)
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
                                                      .displayName(displayName)
                                                      .imageUrl(imageUrl)
                                                      .description("")
                                                      .build());

        return userId;
    }


    public void updateOne(User user) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis();

        // サインアップ時にユーザー作成に失敗している可能性もあるので、アップサートにする
        g.V()
         .hasId(user.getUserId().getValue())
         .hasLabel(VertexLabel.USER.getString())
         .fold()
         .coalesce(unfold().V(user.getUserId().getValue())
                           .property(single,
                                     UserVertexProperty.DISPLAY_NAME.getString(),
                                     user.getDisplayName().getValue())
                           .property(single,
                                     UserVertexProperty.DESCRIPTION.getString(),
                                     user.getDescription().getValue())
                           .property(single, UserVertexProperty.URL.getString(), user.getUrl().getValue())
                           .property(single, UserVertexProperty.IMAGE_URL.getString(), user.getImageUrl().getValue())
                           .property(single, UserVertexProperty.TWITTER_ID.getString(), user.getTwitterId().getValue())
                           .property(single,
                                     UserVertexProperty.FACEBOOK_ID.getString(),
                                     user.getFacebookId().getValue())
                           .property(single, UserVertexProperty.UPDATED_TIME.getString(), now),
                   g.addV(VertexLabel.USER.getString()).property("id", user.getUserId().getValue()))
         .property(UserVertexProperty.DISPLAY_NAME.getString(), user.getDisplayName().getValue())
         .property(UserVertexProperty.DESCRIPTION.getString(), user.getDescription().getValue())
         .property(UserVertexProperty.URL.getString(), user.getUrl().getValue())
         .property(UserVertexProperty.IMAGE_URL.getString(), user.getImageUrl().getValue())
         .property(UserVertexProperty.TWITTER_ID.getString(), user.getTwitterId().getValue())
         .property(UserVertexProperty.FACEBOOK_ID.getString(), user.getFacebookId().getValue())
         .property(UserVertexProperty.LEARN_PUBLIC.getString(), true)
         .property(UserVertexProperty.LIKE_PUBLIC.getString(), true)
         .property(UserVertexProperty.SKILL_PUBLIC.getString(), true)
         .property(UserVertexProperty.DELETED.getString(), false)
         .property(UserVertexProperty.UPDATED_TIME.getString(), now)
         .next();

        algoliaClient.getUserIndex()
                     .saveObjectAsync(UserSearchEntity.builder()
                                                      .objectID(user.getUserId().getValue())
                                                      .description(user.getDescription().getValue())
                                                      .displayName(user.getDisplayName().getValue())
                                                      .build());
    }

    public void updateSetting(String userId, UserSettingInput userSettingInput) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(userId)
         .property(single, UserVertexProperty.LEARN_PUBLIC.getString(), userSettingInput.getLearnPublic())
         .property(single, UserVertexProperty.LIKE_PUBLIC.getString(), userSettingInput.getLikePublic())
         .property(single, UserVertexProperty.SKILL_PUBLIC.getString(), userSettingInput.getSkillPublic())
         .property(single, UserVertexProperty.UPDATED_TIME.getString(), now)
         .next();
    }

    @Override
    public void deleteUser(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(userId)
         .property(single, UserVertexProperty.DELETED.getString(), true)
         .property(single, UserVertexProperty.UPDATED_TIME.getString(), now)
         .next();

        System.out.println(g.V(userId).valueMap().toList());
    }
}
