package com.seven

import io.ktor.client.*
import io.ktor.client.request.*
import kotlin.reflect.full.memberProperties

class JournalResource(client: HttpClient) : Resource(client) {
    suspend fun inbound(params: JournalParams): List<JournalInbound> {
        return client.get {
            url(
                toQueryString(
                    "journal?type=${JournalType.Inbound}",
                    JournalParams::class.memberProperties,
                    params
                )
            )
        }
    }

    suspend fun outbound(params: JournalParams): List<JournalOutbound> {
        return client.get {
            url(
                toQueryString(
                    "journal?type=${JournalType.Outbound}",
                    JournalParams::class.memberProperties,
                    params
                )
            )
        }
    }

    suspend fun replies(params: JournalParams): List<JournalReplies> {
        return client.get {
            url(
                toQueryString(
                    "journal?type=${JournalType.Replies}",
                    JournalParams::class.memberProperties,
                    params
                )
            )
        }
    }

    suspend fun voice(params: JournalParams): List<JournalVoice> {
        return client.get {
            url(
                toQueryString(
                    "journal?type=${JournalType.Voice}",
                    JournalParams::class.memberProperties,
                    params
                )
            )
        }
    }
}


interface JournalBase {
    val from: String
    val id: String
    val price: String
    val text: String
    val timestamp: String
    val to: String
}

object JournalType {
    const val Outbound = "outbound"
    const val Inbound = "inbound"
    const val Voice = "voice"
    const val Replies = "replies"
}

data class JournalParams(
    val date_from: String?,
    val date_to: String?,
    val id: Int?,
    val state: String?,
    val to: String?,
    val limit: Int?,
)

data class JournalOutbound(
    override val from: String,
    override val id: String,
    override val price: String,
    override val text: String,
    override val timestamp: String,
    override val to: String,
    val connection: String,
    val dlr: String?,
    val dlr_timestamp: String?,
    val foreign_id: String?,
    val label: String?,
    val latency: String?,
    val mccmnc: String?,
    val type: String?,
) : JournalBase

data class JournalVoice(
    override val from: String,
    override val id: String,
    override val price: String,
    override val text: String,
    override val timestamp: String,
    override val to: String,
    val duration: String,
    val error: String,
    val status: String,
    val xml: Boolean,
) : JournalBase

data class JournalInbound(
    override val from: String,
    override val id: String,
    override val price: String,
    override val text: String,
    override val timestamp: String,
    override val to: String,
) : JournalBase

data class JournalReplies(
    override val from: String,
    override val id: String,
    override val price: String,
    override val text: String,
    override val timestamp: String,
    override val to: String,
) : JournalBase