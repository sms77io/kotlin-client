package com.seven

import io.ktor.client.*
import io.ktor.client.request.*
import kotlin.reflect.full.memberProperties

class NumbersResource(client: HttpClient) : Resource(client) {
    suspend fun available(params: AvailableParams): AvailableNumbers {
        return client.get {
            url(toQueryString("numbers/available", AvailableParams::class.memberProperties, params))
        }
    }

    suspend fun delete(phoneNumber: String, deleteImmediately: Boolean = false): DeleteResponse {
        return client.delete {
            url("${BASE_URL}numbers/active/${phoneNumber}?deleteImmediately=${deleteImmediately}")
        }
    }

    suspend fun order(params: OrderParams): OrderResponse {
        return client.post {
            url("${BASE_URL}/numbers/order")
            body = params
        }
    }

    suspend fun active(): List<Number> {
        return client.get {
            url("${BASE_URL}/numbers/active")
        }
    }

    suspend fun one(phoneNumber: String): Number {
        return client.get {
            url("${BASE_URL}/numbers/active/${phoneNumber}")
        }
    }

    suspend fun update(number: Number): Number {
        return client.patch {
            url("${BASE_URL}/numbers/active/${number.number}")
            body = number
        }
    }
}

enum class PaymentInterval {
    annnually,
    monthly
}

data class OrderResponse(
    val error: String?,
    val success: Boolean,
)

data class DeleteResponse(
    val message: String?,
    val success: Boolean,
)

data class OrderParams(
    val number: String,
    val payment_interval: PaymentInterval = PaymentInterval.annnually,
)

data class AvailableParams(
    val country: String? = null,
    val features_sms: Boolean? = null,
    val features_a2p_sms: Boolean?  = null,
    val features_voice: Boolean? = null,
)

data class Number(
    val country: String,
    val created: String? = null,
    val features: Features,
    var friendly_name: String,
    val fees: Fees,
    val number: String,
    val number_parsed: String,
)

data class AvailableNumbers(
    val availableNumbers: Array<Number>
)

data class Fees(
    val annually: Fee,
    val monthly: Fee,
    val sms_mo: Float,
    var voice_mo: Float,
)

data class Fee(
    val basic_charge: Float,
    var setup: Float,
)

data class Features(
    var a2p_sms: Boolean,
    val sms: Boolean,
    val voice: Boolean,
)