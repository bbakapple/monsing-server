package org.monsing.auth.oauth

import jakarta.annotation.PostConstruct
import org.monsing.auth.oauthhandler.OauthHandler
import org.monsing.auth.oauthhandler.OauthIdentifier
import org.monsing.member.OauthProviderType
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.stereotype.Component

@Component
class OauthAdaptor {

    private val handlers = mutableListOf<OauthHandler>()

    fun handle(oauthProviderType: OauthProviderType, oauthToken: String): OauthIdentifier {
        return handlers.firstOrNull { it.canHandle(oauthProviderType) }?.handle(oauthToken)
            ?: throw IllegalArgumentException("Unsupported oauth provider type: $oauthProviderType")
    }

    @PostConstruct
    fun init() {
        AnnotationConfigApplicationContext()
            .getBeansOfType(OauthHandler::class.java)
            .values.forEach { handlers.add(it) }
    }
}
