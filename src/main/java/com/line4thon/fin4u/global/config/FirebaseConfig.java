package com.line4thon.fin4u.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

@Configuration
@Profile("!test")
public class FirebaseConfig {

    @Value("${FIREBASE_SERVICE_ACCOUNT_BASE64}")
    private String serviceAccountBase64;

    @Bean
    public FirebaseApp firebaseApp() throws Exception {

        if (FirebaseApp.getApps().isEmpty()) {

            byte[] decodedBytes = Base64.getDecoder().decode(serviceAccountBase64);
            InputStream serviceAccountStream = new ByteArrayInputStream(decodedBytes);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .build();

            return FirebaseApp.initializeApp(options);
        }

        return FirebaseApp.getInstance();
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
