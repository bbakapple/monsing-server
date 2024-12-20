package org.monsing.monitor

import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import jakarta.annotation.PostConstruct
import java.net.InetAddress
import org.monsing.chat.session.LocalSessionStorage
import org.springframework.stereotype.Component

@Component
class WebSocketMetrics(
    private val localSessionStorage: LocalSessionStorage,
    private val meterRegistry: MeterRegistry
) {

    @PostConstruct
    fun registerMetrics() {
        Gauge.builder("websocket.sessions") { localSessionStorage.countSessions().toDouble() }
            .description("The number of websocket sessions")
            .strongReference(true)
            .tag("instance", InetAddress.getLocalHost().hostName)
            .register(meterRegistry)
    }
}
