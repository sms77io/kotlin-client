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
import java.time.Instant
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
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

/*        println(
            subscribeHook(
                client, SubscribeHookParams(
                    event_type = HookEventType.SmsInbound,
                    request_method = HookRequestMethod.Get,
                    target_url = "http://my.tld/kotlin/${DateTimeFormatter.ISO_INSTANT.format(Instant.now())}"
                )
            )
        )*/

        //println(getAnalyticsByDate(client, GetAnalyticsParams(null, null, null, null)))

        //println(getJournalInbound(client, GetJournalParams(null, null, null, null, null)))
        //println(lookupMnpJson(client, LookupParams("491771783130")))
        //println(getPricingJson(client, PricingParams("de")))
/*        println(
            smsJson(
                client, SmsJsonParams(
                    debug = false,
                    delay = null,
                    flash = false,
                    foreign_id = null,
                    from = null,
                    label = null,
                    no_reload = false,
                    text = "HI2U!",
                    to = "491771783130!",
                    unicode = false,
                    udh = null,
                    utf8 = false,
                    ttl = null,
                    performance_tracking = false,
                )
            )
        )*/
    }
}

suspend fun createContactCsv(client: HttpClient): String {
    return client.get {
        url("https://gateway.sms77.io/api/contacts?action=${ContactsAction.Write}")
    }
}

suspend fun createContactJson(client: HttpClient): CreateContactResponse {
    return client.get {
        url("https://gateway.sms77.io/api/contacts?action=${ContactsAction.Write}&json=1")
    }
}

suspend fun editContactCsv(client: HttpClient, params: EditContactParams): String {
    return client.get {
        url(
            toQueryString(
                "contacts?action=${ContactsAction.Write}",
                EditContactParams::class.memberProperties,
                params
            )
        )
    }
}

suspend fun editContactJson(client: HttpClient, params: EditContactParams): EditContactResponse {
    return client.get {
        url(
            toQueryString(
                "contacts?action=${ContactsAction.Write}&json=1",
                EditContactParams::class.memberProperties,
                params
            )
        )
    }
}

suspend fun deleteContactCsv(client: HttpClient, params: DeleteContactParams): String {
    return client.get {
        url("https://gateway.sms77.io/api/contacts?action=${ContactsAction.Delete}&id=${params.id}")
    }
}

suspend fun deleteContactJson(client: HttpClient, params: DeleteContactParams): DeleteContactResponse {
    return client.get {
        url("https://gateway.sms77.io/api/contacts?action=${ContactsAction.Delete}&json=1&id=${params.id}")
    }
}

suspend fun getAnalyticsByCountry(client: HttpClient, params: GetAnalyticsParams): List<AnalyticByCountry> {
    return client.get {
        url(
            toQueryString(
                "analytics?group_by=${AnalyticsGroupBy.Country}",
                GetAnalyticsParams::class.memberProperties,
                params
            )
        )
    }
}

suspend fun getAnalyticsByDate(client: HttpClient, params: GetAnalyticsParams): List<AnalyticByDate> {
    return client.get {
        url(
            toQueryString(
                "analytics?group_by=${AnalyticsGroupBy.Date}",
                GetAnalyticsParams::class.memberProperties,
                params
            )
        )
    }
}

suspend fun getAnalyticsByLabel(client: HttpClient, params: GetAnalyticsParams): List<AnalyticByLabel> {
    return client.get {
        url(
            toQueryString(
                "analytics?group_by=${AnalyticsGroupBy.Label}",
                GetAnalyticsParams::class.memberProperties,
                params
            )
        )
    }
}

suspend fun getAnalyticsBySubaccount(client: HttpClient, params: GetAnalyticsParams): List<AnalyticBySubaccount> {
    return client.get {
        url(
            toQueryString(
                "analytics?group_by=${AnalyticsGroupBy.Subaccount}",
                GetAnalyticsParams::class.memberProperties,
                params
            )
        )
    }
}

suspend fun getBalance(client: HttpClient): Float {
    return client.get<String> {
        url("https://gateway.sms77.io/api/balance")
    }.toFloat()
}

suspend fun getContactsCsv(client: HttpClient): String {
    return client.get {
        url("https://gateway.sms77.io/api/contacts?action=${ContactsAction.Read}")
    }
}

suspend fun getContactsJson(client: HttpClient): List<Contact> {
    return client.get {
        url("https://gateway.sms77.io/api/contacts?action=${ContactsAction.Read}&json=1")
    }
}

