package org.monsing.auth.apiclient

import org.monsing.auth.oauthhandler.OauthIdentifier
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange
interface KakaoOauthApiClient {

    @GetExchange
    fun getIdentifier(
        @RequestHeader("Authorization") token: String,
    ): OauthIdentifier
}
