package com.seven

import io.ktor.client.*
import io.ktor.client.request.*
import kotlin.reflect.full.memberProperties

class HooksResource(client: HttpClient) : Resource(client) {
    suspend fun get(): GetHooksResponse {
        return client.get {
            url("${BASE_URL}hooks?action=${HooksAction.Read}")
        }
    }

    suspend fun subscribe(params: SubscribeHookParams): SubscribeHookResponse {
        return client.post {
            url(
                toQueryString(
                    "hooks?action=${HooksAction.Subscribe}",
                    SubscribeHookParams::class.memberProperties,
                    params
                )
            )
        }
    }

    suspend fun unsubscribe(params: UnsubscribeHookParams): UnsubscribeHookResponse {
        return client.post {
            url("${BASE_URL}hooks?action=${HooksAction.Unsubscribe}&id=${params.id}")
        }
    }
}

data class GetHooksResponse(val success: Boolean, val hooks: List<Hook>)


data class Hook(
    val created: String,
    val event_type: String,
    val id: String,
    val request_method: String,
    val target_url: String,
)

abstract class HookEventType(val value: String)
class HookEventTypeSmsInbound : HookEventType("sms_mo")
class HookEventTypeSmsStatus : HookEventType("dlr")
class HookEventTypeVoiceStatus : HookEventType("voice_status")

abstract class HookRequestMethod(val value: String)
class HookRequestMethodGet : HookRequestMethod("GET")
class HookRequestMethodPost : HookRequestMethod("POST")

object HooksAction {
    const val Read = "read"
    const val Subscribe = "subscribe"
    const val Unsubscribe = "unsubscribe"
}

class SubscribeHookParams(
    event_type: HookEventType,
    val target_url: String,
    request_method: HookRequestMethod?,
) {
    val event_type: String = event_type.value
    val request_method: String? = request_method?.value
}

data class SubscribeHookResponse(val success: Boolean, val id: Int?)

data class UnsubscribeHookParams(val id: Int)

data class UnsubscribeHookResponse(val success: Boolean)