suspend fun getHooks(client: HttpClient): List<Hook> {
    return client.get {
        url("https://gateway.sms77.io/api/hooks?action=${HooksAction.Read}")
    }
}

suspend fun getJournalInbound(client: HttpClient, params: GetJournalParams): List<JournalInbound> {
    return client.get {
        url(
            toQueryString(
                "journal?type=${JournalType.Inbound}",
                GetJournalParams::class.memberProperties,
                params
            )
        )
    }
}

suspend fun getJournalOutbound(client: HttpClient, params: GetJournalParams): List<JournalOutbound> {
    return client.get {
        url(
            toQueryString(
                "journal?type=${JournalType.Outbound}",
                GetJournalParams::class.memberProperties,
                params
            )
        )
    }
}

suspend fun getJournalReplies(client: HttpClient, params: GetJournalParams): List<JournalReplies> {
    return client.get {
        url(
            toQueryString(
                "journal?type=${JournalType.Replies}",
                GetJournalParams::class.memberProperties,
                params
            )
        )
    }
}

suspend fun getJournalVoice(client: HttpClient, params: GetJournalParams): List<JournalVoice> {
    return client.get {
        url(
            toQueryString(
                "journal?type=${JournalType.Voice}",
                GetJournalParams::class.memberProperties,
                params
            )
        )
    }
}

