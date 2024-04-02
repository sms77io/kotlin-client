package com.seven

import io.ktor.client.*
import io.ktor.client.request.*
import kotlin.reflect.full.memberProperties

class SmsResource(client: HttpClient) : Resource(client) {
    suspend fun dispatch(params: SmsParams): SmsResponse {
        return client.post {
            url(
                toQueryString(
                    "sms?json=1",
                    SmsParams::class.memberProperties,
                    params
                )
            )
        }
    }

    suspend fun status(params: StatusParams): String {
        return client.get {
            url(
                toQueryString(
                    "status?",
                    StatusParams::class.memberProperties,
                    params
                )
            )
        }
    }
}

data class SmsParams(var text: String, var to: String)  {
    var delay: String? = null
    var flash: Boolean? = null
    var foreign_id: String? = null
    var from: String? = null
    var label: String? = null
    var no_reload: Boolean? = null
    var unicode: Boolean? = null
    var udh: String? = null
    var utf8: Boolean? = null
    var ttl: Int? = null
    var performance_tracking: Boolean? = null
}

enum class MessageEncoding {
    gsm,
    ucs2
}

data class SmsMessage(
    val encoding: MessageEncoding,
    val error: String?,
    val error_text: String?,
    val id: String?,
    val messages: List<String>?,
    val parts: Int,
    val price: Float,
    val recipient: String,
    val sender: String,
    val success: Boolean,
    val text: String,
)

data class SmsResponse(
    val debug: StringBool,
    val balance: Float,
    val messages: List<SmsMessage>,
    val sms_type: SmsType,
    val success: String,
    val total_price: Float,
)

enum class SmsType {
    direct,
}

data class StatusParams(val msg_id: String)