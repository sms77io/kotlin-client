package com.seven

import io.ktor.client.*
import io.ktor.client.request.*
import kotlin.reflect.full.memberProperties

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