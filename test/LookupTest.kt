package com.seven

import kotlinx.coroutines.runBlocking
import kotlin.test.*

class LookupTest : BaseTest() {
    private val lookup = LookupResource(client)

    @Test
    fun testCnam() {
        runBlocking {
            val lookup = lookup.cnam(LookupParams("491771783130"))

            assertFalse(lookup.code.isBlank())
            assertFalse(lookup.name.isBlank())
            assertFalse(lookup.number.isBlank())
            assertFalse(lookup.success.isBlank())
        }
    }

    @Test
    fun testFormat() {
        runBlocking {
            val lookup = lookup.format(LookupParams("491771783130"))

            assertFalse(lookup.carrier.isBlank())
            assertFalse(lookup.country_code.isBlank())
            assertFalse(lookup.country_iso.isBlank())
            assertFalse(lookup.country_name.isBlank())
            assertFalse(lookup.international.isBlank())
            assertFalse(lookup.international_formatted.isBlank())
            assertFalse(lookup.national.isBlank())
        }
    }

    @Test
    fun testHlr() {
        runBlocking {
            fun assertCarrier(c: HlrCarrier) {
                assertEquals("DE", c.country)
                assertEquals("Telefónica Germany GmbH & Co. oHG (O2)", c.name)
                assertEquals("26207", c.network_code)
                assertEquals(MobileLookupNetworkType().value, c.network_type)
            }

            val lookup = lookup.hlr(LookupParams("491771783130"))

            assertEquals("DE", lookup.country_code)
            assertNull(lookup.country_code_iso3)
            assertEquals("Germany", lookup.country_name)
            assertEquals("49", lookup.country_prefix)
            assertCarrier(lookup.current_carrier)
            assertEquals("0", lookup.gsm_code)
            assertEquals("No error", lookup.gsm_message)
            assertEquals("491771783130", lookup.international_format_number)
            assertEquals("+49 177 1783130", lookup.international_formatted)
            assertEquals(true, lookup.lookup_outcome)
            assertEquals(SuccessLookupHlrStatusMessage().value, lookup.lookup_outcome_message)
            assertEquals("0177 1783130", lookup.national_format_number)
            assertCarrier(lookup.original_carrier)
            assertEquals(AssumedNotPortedLookupHlrReachable().value, lookup.ported)
            assertEquals(ReachableLookupHlrReachable().value, lookup.reachable)
            assertEquals(NotRoamingLookupHlrRoamingStatusCode().value, lookup.roaming)
            assertTrue(lookup.status)
            assertEquals(SuccessLookupHlrStatusMessage().value, lookup.status_message)
            assertEquals(ValidLookupHlrValidNumber().value, lookup.valid_number)
        }
    }

    @Test
    fun testMnp() {
        runBlocking {
            val lookup = lookup.mnp(LookupParams("491771783130"))

            assertEquals(100, lookup.code)
            assertTrue(lookup.mnp.country.isBlank())
            assertEquals("+49 177 1783130", lookup.mnp.international_formatted)
            assertFalse(lookup.mnp.isPorted)
            assertEquals("26203", lookup.mnp.mccmnc)
            assertEquals("0177 1783130", lookup.mnp.national_format)
            assertNull(lookup.mnp.network)
            assertEquals("+491771783130", lookup.mnp.number)
            assertTrue(0 < lookup.price)
            assertTrue(lookup.success)
        }
    }
}
