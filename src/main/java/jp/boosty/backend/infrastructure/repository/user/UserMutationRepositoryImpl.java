package jp.boosty.backend.infrastructure.repository.user;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.oauth.TokenResponse;
import com.stripe.net.OAuth;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.domain.domainmodel.user.User;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;
import jp.boosty.backend.infrastructure.constant.edge.EdgeLabel;
import jp.boosty.backend.infrastructure.constant.vertex.property.DateProperty;
import jp.boosty.backend.infrastructure.constant.vertex.property.UserVertexProperty;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import jp.boosty.backend.domain.repository.user.UserMutationRepository;
import jp.boosty.backend.infrastructure.connector.NeptuneClient;
import jp.boosty.backend.infrastructure.constant.vertex.label.VertexLabel;

import lombok.extern.slf4j.Slf4j;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;
import static org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality.single;

@Slf4j
@Component
public class UserMutationRepositoryImpl implements UserMutationRepository {
    private final NeptuneClient neptuneClient;

    @Value("${stripe.apiKey}")
    private String STRIPE_API_KEY;

    public UserMutationRepositoryImpl(NeptuneClient neptuneClient) {
        this.neptuneClient = neptuneClient;
    }

    @Override
    public String createOne(String displayName, String imageUrl, String uid) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis();
        try {
            return g.V(uid)
                    .fold()
                    .coalesce(unfold(),
                              g.addV(VertexLabel.USER.getString())
                               .property(T.id, uid)
                               .property(UserVertexProperty.DISPLAY_NAME.getString(), displayName)
                               .property(UserVertexProperty.IMAGE_URL.getString(), imageUrl)
                               .property(UserVertexProperty.DESCRIPTION.getString(), "")
                               .property(UserVertexProperty.URL.getString(), "")
                               .property(UserVertexProperty.TWITTER_ID.getString(), "")
                               .property(UserVertexProperty.GITHUB_ID.getString(), "")
                               .property(UserVertexProperty.STRIPE_ID.getString(), "")
                               .property(UserVertexProperty.DELETED.getString(), false)
                               .property(UserVertexProperty.CREATED_TIME.getString(), now)
                               .property(UserVertexProperty.UPDATED_TIME.getString(), now))
                    .next()
                    .id()
                    .toString();
        } catch (Exception e) {
            log.error("createOne error: {} {} {} {}", displayName, imageUrl, uid, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }


    public void updateOne(User user) {
        GraphTraversalSource g = neptuneClient.newTraversal();

        long now = System.currentTimeMillis();

        // サインアップ時にユーザー作成に失敗している可能性もあるので、アップサートにする
        try {
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
                               .property(single,
                                         UserVertexProperty.IMAGE_URL.getString(),
                                         user.getImageUrl().getValue())
                               .property(single,
                                         UserVertexProperty.TWITTER_ID.getString(),
                                         user.getTwitterId().getValue())
                               .property(single,
                                         UserVertexProperty.GITHUB_ID.getString(),
                                         user.getGithubId().getValue())
                               .property(single, UserVertexProperty.UPDATED_TIME.getString(), now),
                       g.addV(VertexLabel.USER.getString()).property("id", user.getUserId().getValue()))
             .property(UserVertexProperty.DISPLAY_NAME.getString(), user.getDisplayName().getValue())
             .property(UserVertexProperty.DESCRIPTION.getString(), user.getDescription().getValue())
             .property(UserVertexProperty.URL.getString(), user.getUrl().getValue())
             .property(UserVertexProperty.IMAGE_URL.getString(), user.getImageUrl().getValue())
             .property(UserVertexProperty.TWITTER_ID.getString(), user.getTwitterId().getValue())
             .property(UserVertexProperty.GITHUB_ID.getString(), user.getGithubId().getValue())
             .property(UserVertexProperty.STRIPE_ID.getString(), "")
             .property(UserVertexProperty.DELETED.getString(), false)
             .property(UserVertexProperty.UPDATED_TIME.getString(), now)
             .next();
        } catch (Exception e) {
            log.error("updateOne error: {} {}", user, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public void deleteUser(String userId) {
        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        try{
        g.V(userId)
         .property(single, UserVertexProperty.DELETED.getString(), true)
         .property(single, UserVertexProperty.UPDATED_TIME.getString(), now)
         .next();

        g.V(userId)
         .hasLabel(VertexLabel.USER.getString())
         .outE(EdgeLabel.PUBLISH.getString())
         .hasLabel(VertexLabel.BOOK.getString())
         .drop()
         .iterate();
        } catch (Exception e) {
            log.error("deleteUser error: {} {}", userId, e.getMessage());
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }
    }

    @Override
    public void registerStripe(String userId, String code) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;

        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("code", code);

        TokenResponse response = OAuth.token(params, null);

        GraphTraversalSource g = neptuneClient.newTraversal();
        long now = System.currentTimeMillis();

        g.V(userId)
         .hasLabel(VertexLabel.USER.getString())
         .property(single, UserVertexProperty.STRIPE_ID.getString(), response.getStripeUserId())
         .property(single, DateProperty.UPDATE_TIME.getString(), now)
         .next();
    }
}
