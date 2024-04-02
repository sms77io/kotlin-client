package com.seven

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

const val BASE_URL = "https://gateway.seven.io/api/"

abstract class Resource(protected val client: HttpClient)

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

suspend fun balance(client: HttpClient): Balance {
    return client.get<Balance> {
        url("${BASE_URL}balance")
    }
}

class ContactsResource(client: HttpClient) : Resource(client) {
    suspend fun create(contact: Contact): Contact {
        return client.post {
            url("${BASE_URL}contacts")
            body = contact
        }
    }

    suspend fun delete(id: Int): Void {
        return client.delete {
            url("${BASE_URL}contacts/${id}")
        }
    }

    suspend fun all(paging: PagingParams): Paginated<Contact> {
        return client.get {
            url(toQueryString(
                "contacts?",
                PagingParams::class.memberProperties,
                paging
            ))
        }
    }

    suspend fun one(id: Int): Contact {
        return client.get {
            url("contacts/${id}")
        }
    }

    suspend fun update(contact: Contact): Contact {
        return client.patch {
            url("contacts/${contact.id}")
            body = contact
        }
    }
}

class HooksResource(client: HttpClient) : Resource(client) {
    suspend fun get(): GetHooksResponse {
        return client.get {
            url("${BASE_URL}hooks?action=${HooksAction.Read}")
        }
    }

    suspend fun subscribe(params: SubscribeHookParams): SubscribeHookResponse {
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

    suspend fun unsubscribe(params: UnsubscribeHookParams): UnsubscribeHookResponse {
        return client.post {
            url("${BASE_URL}hooks?action=${HooksAction.Unsubscribe}&id=${params.id}")
        }
    }
}

class LookupResource(client: HttpClient) : Resource(client) {
    suspend fun cnam(params: LookupParams): LookupCnamResponse {
        return client.get {
            url(
                toQueryString(
                    "lookup?type=${LookupType.CallerNameDelivery}",
                    LookupParams::class.memberProperties,
                    params
                )
            )
        }
    }

    suspend fun format(params: LookupParams): LookupFormatResponse {
        return client.get {
            url(
                toQueryString(
                    "lookup?type=${LookupType.Format}",
                    LookupParams::class.memberProperties,
                    params
                )
            )
        }
    }

    suspend fun hlr(params: LookupParams): LookupHlrResponse {
        return client.get {
            url(
                toQueryString(
                    "lookup?type=${LookupType.HomeLocationRegister}",
                    LookupParams::class.memberProperties,
                    params
                )
            )
        }
    }

    suspend fun mnp(params: LookupParams): LookupMnpResponse {
        return client.get {
            url(
                toQueryString(
                    "lookup?type=${LookupType.MobileNumberPortability}&json=1",
                    LookupParams::class.memberProperties,
                    params
                )
            )
        }
    }
}

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

class SmsResource(client: HttpClient) : Resource(client) {
    suspend fun dispatch(params: SmsParams): SmsResponse {
        return client.post {
            url(
                toQueryString(
                    "sms?json=1",
                    SmsParams::class.memberProperties,
                    params
                )
            )
        }
    }

