package org.monsing.alert

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Profile("dev")
@Service
class AlertService(
    private val fcm: FirebaseMessaging
) {

    fun send(token: String, alert: Alert) {
        val msg = Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(alert.title)
                    .setBody(alert.body)
                    .setImage(alert.image)
                    .build()
            )
            .setToken(token)
            .build()
        fcm.send(msg)
    }
}
