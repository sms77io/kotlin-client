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
    }
}

suspend fun createContactCsv(client: HttpClient): String {
    return client.get {
        url("https://gateway.sms77.io/api/contacts?action=write")
    }
}

suspend fun createContactJson(client: HttpClient): CreateContactResponse {
    return client.get {
        url("https://gateway.sms77.io/api/contacts?action=write&json=1")
    }
}

suspend fun editContactCsv(client: HttpClient, params: EditContactParams): String {
    var url = "https://gateway.sms77.io/api/contacts?action=write"

    for (prop in EditContactParams::class.memberProperties) {
        val value = prop.get(params)

        if (null != value && false != value) {
            url += "&${prop.name}=${value}"
        }
    }

    return client.get {
        url(url)
    }
}

suspend fun editContactJson(client: HttpClient, params: EditContactParams): EditContactResponse {
    var url = "https://gateway.sms77.io/api/contacts?action=write&json=1"

    for (prop in EditContactParams::class.memberProperties) {
        val value = prop.get(params)

        if (null != value && false != value) {
            url += "&${prop.name}=${value}"
        }
    }

    return client.get {
        url(url)
    }
}

suspend fun deleteContactCsv(client: HttpClient, params: DeleteContactParams): String {
    return client.get {
        url("https://gateway.sms77.io/api/contacts?action=del&id=${params.id}")
    }
}

suspend fun deleteContactJson(client: HttpClient, params: DeleteContactParams): DeleteContactResponse {
    return client.get {
        url("https://gateway.sms77.io/api/contacts?action=del&json=1&id=${params.id}")
    }
}

suspend fun getBalance(client: HttpClient): Float {
    return client.get<String> {
        url("https://gateway.sms77.io/api/balance")
    }.toFloat()
}

suspend fun getContactsCsv(client: HttpClient): String {
    return client.get {
        url("https://gateway.sms77.io/api/contacts?action=read")
    }
}

suspend fun getContactsJson(client: HttpClient): List<Contact> {
    return client.get {
        url("https://gateway.sms77.io/api/contacts?action=read&json=1")
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

data class CreateContactResponse(val `return`: String, val id: String)
data class DeleteContactParams(val id: Number)
data class DeleteContactResponse(val `return`: String)
data class EditContactParams(val id: Number, val email: String?, val empfaenger: String?, val nick: String?)
data class EditContactResponse(val `return`: String)
data class Contact(val ID: String, val Name: String, val Number: String)

