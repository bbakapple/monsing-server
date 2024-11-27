package org.monsing.auth.oauthhandler

import org.monsing.auth.oauth.OauthApiClient
import org.monsing.member.OauthProviderType
import org.springframework.stereotype.Component

@Component
class GoogleOauthHandler(
    private val googleApiClient: OauthApiClient
) : OauthHandler {

    override fun canHandle(oauthProviderType: OauthProviderType): Boolean {
        return oauthProviderType == OauthProviderType.GOOGLE
    }

    override fun handle(code: String): OauthIdentifier {

        return googleApiClient.getGoogleOauthIdentifier(code)
    }
}
