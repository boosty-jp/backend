package co.jp.wever.graphql.domainmodel;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.RSAKeyProvider;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

class AwsCognitoRSAKeyProviderTest {

    @Test
    void getPublicKeyById() {
        String aws_cognito_region = "us-east-1"; // Replace this with your aws cognito region
        String aws_user_pools_id = "us-east-1_7DEw1nt5r"; // Replace this with your aws user pools id
        RSAKeyProvider keyProvider = new AwsCognitoRSAKeyProvider(aws_cognito_region, aws_user_pools_id);
        Algorithm algorithm = Algorithm.RSA256(keyProvider);
        JWTVerifier jwtVerifier = JWT.require(algorithm)
            //.withAudience("2qm9sgg2kh21masuas88vjc9se") // Validate your apps audience if needed
            .build();

        String token = "eyJraWQiOiJzYysyeHhlYmJZaHJISzZEYlYwMG13WHBEeTN6VU13c24wRWw1S0FmSzlrPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI2ZWNiZjI0ZS1kODJjLTQ0NTctODI3YS1jNjg4ZjBlOWM3MTYiLCJjb2duaXRvOmdyb3VwcyI6WyJQYWlkTWVtYmVycyJdLCJldmVudF9pZCI6IjNjYjk2Y2E4LTQxMTQtNDIzYy05YzNmLWNkNjk0ZTU0YTA3ZiIsInRva2VuX3VzZSI6ImFjY2VzcyIsInNjb3BlIjoiYXdzLmNvZ25pdG8uc2lnbmluLnVzZXIuYWRtaW4iLCJhdXRoX3RpbWUiOjE1NzA2MTczMzQsImlzcyI6Imh0dHBzOlwvXC9jb2duaXRvLWlkcC5hcC1ub3J0aGVhc3QtMS5hbWF6b25hd3MuY29tXC9hcC1ub3J0aGVhc3QtMV94Ukl2RENxYUQiLCJleHAiOjE1NzA4NDc1MTQsImlhdCI6MTU3MDg0MzkxNCwianRpIjoiYjZmZDg0NzEtNWU1NC00OTczLWI5ODEtNWU0ZDIxMDIzMzJmIiwiY2xpZW50X2lkIjoiNG4zZmp1dXBxam9oMGtna21tdWY2M2xtMnIiLCJ1c2VybmFtZSI6ImhvZ2UifQ.pESG5YsWLgSZjKGMF-qdbbQQyhqTgG_g9M7M4F3NUZsvS2MMEo1j0WMrSicVDBBQKk0uSyI64jSu16E0Qt8Yvf8vbqEIE0nUk3U3NIbO0n45TitvGrHkRRnFsHKJs9oxtVtG8enKv6P4HcfLslvTBUDZyCgY_4O8yWWh30dFgPpPAZSZpsvlh3vjO44v6x_YthnM0ddeFgwWGwnP27YqUrcwC61AKMNyDMr-JEPgdm5WyJEUkFfQpXS2yolREAn4aPHH6Iifl7uJ9YdBJhqKtORBTEvk7zjEJ9XDWgiMoMDhOEBPONizU2KPVN3wqJK6dnS2o6hf6BYGU44B-u_5_g"; // Replace this with your JWT token
        jwtVerifier.verify(token);
        assertTrue(true);
    }

    @Test
    void getPrivateKey() {
    }

    @Test
    void getPrivateKeyId() {
    }
}