    suspend fun status(params: StatusParams): String {
        return client.get {
            url(
                toQueryString(
                    "status?",
                    StatusParams::class.memberProperties,
                    params
                )
            )
        }
    }
}

suspend fun validateForVoice(client: HttpClient, params: ValidateForVoiceParams): ValidateForVoiceResponse {
    return client.post {
        url(
            toQueryString(
                "validate_for_voice?",
                ValidateForVoiceParams::class.memberProperties,
                params
            )
        )
    }
}

suspend fun voice(client: HttpClient, params: VoiceParams): VoiceResponse {
    return client.post {
        url(
            toQueryString(
                "voice?",
                VoiceParams::class.memberProperties,
                params
            )
        )
    }
}

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

private inline fun <reified T> toQueryString(endpoint: String, props: Collection<KProperty1<T, *>>, params: T): String {
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

data class Contact(
    val id: Int? = null,
    val avatar: String? = null,
    val created: String? = null,
    val groups: List<Int> = listOf(),
    val initials: Initials = Initials(),
    val validation: Validation = Validation(),
    val properties: Properties  = Properties(),
)

data class Validation(
    val state: String? = null,
    val timestamp: String? = null,
)

data class Initials(
    val color: String? = null,
    val initials: String? = null,
)

data class Properties(
    var firstname: String? = null,
    var lastname: String? = null,
    var mobile_number: String? = null,
    var home_number: String? = null,
    var email: String? = null,
    var address: String? = null,
    var postal_code: String? = null,
    var city: String? = null,
    var birthday: String? = null,
    var notes: String? = null,
)

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

data class GetHooksResponse(val success: Boolean, val hooks: List<Hook>)

object LookupType {
    const val CallerNameDelivery = "cnam"
    const val Format = "format"
    const val HomeLocationRegister = "hlr"
    const val MobileNumberPortability = "mnp"
}

data class LookupParams(val number: String)

data class LookupCnamResponse(
    val code: String,
    val name: String, // callerID
    val number: String,
    val success: String,
)

data class LookupFormatResponse(
    val national: String,
    val carrier: String,
    val country_code: String,
    val country_iso: String,
    val country_name: String,
    val international: String,
    val international_formatted: String,
    val network_type: LookupNetworkType,
    val success: Boolean,
)

data class LookupMnp(
    val country: String,
    val international_formatted: String,
    val isPorted: Boolean,
    val mccmnc: String,
    val national_format: String,
    val network: String,
    val number: String,
)

data class LookupMnpResponse(
    val code: Int,
    val mnp: LookupMnp,
    val price: Float,
    val success: Boolean,
)

data class HlrCarrier(
    val country: String,
    val name: String,
    val network_code: String,
    val network_type: String,
)

abstract class LookupHlrPorted(val value: String)
class UnknownLookupHlrPorted : LookupHlrPorted("unknown")
class PortedLookupHlrReachable : LookupHlrPorted("ported")
class NotPortedLookupHlrReachable : LookupHlrPorted("not_ported")
class AssumedNotPortedLookupHlrReachable : LookupHlrPorted("assumed_not_ported")
class AssumedPortedLookupHlrReachable : LookupHlrPorted("assumed_ported")

abstract class LookupHlrReachable(val value: String)
class UnknownLookupHlrReachable : LookupHlrReachable("unknown")
class ReachableLookupHlrReachable : LookupHlrReachable("reachable")
class UndeliverableLookupHlrReachable : LookupHlrReachable("undeliverable")
class AbsentLookupHlrReachable : LookupHlrReachable("absent")
class BadNumberLookupHlrReachable : LookupHlrReachable("bad_number")
class BlacklistedLookupHlrReachable : LookupHlrReachable("blacklisted")

data class LookupHlrRoaming(
    val roaming_country_code: String,
    val roaming_network_code: String,
    val roaming_network_name: String,
    val status: String,
)

abstract class LookupHlrRoamingStatusCode(val value: String)
class NotRoamingLookupHlrRoamingStatusCode : LookupHlrRoamingStatusCode("not_roaming")
class RoamingLookupHlrRoamingStatusCode : LookupHlrRoamingStatusCode("roaming")
class UnknownLookupHlrRoamingStatusCode : LookupHlrRoamingStatusCode("unknown")

data class LookupHlrResponse(
    val country_code: String,
    val country_code_iso3: String?,
    val country_name: String,
    val country_prefix: String,
    val current_carrier: HlrCarrier,
    val gsm_code: String,
    val gsm_message: String,
    val international_format_number: String,
    val international_formatted: String,
    val lookup_outcome: Any, // TODO: LookupOutcome | Boolean (false)
    val lookup_outcome_message: String,
    val national_format_number: String,
    val original_carrier: HlrCarrier,
    val ported: String,
    val reachable: String,
    val roaming: Any, // TODO: LookupHlrRoaming | LookupHlrRoamingStatusCode
    val status: Boolean,
    val status_message: String,
    val valid_number: String,
)

abstract class LookupHlrValidNumber(val value: String)
class UnknownLookupHlrValidNumber : LookupHlrValidNumber("unknown")
class ValidLookupHlrValidNumber : LookupHlrValidNumber("valid")
class NotValidLookupHlrValidNumber : LookupHlrValidNumber("not_valid")

abstract class LookupHlrStatusMessage(val value: String)
class ErrorLookupHlrStatusMessage : LookupHlrStatusMessage("error")
class SuccessLookupHlrStatusMessage : LookupHlrStatusMessage("success")

abstract class LookupNetworkType(val value: String)
class FixedLineLookupNetworkType : LookupNetworkType("fixed_line")
class FixedLineOrMobileLookupNetworkType : LookupNetworkType("fixed_line_or_mobile")
class MobileLookupNetworkType : LookupNetworkType("mobile")
class PagerLookupNetworkType : LookupNetworkType("pager")
class PersonalNumberLookupNetworkType : LookupNetworkType("personal_number")
class PremiumRateLookupNetworkType : LookupNetworkType("premium_rate")
class SharedCostLookupNetworkType : LookupNetworkType("shared_cost")
class TollFreeLookupNetworkType : LookupNetworkType("toll_free")
class UanLookupNetworkType : LookupNetworkType("uan")
class UnknownLookupNetworkType : LookupNetworkType("unknown")
class VoicemailLookupNetworkType : LookupNetworkType("voicemail")
class VoipLookupNetworkType : LookupNetworkType("voip")

data class AnalyticsParams(
    val start: String?,
    val end: String?,
    val label: String?,
    val subaccounts: String?,
)

data class Hook(
    val created: String,
    val event_type: String,
    val id: String,
    val request_method: String,
    val target_url: String,
)

abstract class HookEventType(val value: String)
class HookEventTypeSmsInbound : HookEventType("sms_mo")
class HookEventTypeSmsStatus : HookEventType("dlr")
class HookEventTypeVoiceStatus : HookEventType("voice_status")

abstract class HookRequestMethod(val value: String)
class HookRequestMethodGet : HookRequestMethod("GET")
class HookRequestMethodPost : HookRequestMethod("POST")

object HooksAction {
    const val Read = "read"
    const val Subscribe = "subscribe"
    const val Unsubscribe = "unsubscribe"
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

data class SmsParams(var text: String, var to: String)  {
    var delay: String? = null
    var flash: Boolean? = null
    var foreign_id: String? = null
    var from: String? = null
    var label: String? = null
    var no_reload: Boolean? = null
    var unicode: Boolean? = null
    var udh: String? = null
    var utf8: Boolean? = null
    var ttl: Int? = null
    var performance_tracking: Boolean? = null
}

enum class SmsEncoding {
    gsm,
    ucs2
}

data class SmsMessage(
    val encoding: SmsEncoding,
    val error: String?,
    val error_text: String?,
    val id: String?,
    val messages: List<String>?,
    val parts: Int,
    val price: Float,
    val recipient: String,
    val sender: String,
    val success: Boolean,
    val text: String,
)

data class SmsResponse(
    val debug: StringBool,
    val balance: Float,
    val messages: List<SmsMessage>,
    val sms_type: SmsType,
    val success: String,
    val total_price: Float,
)

enum class SmsType {
    direct,
    economy
}

data class StatusParams(val msg_id: String)

enum class StringBool {
    `true`,
    `false`
}

class SubscribeHookParams(
    event_type: HookEventType,
    val target_url: String,
    request_method: HookRequestMethod?,
) {
    val event_type: String = event_type.value
    val request_method: String? = request_method?.value
}

data class SubscribeHookResponse(val success: Boolean, val id: Int?)

data class UnsubscribeHookParams(val id: Int)

data class UnsubscribeHookResponse(val success: Boolean)

data class ValidateForVoiceParams(
    val number: String,
    val callback: String?,
)

data class ValidateForVoiceResponse(
    val code: String?,
    val error: String?,
    val formatted_output: String?,
    val id: Int?,
    val sender: String?,
    val success: Boolean,
    val voice: Boolean?,
)

data class VoiceParams(
    val from: String?,
    val text: String,
    val to: String,
    val xml: Boolean?,
)

data class VoiceMessage(
    val error: String?,
    val error_text: String?,
    val id: String?,
    val messages: List<String>?,
    val price: Float,
    val recipient: String,
    val sender: String,
    val success: Boolean,
    val text: String,
)

data class VoiceResponse(
    val balance: Float,
    val debug: Boolean,
    val messages: List<VoiceMessage>,
    val success: String,
    val total_price: Float,
)

data class Balance(
    val amount: Float,
    val currency: String,
)