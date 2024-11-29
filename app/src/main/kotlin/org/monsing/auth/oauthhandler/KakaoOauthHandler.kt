package org.monsing.auth.oauthhandler

import org.monsing.auth.apiclient.KakaoOauthApiClient
import org.monsing.member.OauthProviderType
import org.springframework.stereotype.Component

@Component
class KakaoOauthHandler(
    private val kakaoApiClient: KakaoOauthApiClient
) : OauthHandler {

    override fun canHandle(oauthProviderType: OauthProviderType): Boolean {
        return oauthProviderType == OauthProviderType.KAKAO
    }

    override fun handle(code: String): OauthIdentifier {
        return kakaoApiClient.getIdentifier(code)
    }
}
