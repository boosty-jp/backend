package co.jp.wever.graphql.infrastructure.repository.user;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.application.datamodel.request.UserSettingInput;
import co.jp.wever.graphql.domain.repository.user.UpdateUserRepository;
import co.jp.wever.graphql.infrastructure.connector.AlgoliaClient;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.UserVertexProperty;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.UserSearchEntity;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;
import static org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality.single;

@Component
public class UpdateUserRepositoryImpl implements UpdateUserRepository {

    private final NeptuneClient neptuneClient;
    private final AlgoliaClient algoliaClient;

    public UpdateUserRepositoryImpl(
        NeptuneClient neptuneClient, AlgoliaClient algoliaClient) {
        this.neptuneClient = neptuneClient;
        this.algoliaClient = algoliaClient;
    }

    public void updateOne(UserEntity userEntity) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis();

        // サインアップ時にユーザー作成に失敗している可能性もあるので、アップサートにする
        g.V()
         .has(VertexLabel.USER.getString(), "id", userEntity.getUserId())
         .fold()
         .coalesce(unfold().V(userEntity.getUserId())
                           .property(single, UserVertexProperty.DISPLAY_NAME.getString(), userEntity.getDisplayName())
                           .property(single, UserVertexProperty.DESCRIPTION.getString(), userEntity.getDescription())
                           .property(single, UserVertexProperty.URL.getString(), userEntity.getUrl())
                           .property(single, UserVertexProperty.IMAGE_URL.getString(), userEntity.getImageUrl())
                           .property(single, UserVertexProperty.TWITTER_ID.getString(), userEntity.getTwitterId())
                           .property(single, UserVertexProperty.FACEBOOK_ID.getString(), userEntity.getFacebookId())
                           .property(single, UserVertexProperty.UPDATED_TIME.getString(), now),
                   g.addV(VertexLabel.USER.getString()).property("id", userEntity.getUserId()))
         .property(UserVertexProperty.DISPLAY_NAME.getString(), userEntity.getDisplayName())
         .property(UserVertexProperty.DESCRIPTION.getString(), userEntity.getDescription())
         .property(UserVertexProperty.URL.getString(), userEntity.getUrl())
         .property(UserVertexProperty.IMAGE_URL.getString(), userEntity.getImageUrl())
         .property(UserVertexProperty.TWITTER_ID.getString(), userEntity.getTwitterId())
         .property(UserVertexProperty.FACEBOOK_ID.getString(), userEntity.getFacebookId())
         .property(UserVertexProperty.LEARN_PUBLIC.getString(), true)
         .property(UserVertexProperty.LIKE_PUBLIC.getString(), true)
         .property(UserVertexProperty.SKILL_PUBLIC.getString(), true)
         .property(UserVertexProperty.DELETED.getString(), false)
         .property(UserVertexProperty.UPDATED_TIME.getString(), now)
         .next();

        algoliaClient.getUserIndex()
                     .saveObjectAsync(UserSearchEntity.builder()
                                                      .objectID(userEntity.getUserId())
                                                      .description(userEntity.getDescription())
                                                      .displayName(userEntity.getDisplayName())
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
}
