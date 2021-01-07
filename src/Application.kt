package com.sms77

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import io.ktor.client.features.logging.*
import io.ktor.http.*
import java.time.Instant
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val client = getClient()

    runBlocking {
        //println("balance: ${balance(client)}")
        //println(getContactsCsv(client))
        //println(getContactsJson(client))

        println(
            subscribeHook(
                client, SubscribeHookParams(
                    event_type = HookEventType.SmsInbound,
                    request_method = HookRequestMethod.Get,
                    target_url = "http://my.tld/kotlin/${DateTimeFormatter.ISO_INSTANT.format(Instant.now())}"
                )
            )
        )
    }
}

suspend fun createContactCsv(client: HttpClient): String {
    return client.get {
        url("https://gateway.sms77.io/api/contacts?action=${ContactsAction.Write}")
    }
}

suspend fun createContactJson(client: HttpClient): CreateContactResponse {
    return client.get {
        url("https://gateway.sms77.io/api/contacts?action=${ContactsAction.Write}&json=1")
    }
}

suspend fun editContactCsv(client: HttpClient, params: EditContactParams): String {
    return client.get {
        url(
            toQueryString(
                "contacts?action=${ContactsAction.Write}",
                EditContactParams::class.memberProperties,
                params
            )
        )
    }
}

suspend fun editContactJson(client: HttpClient, params: EditContactParams): EditContactResponse {
    return client.get {
        url(
            toQueryString(
                "contacts?action=${ContactsAction.Write}&json=1",
                EditContactParams::class.memberProperties,
                params
            )
        )
    }
}

suspend fun deleteContactCsv(client: HttpClient, params: DeleteContactParams): String {
    return client.get {
        url("https://gateway.sms77.io/api/contacts?action=${ContactsAction.Delete}&id=${params.id}")
    }
}

suspend fun deleteContactJson(client: HttpClient, params: DeleteContactParams): DeleteContactResponse {
    return client.get {
        url("https://gateway.sms77.io/api/contacts?action=${ContactsAction.Delete}&json=1&id=${params.id}")
    }
}

suspend fun getBalance(client: HttpClient): Float {
    return client.get<String> {
        url("https://gateway.sms77.io/api/balance")
    }.toFloat()
}

suspend fun getContactsCsv(client: HttpClient): String {
    return client.get {
        url("https://gateway.sms77.io/api/contacts?action=${ContactsAction.Read}")
    }
}

suspend fun getContactsJson(client: HttpClient): List<Contact> {
    return client.get {
        url("https://gateway.sms77.io/api/contacts?action=${ContactsAction.Read}&json=1")
    }
}

suspend fun getHooks(client: HttpClient): List<Hook> {
    return client.get {
        url("https://gateway.sms77.io/api/hooks?action=${HooksAction.Read}")
    }
}

suspend fun subscribeHook(client: HttpClient, params: SubscribeHookParams): SubscribeHookResponse {
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

suspend fun unsubscribeHook(client: HttpClient, params: UnsubscribeHookParams): UnsubscribeHookResponse {
    return client.post {
        url("https://gateway.sms77.io/api/hooks?action=${HooksAction.Unsubscribe}&id=${params.id}")
    }
}

fun getClient(): HttpClient {
    return HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
            acceptContentTypes = acceptContentTypes +
                    ContentType("text", "plain")
        }
        install(Logging) {
            level = LogLevel.HEADERS
        }
        defaultRequest {
            developmentMode = true
            header("sentWith", "Kotlin")
            header("Authorization", "Basic ${System.getenv("SMS77_DUMMY_API_KEY")}")
        }
    }
}

private inline fun <reified T> toQueryString(endpoint: String, props: Collection<KProperty1<T, *>>, params: T): String {
    var url = "https://gateway.sms77.io/api/${endpoint}"

    for (prop in props) {
        val value = prop.get(params)

        if (null != value && false != value) {
            url += "&${prop.name}=${value}"
        }
    }

    return url
}

data class Contact(val ID: String, val Name: String, val Number: String)
object ContactsAction {
    const val Delete = "del"
    const val Read = "read"
    const val Write = "write"
}

data class CreateContactResponse(val `return`: String, val id: String)
data class DeleteContactParams(val id: Number)
data class DeleteContactResponse(val `return`: String)
data class EditContactParams(val id: Number, val email: String?, val empfaenger: String?, val nick: String?)
data class EditContactResponse(val `return`: String)
data class Hook(
    val created: String,
    val event_type: HookEventType,
    val id: String,
    val request_method: HookRequestMethod,
    val target_url: String,
)

enum class HookEventType(val value: String) {
    SmsInbound("sms_mo"),
    SmsStatus("dlr"),
    VoiceStatus("voice_status")
}

enum class HookRequestMethod(val value: String) {
    Get("GET"),
    Post("POST"),
}

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

data class SubscribeHookResponse(val success: Boolean, val id: Number?)
data class UnsubscribeHookParams(val id: Number)
data class UnsubscribeHookResponse(val success: Boolean)