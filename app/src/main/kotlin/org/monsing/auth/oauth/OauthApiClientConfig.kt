package org.monsing.auth.oauth

import org.monsing.auth.oauthhandler.OauthHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class OauthApiClientConfig(
    private val googleOauthHandler: OauthHandler,
    private val kakaoOauthHandler: OauthHandler
) {

    @Bean("googleApiClient")
    fun googleApiClient(): OauthApiClient {
        val restClient = RestClient.builder()
            .baseUrl("https://www.googleapis.com/oauth2/v2/userinfo")
            .build()

        val adaptor = RestClientAdapter.create(restClient)

        return HttpServiceProxyFactory.builderFor(adaptor).build()
            .createClient(OauthApiClient::class.java)
    }

    @Bean("kakaoApiClient")
    fun kakaoApiClient(): OauthApiClient {
        val restClient = RestClient.builder()
            .baseUrl("https://kapi.kakao.com/v2/user/me")
            .build()

        val adaptor = RestClientAdapter.create(restClient)

        return HttpServiceProxyFactory.builderFor(adaptor).build()
            .createClient(OauthApiClient::class.java)
    }

    @Bean
    fun oauthAdaptor(): OauthAdaptor {
        return OauthAdaptor().apply {
            addHandler(googleOauthHandler)
            addHandler(kakaoOauthHandler)
        }
    }
}
