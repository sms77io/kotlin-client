package com.seven

import io.ktor.client.*
import io.ktor.client.request.*
import kotlin.reflect.full.memberProperties

suspend fun validateForVoice(client: HttpClient, params: ValidateForVoiceParams): ValidateForVoiceResponse {
    return client.post {
        url(
            toQueryString(
                "validate_for_voice?",
                ValidateForVoiceParams::class.memberProperties,
                params
            )
        )
    }
}


data class ValidateForVoiceParams(
    val number: String,
    val callback: String?,
)

data class ValidateForVoiceResponse(
    val code: String?,
    val error: String?,
    val formatted_output: String?,
    val id: Int?,
    val sender: String?,
    val success: Boolean,
    val voice: Boolean?,
)
