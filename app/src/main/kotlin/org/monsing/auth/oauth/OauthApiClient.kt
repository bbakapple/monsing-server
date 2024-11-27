package org.monsing.auth.oauth

import org.monsing.auth.oauthhandler.OauthIdentifier
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange
interface OauthApiClient {

    @GetExchange
    fun getKakaoIdentifier(
        @RequestHeader("Authorization") token: String,
    ): OauthIdentifier

    @GetExchange
    fun getGoogleOauthIdentifier(
        @RequestParam(name = "access_token") token: String
    ): OauthIdentifier
}
