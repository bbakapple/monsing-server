package org.monsing.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.monsing.auth.jwt.TokenManager
import org.monsing.auth.jwt.TokenPayload

class TokenManagerTest : StringSpec({

    "토큰 페이로드 검증" {
        val key = "sfamamowevamov2mo2o42ov4momoomvm4osdmfkadll"
        val tokenManager = TokenManager(key, 3600L, key, 3600L, ObjectMapper())

        val token = tokenManager.createAccessToken(TokenPayload(1L))

        val payload = tokenManager.getPayLoad(token)

        payload.id shouldBe 1L
    }

    "액세스 키로 리프레시 토큰을 디코딩할 수 없음" {
        val key = "sfamamowevamov2mo2o42ov414momoomvm4osdmfkadllasdsad"
        val refresh = "sfamamowevamov2mo2o4223ov4momoomvm4osdmfkadjf2jll"
        val tokenManager = TokenManager(key, 3600L, refresh, 3600L, ObjectMapper())

        val token = tokenManager.createRefreshToken(1L)

        shouldThrow<IllegalArgumentException> {
            tokenManager.getPayLoad(token)
        }.message shouldBe "Invalid token"
    }

    "리프레시 키로 액세스 토큰을 디코딩할 수 없음" {
        val key = "sfamamowevamov2mo2o42ov414momoomvm4osdmfkadllasdsad"
        val refresh = "sfamamowevamov2mo2o4223ov4momoomvm4osdmfkadjf2jll"
        val tokenManager = TokenManager(key, 3600L, refresh, 3600L, ObjectMapper())

        val token = tokenManager.createAccessToken(TokenPayload(1L))

        shouldThrow<IllegalArgumentException> {
            tokenManager.getRefreshPayload(token)
        }.message shouldBe "Invalid token"
    }

    "만료된 토큰을 디코딩할 수 없음" {
        val key = "sfamamowevamov2mo2o42ov414momoomvm4osdmfkadllasdsad"
        val tokenManager = TokenManager(key, 1L, key, 3600L, ObjectMapper())

        val token = tokenManager.createAccessToken(TokenPayload(1L))

        Thread.sleep(1000)

        shouldThrow<ExpiredJwtException> {
            tokenManager.getPayLoad(token)
        }
    }
})
