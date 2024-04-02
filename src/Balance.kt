package com.seven

import io.ktor.client.*
import io.ktor.client.request.*

suspend fun balance(client: HttpClient): Balance {
    return client.get<Balance> {
        url("${BASE_URL}balance")
    }
}

data class Balance(
    val amount: Float,
    val currency: String,
)