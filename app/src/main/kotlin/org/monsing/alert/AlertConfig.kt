package org.monsing.alert

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("dev")
@Configuration
class AlertConfig {

    @Bean
    fun firebaseApp(): FirebaseApp = FirebaseApp.initializeApp(firebaseOptions())

    @Bean
    fun firebaseOptions(): FirebaseOptions {
        return FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .build()
    }

    @Bean
    fun fcm(): FirebaseMessaging = FirebaseMessaging.getInstance(firebaseApp())
}
