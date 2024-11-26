package org.monsing.auth.oauthhandler

interface OauthHandler {

    fun handle(code: String): OauthIdentifier
}
