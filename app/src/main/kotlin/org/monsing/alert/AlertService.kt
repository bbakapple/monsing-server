package org.monsing.alert

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service

@Service
class AlertService(
    private val fcm: FirebaseMessaging
) {

    fun send() {
        val msg = Message.builder()
            .setNotification(
                Notification.builder()
                    .build()
            )
            .setToken("to")
            .build()
        fcm.send(msg)
    }
}
