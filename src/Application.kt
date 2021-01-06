package com.sms77

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import io.ktor.client.features.logging.*
import io.ktor.client.utils.*
import io.ktor.features.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
        install(Logging) {
            level = LogLevel.HEADERS
            developmentMode = true
/*            buildHeaders {
                append("sentWith", "Kotlin")
                append("Authorization", "Basic ${System.getenv("SMS77_DUMMY_API_KEY")}")
            }*/
        }
/*        install(DefaultHeaders) {
            header("sentWith", "Kotlin")
            println("apiKey: ${System.getenv("SMS77_DUMMY_API_KEY")}")
            header("Authorization", "Basic ${System.getenv("SMS77_DUMMY_API_KEY")}")
        }*/
        defaultRequest {
            header("sentWith", "Kotlin")
            println("apiKey: ${System.getenv("SMS77_DUMMY_API_KEY")}")
            header("Authorization", "Basic ${System.getenv("SMS77_DUMMY_API_KEY")}")
        }
/*        buildHeaders {
            append("sentWith", "Kotlin")
            append("Authorization", "Basic ${System.getenv("SMS77_DUMMY_API_KEY")}")
        }*/
    }
    runBlocking {
        /*
        val message = client.post<JsonSampleClass> {
            url("http://127.0.0.1:8080/path/to/endpoint")
            contentType(ContentType.Application.Json)
            body = JsonSampleClass(hello = "world")
        }
        */

        val balance = client.get<String> {
            url("https://gateway.sms77.io/api/balance")
        }
        print("balance: $balance")
    }
}
class BalanceParams()
data class JsonSampleClass(val hello: String)

