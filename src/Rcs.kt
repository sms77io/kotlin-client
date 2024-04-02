package com.seven

import io.ktor.client.*
import io.ktor.client.request.*
import kotlin.reflect.full.memberProperties

class RcsResource(client: HttpClient) : Resource(client) {
    suspend fun delete(msg: RcsMessage): RcsDeleted {
        return delete(msg.id!!.toInt())
    }

    suspend fun delete(id: Int): RcsDeleted {
        return client.delete {
            url("rcs/messages/${id}")
        }
    }

    suspend fun dispatch(params: RcsParams): RcsResponse {
        return client.post {
            url(
                toQueryString(
                    "rcs/messages",
                    RcsParams::class.memberProperties,
                    params
                )
            )
        }
    }

    suspend fun events(params: EventsParams): RcsEventResponse {
        return client.post {
            url("${BASE_URL}/rcs/events")
            body = params
        }
    }
}

enum class Event {
    IS_TYPING,
    READ
}

data class EventsParams(
    val to: String,
    val msg_id: String,
    val event: Event
)

data class RcsParams(var to: String, var text: String)  {
    var delay: String? = null
    var foreign_id: String? = null
    var from: String? = null
    var label: String? = null
    var ttl: Int? = null
    var performance_tracking: Boolean? = null
}

data class RcsMessage(
    val channel: String,
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

data class RcsResponse(
    val debug: StringBool,
    val balance: Float,
    val messages: List<RcsMessage>,
    val sms_type: SmsType,
    val success: String,
    val total_price: Float,
)

data class RcsDeleted(
    val success: Boolean,
)

data class RcsEventResponse(
    val success: Boolean,
)