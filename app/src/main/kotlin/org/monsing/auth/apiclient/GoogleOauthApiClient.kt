package org.monsing.auth.apiclient

import org.monsing.auth.oauthhandler.OauthIdentifier
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange
interface GoogleOauthApiClient {

    @GetExchange("/oauth2/v2/userinfo")
    fun getIdentifier(
        @RequestParam(name = "access_token") token: String
    ): OauthIdentifier
}
