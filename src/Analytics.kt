package com.seven

import io.ktor.client.*
import io.ktor.client.request.*
import kotlin.reflect.full.memberProperties

class AnalyticsResource(client: HttpClient) : Resource(client) {
    suspend fun byCountry(params: AnalyticsParams): List<AnalyticByCountry> {
        return client.get {
            url(
                toQueryString(
                    "analytics?group_by=${AnalyticsGroupBy.Country}",
                    AnalyticsParams::class.memberProperties,
                    params
                )
            )
        }
    }

    suspend fun byDate(params: AnalyticsParams): List<AnalyticByDate> {
        return client.get {
            url(
                toQueryString(
                    "analytics?group_by=${AnalyticsGroupBy.Date}",
                    AnalyticsParams::class.memberProperties,
                    params
                )
            )
        }
    }

    suspend fun byLabel(params: AnalyticsParams): List<AnalyticByLabel> {
        return client.get {
            url(
                toQueryString(
                    "analytics?group_by=${AnalyticsGroupBy.Label}",
                    AnalyticsParams::class.memberProperties,
                    params
                )
            )
        }
    }

    suspend fun bySubaccount(params: AnalyticsParams): List<AnalyticBySubaccount> {
        return client.get {
            url(
                toQueryString(
                    "analytics?group_by=${AnalyticsGroupBy.Subaccount}",
                    AnalyticsParams::class.memberProperties,
                    params
                )
            )
        }
    }
}

object AnalyticsGroupBy {
    const val Date = "date"
    const val Label = "label"
    const val Subaccount = "subaccount"
    const val Country = "country"
}

interface AnalyticBase {
    val direct: Int
    val economy: Int
    val hlr: Int
    val inbound: Int
    val mnp: Int
    val usage_eur: Float
    val voice: Int
}

data class AnalyticByCountry(
    override val direct: Int,
    override val economy: Int,
    override val hlr: Int,
    override val inbound: Int,
    override val mnp: Int,
    override val usage_eur: Float,
    override val voice: Int,
    val country: String,
) : AnalyticBase

data class AnalyticByDate(
    override val direct: Int,
    override val economy: Int,
    override val hlr: Int,
    override val inbound: Int,
    override val mnp: Int,
    override val usage_eur: Float,
    override val voice: Int,
    val date: String,
) : AnalyticBase

data class AnalyticBySubaccount(
    override val direct: Int,
    override val economy: Int,
    override val hlr: Int,
    override val inbound: Int,
    override val mnp: Int,
    override val usage_eur: Float,
    override val voice: Int,
    val account: String,
) : AnalyticBase

data class AnalyticByLabel(
    override val direct: Int,
    override val economy: Int,
    override val hlr: Int,
    override val inbound: Int,
    override val mnp: Int,
    override val usage_eur: Float,
    override val voice: Int,
    val label: String,
) : AnalyticBase

data class AnalyticsParams(
    val start: String?,
    val end: String?,
    val label: String?,
    val subaccounts: String?,
)