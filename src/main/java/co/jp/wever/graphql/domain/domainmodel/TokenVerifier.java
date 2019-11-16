package co.jp.wever.graphql.domain.domainmodel;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.domain.GraphQLCustomException;
import co.jp.wever.graphql.infrastructure.constant.GraphQLErrorMessage;

@Component
public class TokenVerifier {

    private RSAKeyProvider rsaKeyProvider;

    public TokenVerifier(RSAKeyProvider rsaKeyProvider) {
        this.rsaKeyProvider = rsaKeyProvider;
    }

    private DecodedJWT verify(String token) {
        Algorithm algorithm = Algorithm.RSA256(rsaKeyProvider);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();

        return jwtVerifier.verify(token);
    }

    public String getUserId(String token) {
        DecodedJWT decode;

        try {
            decode = this.verify(token);
        } catch (JWTDecodeException e) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.INVALID_TOKEN.getString());
        } catch (JWTVerificationException e) {
            throw new GraphQLCustomException(HttpStatus.BAD_REQUEST.value(),
                                             GraphQLErrorMessage.EXPIRED_TOKEN.getString());
        } catch (Exception e) {
            throw new GraphQLCustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                             GraphQLErrorMessage.INTERNAL_SERVER_ERROR.getString());
        }

        return decode.getClaims().get("username").asString();
    }
}
