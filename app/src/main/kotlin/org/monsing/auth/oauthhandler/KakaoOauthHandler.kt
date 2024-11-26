package org.monsing.auth.oauthhandler

import com.fasterxml.jackson.databind.ObjectMapper
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


private const val KAKAO_OAUTH_URL = "https://kapi.kakao.com/v2/user/me"

class KakaoOauthHandler : OauthHandler {

    override fun handle(code: String): OauthIdentifier {
        val request = HttpRequest.newBuilder()
            .GET()
            .uri(URI(KAKAO_OAUTH_URL))
            .headers("Authorization", "Bearer $code")
            .build()

        val response = HttpClient.newBuilder()
            .build()
            .send(request, HttpResponse.BodyHandlers.ofString())
            .body()

        return ObjectMapper().readTree(response)
            .get("id").textValue()
            ?.let { return OauthIdentifier(it) }
            ?: throw IllegalArgumentException("Failed to get user id from kakao oauth response")
    }
}
