package co.jp.wever.graphql.domain.domainmodel;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FirebaseClient {
    private FirebaseAuth firebaseAuth;

    public FirebaseClient(@Value("${firebase.databaseUrl}") String databaseUrl) {
        try {

            FirebaseOptions options =
                new FirebaseOptions.Builder().setCredentials(GoogleCredentials.getApplicationDefault())
                                             .setDatabaseUrl(databaseUrl)
                                             .build();
            FirebaseApp app = FirebaseApp.initializeApp(options);

            this.firebaseAuth = FirebaseAuth.getInstance(app);
        } catch (IOException e) {
            throw new RuntimeException("Firebase init failed");
        }
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }
}
