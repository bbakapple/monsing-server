package org.monsing.api

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class CreateChatRequest @JsonCreator constructor(

    @JsonProperty
    val memberId: Long
)
