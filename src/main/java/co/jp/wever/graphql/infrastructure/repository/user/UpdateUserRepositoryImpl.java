package co.jp.wever.graphql.infrastructure.repository.user;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import co.jp.wever.graphql.domain.repository.user.UpdateUserRepository;
import co.jp.wever.graphql.infrastructure.connector.AlgoliaClient;
import co.jp.wever.graphql.infrastructure.connector.NeptuneClient;
import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToTagEdge;
import co.jp.wever.graphql.infrastructure.constant.edge.property.UserToTagProperty;
import co.jp.wever.graphql.infrastructure.constant.vertex.label.VertexLabel;
import co.jp.wever.graphql.infrastructure.constant.vertex.property.UserVertexProperty;
import co.jp.wever.graphql.infrastructure.datamodel.algolia.UserSearchEntity;
import co.jp.wever.graphql.infrastructure.datamodel.user.UserEntity;
import co.jp.wever.graphql.infrastructure.util.EdgeIdCreator;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.inV;
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

        // サインアップ時にユーザー作成に失敗する可能性もあるので、アップサートにする
        g.V()
         .has(VertexLabel.USER.getString(), "id", userEntity.getUserId())
         .fold()
         .coalesce(unfold().V(userEntity.getUserId())
                           .property(single, UserVertexProperty.DISPLAY_NAME.getString(), userEntity.getDisplayName())
                           .property(single, UserVertexProperty.URL.getString(), userEntity.getUrl())
                           .property(single, UserVertexProperty.IMAGE_URL.getString(), userEntity.getImageUrl())
                           .property(single, UserVertexProperty.DESCRIPTION.getString(), userEntity.getDescription())
                           .property(single, UserVertexProperty.UPDATED_TIME.getString(), now),
                   g.addV(VertexLabel.USER.getString()).property("id", userEntity.getUserId()))
         .property(UserVertexProperty.DISPLAY_NAME.getString(), userEntity.getDisplayName())
         .property(UserVertexProperty.DESCRIPTION.getString(), userEntity.getDescription())
         .property(UserVertexProperty.IMAGE_URL.getString(), userEntity.getImageUrl())
         .property(UserVertexProperty.URL.getString(), userEntity.getUrl())
         .property(UserVertexProperty.UPDATED_TIME.getString(), now)
         .next();

        List<String> tagIds = userEntity.getTags().stream().map(t -> t.getId()).collect(Collectors.toList());

        g.V(userEntity.getUserId())
         .hasLabel(VertexLabel.USER.getString())
         .outE(UserToTagEdge.RELATED.getString())
         .where(inV().hasLabel(VertexLabel.TAG.getString()))
         .drop()
         .iterate();

        if (!tagIds.isEmpty()) {
            tagIds.stream()
                  .forEach(t -> g.V(t)
                                 .addE(UserToTagEdge.RELATED.getString())
                                 .property(T.id,
                                           EdgeIdCreator.createId(userEntity.getUserId(),
                                                                  t,
                                                                  UserToTagEdge.RELATED.getString()))
                                 .from(g.V(userEntity.getUserId()))
                                 .iterate());
        }


        algoliaClient.getUserIndex()
                     .saveObjectAsync(UserSearchEntity.builder()
                                                      .objectID(userEntity.getUserId())
                                                      .description(userEntity.getDescription())
                                                      .displayName(userEntity.getDisplayName())
                                                      .tags(userEntity.getTags()
                                                                      .stream()
                                                                      .map(u -> u.getName())
                                                                      .collect(Collectors.toList()))
                                                      .build());

    }


    public void updateImageUrl(String imageUrl, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(userId)
         .hasLabel(VertexLabel.USER.getString())
         .property(single, UserVertexProperty.IMAGE_URL.getString(), imageUrl)
         .property(single, UserVertexProperty.UPDATED_TIME.getString(), now)
         .next();
    }

    public void updateTags(List<String> tagIds, String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(userId)
         .hasLabel(VertexLabel.USER.getString())
         .property(single, UserVertexProperty.UPDATED_TIME.getString(), now)
         .next();

        g.V(userId)
         .hasLabel(VertexLabel.USER.getString())
         .outE(UserToTagEdge.RELATED.getString())
         .where(inV().hasLabel(VertexLabel.TAG.getString()))
         .drop()
         .iterate();

        if (!tagIds.isEmpty()) {
            g.V(tagIds)
             .hasLabel(VertexLabel.TAG.getString())
             .addE(UserToTagEdge.RELATED.getString())
             .property(UserToTagProperty.CREATED_TIME.getString(), now)
             .property(UserToTagProperty.UPDATED_TIME.getString(), now)
             .from(g.V(userId))
             .iterate();
        }
    }
}
