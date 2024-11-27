package org.monsing.auth.oauthhandler

import org.monsing.member.OauthProviderType

interface OauthHandler {

    fun handle(code: String): OauthIdentifier

    fun canHandle(oauthProviderType: OauthProviderType): Boolean
}
