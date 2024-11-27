package org.monsing.auth

import org.monsing.auth.oauthhandler.OauthHandler
import org.monsing.auth.oauthhandler.OauthIdentifier
import org.monsing.member.OauthProviderType
import org.springframework.stereotype.Component

@Component
class OauthAdaptor {

    private val handlers = mutableListOf<OauthHandler>()

    fun handle(oauthProviderType: OauthProviderType, oauthToken: String): OauthIdentifier {
        return handlers.firstOrNull() { it.canHandle(oauthProviderType) }
            ?.handle(oauthToken)
            ?: throw IllegalArgumentException("Unsupported oauth provider type: $oauthProviderType")
    }
}
