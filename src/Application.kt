package com.sms77

import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import io.ktor.client.features.logging.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val client = getClient()

    runBlocking {
        /*
        val message = client.post<JsonSampleClass> {
            url("http://127.0.0.1:8080/path/to/endpoint")
            contentType(ContentType.Application.Json)
            body = JsonSampleClass(hello = "world")
        }
        */

        println("balance: ${balance(client)}")
    }
}

suspend fun balance(client: HttpClient): Float {
    return client.get<String> {
        url("https://gateway.sms77.io/api/balance")
    }.toFloat()
}

fun getClient(): HttpClient {
    return HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
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

data class JsonSampleClass(val hello: String)

