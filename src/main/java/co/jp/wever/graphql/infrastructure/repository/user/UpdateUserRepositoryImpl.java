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

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.outV;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;
import static org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality.single;

@Component
public class UpdateUserRepositoryImpl implements UpdateUserRepository {

    private final NeptuneClient neptuneClient;

    public UpdateUserRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    public void updateOne(UserEntity userEntity) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis() / 1000L;

        // サインアップ時にユーザー作成に失敗する可能性もあるので、アップサートにする
        g.V()
         .has(VertexLabel.USER.getString(), "id", userEntity.getUserId())
         .fold()
         .coalesce(unfold().V(userEntity.getUserId())
                           .property(single, UserVertexProperty.DISPLAY_NAME.getString(), userEntity.getDisplayName())
                           .property(single, UserVertexProperty.URL.getString(), userEntity.getUrl())
                           .property(single, UserVertexProperty.IMAGE_URL.getString(), userEntity.getImageUrl())
                           .property(single, UserVertexProperty.UPDATED_TIME.getString(), now),
                   g.addV(VertexLabel.USER.getString()).property("id", userEntity.getUserId()))
         .property(UserVertexProperty.DISPLAY_NAME.getString(), userEntity.getDisplayName())
         .property(UserVertexProperty.DESCRIPTION.getString(), userEntity.getDescription())
         .property(UserVertexProperty.URL.getString(), userEntity.getUrl())
         .property(UserVertexProperty.IMAGE_URL.getString(), userEntity.getImageUrl())
         .property(UserVertexProperty.UPDATED_TIME.getString(), now)
         .next();

        List<String> tagIds = userEntity.getTags().stream().map(t -> t.getTagId()).collect(Collectors.toList());

        g.V(userEntity.getUserId()).outE(UserToTagEdge.RELATED.getString()).drop().iterate();
        g.V(tagIds).addE(UserToTagEdge.RELATED.getString()).from(g.V(userEntity.getUserId())).next();

        //TODO: Algoliaにデータ追加する
    }

    public void followUser(String targetUserId, String followerUserId) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis() / 1000L;

        g.V(followerUserId)
         .as("follower")
         .V(targetUserId)
         .coalesce(g.V(targetUserId).inE(UserToUserEdge.FOLLOWED.getString()).where(outV().as("follower")),
                   g.V(targetUserId)
                    .addE(UserToUserEdge.FOLLOWED.getString())
                    .property(UserToUserProperty.FOLLOWED_TIME.getString(), now)
                    .from(g.V(followerUserId)))
         .next();
    }

    public void unFollowUser(String targetUserId, String followerUserId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        g.V(followerUserId)
         .as("follower")
         .V(targetUserId)
         .inE(UserToUserEdge.FOLLOWED.getString())
         .where(outV().as("follower"))
         .drop()
         .iterate();
    }
}
