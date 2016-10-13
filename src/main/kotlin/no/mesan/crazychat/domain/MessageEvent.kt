package no.mesan.crazychat.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class MessageEvent(
        @JsonProperty("username") val username: String,
        @JsonProperty("returnAddress") val returnAddress: String,
        @JsonProperty("message") val message: String,
        val toAddress: String? = null)
