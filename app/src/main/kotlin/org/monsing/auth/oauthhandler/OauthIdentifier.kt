package org.monsing.auth.oauthhandler

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

class OauthIdentifier @JsonCreator constructor(
    @JsonProperty("id")
    val id: String
)
