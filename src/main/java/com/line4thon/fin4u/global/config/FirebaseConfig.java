package com.line4thon.fin4u.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import java.io.*;

@Configuration public class FirebaseConfig {
    @Value("${fcm.service-account}")
    private String serviceAccountPath;

    @Bean public FirebaseApp firebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            InputStream in;
            if (serviceAccountPath.startsWith("classpath:")) {
                String p = serviceAccountPath.replace("classpath:", ""); in = new ClassPathResource(p).getInputStream();
            } else {
                in = new FileInputStream(serviceAccountPath);
            }
            FirebaseOptions options = FirebaseOptions.builder() .setCredentials(GoogleCredentials.fromStream(in)) .build(); return FirebaseApp.initializeApp(options); }
        return FirebaseApp.getInstance(); }

    @Bean public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp); }
}