suspend fun subscribeHook(client: HttpClient, params: SubscribeHookParams): SubscribeHookResponse {
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

suspend fun unsubscribeHook(client: HttpClient, params: UnsubscribeHookParams): UnsubscribeHookResponse {
    return client.post {
        url("https://gateway.sms77.io/api/hooks?action=${HooksAction.Unsubscribe}&id=${params.id}")
    }
}

suspend fun lookupCnam(client: HttpClient, params: LookupParams): LookupCnamResponse {
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

suspend fun lookupFormat(client: HttpClient, params: LookupParams): LookupFormatResponse {
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

suspend fun lookupHlr(client: HttpClient, params: LookupParams): LookupHlrResponse {
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

suspend fun lookupMnp(client: HttpClient, params: LookupParams): String {
    return client.get {
        url(
            toQueryString(
                "lookup?type=${LookupType.MobileNumberPortability}",
                LookupParams::class.memberProperties,
                params
            )
        )
    }
}

suspend fun lookupMnpJson(client: HttpClient, params: LookupParams): LookupMnpResponse {
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

suspend fun getPricingCsv(client: HttpClient, params: PricingParams): String {
    return client.get {
        url(
            toQueryString(
                "pricing?format=${PricingFormat.Csv}",
                PricingParams::class.memberProperties,
                params
            )
        )
    }
}

suspend fun getPricingJson(client: HttpClient, params: PricingParams): PricingResponse {
    return client.get {
        url(
            toQueryString(
                "pricing?format=${PricingFormat.Json}",
                PricingParams::class.memberProperties,
                params
            )
        )
    }
}

suspend fun sms(client: HttpClient, params: SmsParams): String {
    return client.post {
        url(
            toQueryString(
                "sms",
                SmsParams::class.memberProperties,
                params
            )
        )
    }
}

suspend fun smsJson(client: HttpClient, params: SmsJsonParams): SmsResponse {
    return client.post {
        url(
            toQueryString(
                "sms?json=1",
                SmsJsonParams::class.memberProperties,
                params
            )
        )
    }
}

suspend fun status(client: HttpClient, params: StatusParams): String {
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

private inline fun <reified T> toQueryString(endpoint: String, props: Collection<KProperty1<T, *>>, params: T): String {
    var url = "https://gateway.sms77.io/api/${endpoint}"

    for (prop in props) {
        val value = prop.get(params)

        if (null != value && false != value) {
            url += "&${prop.name}=${value}"
        }
    }

    return url
}

data class Contact(val ID: String, val Name: String, val Number: String)
object ContactsAction {
    const val Delete = "del"
    const val Read = "read"
    const val Write = "write"
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
    val success: String
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
    val success: Boolean
)

data class LookupMnp(
    val country: String,
    val international_formatted: String,
    val isPorted: Boolean,
    val mccmnc: String,
    val national_format: String,
    val network: String,
    val number: String
)

data class LookupMnpResponse(
    val code: Int,
    val mnp: LookupMnp,
    val price: Float,
    val success: Boolean
)

data class HlrCarrier(
    val country: String,
    val name: String,
    val network_code: String,
    val network_type: LookupNetworkType
)

enum class LookupHlrPorted {
    unknown,
    ported,
    not_ported,
    assumed_not_ported,
    assumed_ported,
}

enum class LookupHlrReachable {
    unknown,
    reachable,
    undeliverable,
    absent,
    bad_number,
    blacklisted
}

data class LookupHlrRoaming(
    val roaming_country_code: String,
    val roaming_network_code: String,
    val roaming_network_name: String,
    val status: LookupHlrStatusCode
)

enum class LookupHlrStatusCode {
    not_roaming,
    roaming,
    unknown
}

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
    val lookup_outcome: Int,
    val lookup_outcome_message: String,
    val national_format_number: String,
    val original_carrier: HlrCarrier,
    val ported: LookupHlrPorted,
    val reachable: LookupHlrReachable,
    val roaming: LookupHlrRoaming,
    val status: Boolean,
    val status_message: LookupHlrStatusMessage,
    val valid_number: LookupHlrValidNumber
)

enum class LookupHlrValidNumber {
    unknown,
    valid,
    not_valid
}

enum class LookupHlrStatusMessage {
    error,
    success
}

enum class LookupNetworkType {
    fixed_line,
    fixed_line_or_mobile,
    mobile,
    pager,
    personal_number,
    premium_rate,
    shared_cost,
    toll_free,
    uan,
    unknown,
    voicemail,
    voip
}

data class CreateContactResponse(val `return`: String, val id: String)
data class DeleteContactParams(val id: Number)
data class DeleteContactResponse(val `return`: String)
data class EditContactParams(val id: Number, val email: String?, val empfaenger: String?, val nick: String?)
data class EditContactResponse(val `return`: String)
data class GetAnalyticsParams(
    val start: String?,
    val end: String?,
    val label: String?,
    val subaccounts: String?,
)

data class Hook(
    val created: String,
    val event_type: HookEventType,
    val id: String,
    val request_method: HookRequestMethod,
    val target_url: String,
)

enum class HookEventType(val value: String) {
    SmsInbound("sms_mo"),
    SmsStatus("dlr"),
    VoiceStatus("voice_status")
}

enum class HookRequestMethod(val value: String) {
    Get("GET"),
    Post("POST"),
}

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

data class GetJournalParams(
    val date_from: String?,
    val date_to: String?,
    val id: Int?,
    val state: String?,
    val to: String?
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
    val type: String?
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
    val networks: List<PricingCountryNetwork>
)

data class PricingCountryNetwork(
    val comment: String,
    val features: List<String>,
    val mcc: String,
    val mncs: List<String>,
    val networkName: String,
    val price: Float
)

data class PricingParams(val country: String?)

data class PricingResponse(
    val countCountries: Int,
    val countNetworks: Int,
    val countries: List<PricingCountry>
)

object PricingFormat {
    const val Csv = "csv"
    const val Json = "json"
}

interface SmsBaseParams {
    val debug: Boolean?
    val delay: String?
    val flash: Boolean?
    val foreign_id: String?
    val from: String?
    val label: String?
    val no_reload: Boolean?
    val text: String
    val to: String
    val unicode: Boolean?
    val udh: String?
    val utf8: Boolean?
    val ttl: Int?
    val performance_tracking: Boolean?
}

data class SmsParams(
    override val debug: Boolean?,
    override val delay: String?,
    override val flash: Boolean?,
    override val foreign_id: String?,
    override val from: String?,
    override val label: String?,
    override val no_reload: Boolean?,
    override val text: String,
    override val to: String,
    override val unicode: Boolean?,
    override val udh: String?,
    override val utf8: Boolean?,
    override val ttl: Int?,
    override val performance_tracking: Boolean?,
    val details: Boolean?,
    val return_msg_id: Boolean?

) : SmsBaseParams

data class SmsJsonParams(
    override val debug: Boolean?,
    override val delay: String?,
    override val flash: Boolean?,
    override val foreign_id: String?,
    override val from: String?,
    override val label: String?,
    override val no_reload: Boolean?,
    override val text: String,
    override val to: String,
    override val unicode: Boolean?,
    override val udh: String?,
    override val utf8: Boolean?,
    override val ttl: Int?,
    override val performance_tracking: Boolean?,
) : SmsBaseParams

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
    val text: String
)

data class SmsResponse(
    val debug: StringBool,
    val balance: Float,
    val messages: List<SmsMessage>,
    val sms_type: SmsType,
    val success: String,
    val total_price: Float
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

data class SubscribeHookResponse(val success: Boolean, val id: Number?)
data class UnsubscribeHookParams(val id: Number)
data class UnsubscribeHookResponse(val success: Boolean)