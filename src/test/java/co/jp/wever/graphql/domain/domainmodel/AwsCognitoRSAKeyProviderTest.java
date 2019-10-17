package co.jp.wever.graphql.domain.domainmodel;

import com.auth0.jwt.interfaces.RSAKeyProvider;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AwsCognitoRSAKeyProviderTest {

    @Autowired
    private RSAKeyProvider rsaKeyProvider;

    @Test
    void getPrivateKey_NULLを返す(){
        assertNull(rsaKeyProvider.getPrivateKey());
    }

    @Test
    void getPrivateKeyId_NULLを返す(){
        assertNull(rsaKeyProvider.getPrivateKeyId());
    }

}