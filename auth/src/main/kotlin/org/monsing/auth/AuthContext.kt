package org.monsing.auth

import org.monsing.auth.jwt.TokenPayload
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@RequestScope
@Component
class AuthContext {

    var payload: TokenPayload? = null
}
