package org.monsing.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TokenManagerTest : StringSpec({

    "토큰 페이로드 검증" {
        val key = "sfamamowevamov2mo2o42ov4momoomvm4osdmfkadll"
        val tokenManager = TokenManager(key, 3600L, key, 3600L, ObjectMapper())

        val token = tokenManager.createAccessToken(TokenPayload(1L))

        val payload = tokenManager.getPayLoad(token)

        payload.id shouldBe 1L
    }
})
