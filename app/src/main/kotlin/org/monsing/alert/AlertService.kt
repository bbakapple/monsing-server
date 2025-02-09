package org.monsing.alert

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Profile("dev")
@Service
class AlertService(
    private val fcm: FirebaseMessaging
) {

    fun send(alert: Alert, vararg tokens: String) {
        val messages = MulticastMessage.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(alert.title)
                    .setBody(alert.body)
                    .setImage(alert.image)
                    .build()
            )
            .addAllTokens(tokens.toList())
            .build()

        fcm.sendEachForMulticast(messages)
    }
}
