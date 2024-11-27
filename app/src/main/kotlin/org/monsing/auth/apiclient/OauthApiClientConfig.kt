package org.monsing.auth.apiclient

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class OauthApiClientConfig {

    @Bean
    fun googleApiClient(): GoogleOauthApiClient {
        val restClient = RestClient.builder()
            .baseUrl("https://www.googleapis.com/oauth2/v2/userinfo")
            .build()

        val adaptor = RestClientAdapter.create(restClient)

        return HttpServiceProxyFactory.builderFor(adaptor).build()
            .createClient(GoogleOauthApiClient::class.java)
    }

    @Bean
    fun kakaoApiClient(): KakaoOauthApiClient {
        val restClient = RestClient.builder()
            .baseUrl("https://kapi.kakao.com/v2/user/me")
            .build()

        val adaptor = RestClientAdapter.create(restClient)

        return HttpServiceProxyFactory.builderFor(adaptor).build()
            .createClient(KakaoOauthApiClient::class.java)
    }
}
