package org.monsing.auth.oauthhandler

import org.monsing.auth.oauth.OauthApiClient
import org.monsing.member.OauthProviderType
import org.springframework.stereotype.Component

@Component
class KakaoOauthHandler(
    private val kakaoApiClient: OauthApiClient
) : OauthHandler {

    override fun canHandle(oauthProviderType: OauthProviderType): Boolean {
        return oauthProviderType == OauthProviderType.KAKAO
    }

    override fun handle(code: String): OauthIdentifier {
        return kakaoApiClient.getKakaoIdentifier(code)
    }
}
