package com.seven

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.reflect.KProperty1

const val BASE_URL = "https://gateway.seven.io/api/"

abstract class Resource(protected val client: HttpClient)


fun getClient(params: ClientParams): HttpClient {
    return HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
            acceptContentTypes = acceptContentTypes +
                    ContentType("text", "plain")
        }

        if (params.debug) {
            install(Logging) {
                level = LogLevel.INFO
            }
        }

        defaultRequest {
            developmentMode = params.debug
            header("Accept", "application/json")
            header("sentWith", params.sentWith)
            header("Authorization", "Basic ${params.apiKey}") // TODO
        }
    }
}

inline fun <reified T> toQueryString(endpoint: String, props: Collection<KProperty1<T, *>>, params: T): String {
    var url = "${BASE_URL}${endpoint}"

    for (prop in props) {
        val value = prop.get(params)

        if (null != value && false != value) {
            url += "&${prop.name}=${value}"
        }
    }

    return url
}

data class PagingParams(
    val order_by: String? = null,
    val order_direction: String? = null,
    val search: String? = null,
    val offset: Int? = null,
    val limit: Int? = null,
    val group_id: Int? = null,
)

data class PagingMetadata(
    val offset: Int,
    val count: Int,
    val total: Int,
    val has_more: Boolean
)

data class Paginated<T>(
    val data: List<T>,
    val pagingMetadata: PagingMetadata
)

data class ClientParams(
    val apiKey: String = System.getenv("SEVEN_API_KEY"),
    val debug: Boolean = false,
    val dummy: Boolean = false,
    val sentWith: String = "Kotlin",
    val testing: Boolean = false,
)

enum class StringBool {
    `true`,
    `false`
}