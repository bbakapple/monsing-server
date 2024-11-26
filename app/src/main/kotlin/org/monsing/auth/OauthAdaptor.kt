package org.monsing.auth

import org.monsing.auth.oauthhandler.GoogleOauthHandler
import org.monsing.auth.oauthhandler.KakaoOauthHandler
import org.monsing.auth.oauthhandler.NaverOauthHandler
import org.monsing.auth.oauthhandler.OauthInfo
import org.monsing.member.OauthProviderType
import org.springframework.stereotype.Component

@Component
class OauthAdaptor {

    companion object {
        private val oauthHandlers = mapOf(
            OauthProviderType.GOOGLE to GoogleOauthHandler(),
            OauthProviderType.NAVER to NaverOauthHandler(),
            OauthProviderType.KAKAO to KakaoOauthHandler()
        )
    }

    fun handle(oauthProviderType: OauthProviderType, oauthToken: String): OauthInfo {
        return oauthHandlers[oauthProviderType]?.handle(oauthToken)
            ?: throw IllegalArgumentException("Unsupported oauth provider type: $oauthProviderType")
    }
}
