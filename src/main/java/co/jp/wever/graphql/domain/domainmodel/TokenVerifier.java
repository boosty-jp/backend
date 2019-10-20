package co.jp.wever.graphql.domain.domainmodel;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TokenVerifier {

    private RSAKeyProvider rsaKeyProvider;

    public TokenVerifier(RSAKeyProvider rsaKeyProvider) {
        this.rsaKeyProvider = rsaKeyProvider;
    }

    public void verify(String token) {
        Algorithm algorithm = Algorithm.RSA256(rsaKeyProvider);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();

        DecodedJWT decode = jwtVerifier.verify(token);
    }

    public String getUserId(String token) {
        Algorithm algorithm = Algorithm.RSA256(rsaKeyProvider);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();

        DecodedJWT decode = jwtVerifier.verify(token);
        return decode.getClaims().get("username").asString();
    }
}
