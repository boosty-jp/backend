package jp.boosty.backend.infrastructure.client;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import jp.boosty.backend.domain.GraphQLCustomException;
import jp.boosty.backend.infrastructure.constant.GraphQLErrorMessage;

@Component
public class TokenVerifier {

    private FirebaseClient firebaseClient;

    public TokenVerifier(FirebaseClient firebaseClient) {
        this.firebaseClient = firebaseClient;
    }

    public String getUserId(String token) {
        try {
            FirebaseToken decodedToken = firebaseClient.getFirebaseAuth().verifyIdToken(token);
            return decodedToken.getUid();
        } catch (FirebaseAuthException e) {
            if (e.getErrorCode().equals("id-token-revoked")) {
                throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                                 GraphQLErrorMessage.NEED_RE_AUTH.getString());
            } else {
                throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                                 GraphQLErrorMessage.INVALID_TOKEN.getString());
            }
        }
    }
}
