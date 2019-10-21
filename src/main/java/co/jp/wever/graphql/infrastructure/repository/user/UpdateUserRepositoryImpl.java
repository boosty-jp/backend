package co.jp.wever.graphql.infrastructure.repository.user;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.repository.user.UpdateUserRepository;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToUserEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToUserProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.UserVertexProperty;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;

@Component
public class UpdateUserRepositoryImpl implements UpdateUserRepository {

    private final NeptuneClient neptuneClient;

    public UpdateUserRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    public void updateOne(UserEntity userEntity) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis() / 1000L;

        // サインアップ時にユーザー作成に失敗する可能性もあるので、アップサート文にする
        g.V()
         .has(VertexLabel.USER.name(), "id", userEntity.getUserId())
         .fold()
         .coalesce(unfold(), g.V().addV(VertexLabel.USER.name()).property("id", userEntity.getUserId()))
         .property(UserVertexProperty.DISPLAY_NAME.name(), userEntity.getDisplayName())
         .property(UserVertexProperty.DESCRIPTION.name(), userEntity.getDescription())
         .property(UserVertexProperty.URL.name(), userEntity.getUrl())
         .property(UserVertexProperty.IMAGE_URL.name(), userEntity.getImageUrl())
         .property(UserVertexProperty.UPDATE_DATE.name(), now)
         .next();

        List<String> tagIds = userEntity.getTags().stream().map(t -> t.getTagId()).collect(Collectors.toList());

        g.V(tagIds).addE(UserToTagEdge.RELATED.name()).from(userEntity.getUserId()).next();


        //TODO: Algoliaにデータ追加する
    }

    public void followUser(String targetUserId, String followerUserId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis() / 1000L;

        g.V(targetUserId)
         .addE(UserToUserEdge.FOLLOW.name())
         .property(UserToUserProperty.FOLLOWED_TIME.name(), now)
         .from(followerUserId)
         .next();
    }

    public void unFollowUser(String targetUserId, String followerUserId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        g.V(targetUserId).inE(UserToUserEdge.FOLLOW.name()).from(followerUserId).drop();
    }
}
