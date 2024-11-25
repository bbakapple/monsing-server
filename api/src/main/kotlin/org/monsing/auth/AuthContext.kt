package org.monsing.auth

import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@RequestScope
@Component
class AuthContext {

    var payload: String? = null
}
