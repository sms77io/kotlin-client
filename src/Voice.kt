package com.seven

import io.ktor.client.*
import io.ktor.client.request.*
import kotlin.reflect.full.memberProperties

suspend fun voice(client: HttpClient, params: VoiceParams): VoiceResponse {
    return client.post {
        url(
            toQueryString(
                "voice?",
                VoiceParams::class.memberProperties,
                params
            )
        )
    }
}

data class VoiceParams(
    val from: String?,
    val text: String,
    val to: String,
    val xml: Boolean?,
)

data class VoiceMessage(
    val error: String?,
    val error_text: String?,
    val id: String?,
    val messages: List<String>?,
    val price: Float,
    val recipient: String,
    val sender: String,
    val success: Boolean,
    val text: String,
)

data class VoiceResponse(
    val balance: Float,
    val debug: Boolean,
    val messages: List<VoiceMessage>,
    val success: String,
    val total_price: Float,
)