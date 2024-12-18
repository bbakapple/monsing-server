package org.monsing.util

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.monsing.member.OauthProviderType

class EnumExtensionsTest : FreeSpec({
    "enumValueOrNull" - {
        "enum의 name에 맞는 enum을 반환한다." {
            val enum = enumValueOrNull<OauthProviderType>("GOOGLE")

            enum shouldBe OauthProviderType.GOOGLE
        }
        "enum의 name이 null이면 null을 반환한다." {
            val enum = enumValueOrNull<OauthProviderType>("jude")

            enum shouldBe null
        }
    }
})
