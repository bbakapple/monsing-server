package org.monsing.alert

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AlertConfig {

    @Bean
    fun firebaseApp(): FirebaseApp = FirebaseApp.initializeApp(firebaseOptions())

    @Bean
    fun firebaseOptions(): FirebaseOptions {
        val stream = ClassLoader.getSystemResourceAsStream("firebase/firebase.json")
        return FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(stream))
            .build()
    }

    @Bean
    fun fcm(): FirebaseMessaging = FirebaseMessaging.getInstance(firebaseApp())
}
