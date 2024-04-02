package com.seven

import io.ktor.client.*
import io.ktor.client.request.*
import kotlin.reflect.full.memberProperties

suspend fun pricing(client: HttpClient, params: PricingParams): PricingResponse {
    return client.get {
        url(
            toQueryString(
                "pricing?",
                PricingParams::class.memberProperties,
                params
            )
        )
    }
}


data class PricingCountry(
    val countryCode: String,
    val countryName: String,
    val countryPrefix: String,
    val networks: List<PricingCountryNetwork>,
)

data class PricingCountryNetwork(
    val comment: String,
    val features: List<String>,
    val mcc: String,
    val mncs: List<String>,
    val networkName: String,
    val price: Float,
)

data class PricingParams(val country: String?)

data class PricingResponse(
    val countCountries: Int,
    val countNetworks: Int,
    val countries: List<PricingCountry>,
)