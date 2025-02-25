package org.monsing.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.monsing.auth.jwt.AuthTokenManager
import org.monsing.auth.jwt.AuthTokenPayload

class TokenManagerTest : StringSpec({

    "토큰 페이로드 검증" {
        val key = "sfamamowevamov2mo2o42ov4momoomvm4osdmfkadll"
        val authTokenManager = AuthTokenManager(key, 3600L, key, 3600L, ObjectMapper())

        val token = authTokenManager.createAccessToken(AuthTokenPayload(1L))

        val payload = authTokenManager.getPayLoad(token)

        payload.id shouldBe 1L
    }

    "액세스 키로 리프레시 토큰을 디코딩할 수 없음" {
        val key = "sfamamowevamov2mo2o42ov414momoomvm4osdmfkadllasdsad"
        val refresh = "sfamamowevamov2mo2o4223ov4momoomvm4osdmfkadjf2jll"
        val authTokenManager = AuthTokenManager(key, 3600L, refresh, 3600L, ObjectMapper())

        val token = authTokenManager.createRefreshToken(1L)

        shouldThrow<IllegalArgumentException> {
            authTokenManager.getPayLoad(token)
        }
    }

    "리프레시 키로 액세스 토큰을 디코딩할 수 없음" {
        val key = "sfamamowevamov2mo2o42ov414momoomvm4osdmfkadllasdsad"
        val refresh = "sfamamowevamov2mo2o4223ov4momoomvm4osdmfkadjf2jll"
        val authTokenManager = AuthTokenManager(key, 3600L, refresh, 3600L, ObjectMapper())

        val token = authTokenManager.createAccessToken(AuthTokenPayload(1L))

        shouldThrow<IllegalArgumentException> {
            authTokenManager.getRefreshPayload(token)
        }.message shouldBe "Invalid token"
    }

    "만료된 토큰을 디코딩할 수 없음" {
        val key = "sfamamowevamov2mo2o42ov414momoomvm4osdmfkadllasdsad"
        val authTokenManager = AuthTokenManager(key, 1L, key, 3600L, ObjectMapper())

        val token = authTokenManager.createAccessToken(AuthTokenPayload(1L))

        Thread.sleep(1000)

        shouldThrow<ExpiredJwtException> {
            authTokenManager.getPayLoad(token)
        }
    }
})
