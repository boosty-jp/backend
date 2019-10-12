package co.jp.wever.graphql.domainmodel;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.RSAKeyProvider;

import org.springframework.stereotype.Component;

@Component
public class TokenVerifier {

    private RSAKeyProvider rsaKeyProvider;

    public TokenVerifier(RSAKeyProvider rsaKeyProvider) {
        this.rsaKeyProvider = rsaKeyProvider;
    }

    public void verify(String token) {
        Algorithm algorithm = Algorithm.RSA256(rsaKeyProvider);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();

        jwtVerifier.verify(token);
    }
}
