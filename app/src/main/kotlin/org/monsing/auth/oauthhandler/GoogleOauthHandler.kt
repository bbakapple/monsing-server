package org.monsing.auth.oauthhandler

import com.fasterxml.jackson.databind.ObjectMapper
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

private const val GOOGLE_OAUTH_URL = "https://www.googleapis.com/oauth2/v2/userinfo"

class GoogleOauthHandler : OauthHandler {

    override fun handle(code: String): OauthIdentifier {
        val request = HttpRequest.newBuilder()
            .GET()
            .uri(URI("$GOOGLE_OAUTH_URL?oauth_token=$code"))
            .build()

        val response = HttpClient.newBuilder()
            .build()
            .send(request, HttpResponse.BodyHandlers.ofString())
            .body()

        return ObjectMapper().readTree(response)
            .get("id").textValue()
            ?.let { OauthIdentifier(it) }
            ?: throw IllegalArgumentException("Failed to get user id from google oauth response")
    }

}
