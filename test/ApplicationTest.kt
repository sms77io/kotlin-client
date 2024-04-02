package com.seven

import kotlinx.coroutines.runBlocking
import kotlin.test.*

class ApplicationTest : BaseTest() {
    private var hookId: Int? = 92
    private var contactId: Int? = null
    private val analytics = AnalyticsResource(client)
    private val hooks = HooksResource(client)
    private val journal = JournalResource(client)
    private val lookup = LookupResource(client)
    private val sms = SmsResource(client)

    @Test
    fun testBalance() {
        runBlocking {
            val res = balance(client)
            assertTrue(0 <= res.amount)
        }
    }

    @Test
    fun testAnalyticsByCountry() {
        runBlocking {
            fun each(a: AnalyticByCountry) {
                assertFalse(a.country.isBlank())
            }

            testAnalyticsBase(
                analytics.byCountry(AnalyticsParams(null, null, null, null)),
                ::each
            )
        }
    }

    @Test
    fun testAnalyticsByDate() {
        runBlocking {
            fun each(a: AnalyticByDate) {
                assertFalse(a.date.isBlank())
            }

            testAnalyticsBase(
                analytics.byDate(AnalyticsParams(null, null, null, null)),
                ::each
            )
        }
    }

    @Test
    fun testAnalyticsByLabel() {
        runBlocking {
            fun each(a: AnalyticByLabel) {
                assertNotNull(a.label)
            }

            testAnalyticsBase(
                analytics.byLabel(AnalyticsParams(null, null, null, null)),
                ::each
            )
        }
    }

    @Test
    fun testAnalyticsBySubaccount() {
        runBlocking {
            fun each(a: AnalyticBySubaccount) {
                assertFalse(a.account.isBlank())
            }

            testAnalyticsBase(
                analytics.bySubaccount(AnalyticsParams(null, null, null, null)),
                ::each
            )
        }
    }

    @Test
    fun testGetHooks() {
        runBlocking {
            val hooks = hooks.get()

            assertTrue(hooks.success)

            for (hook in hooks.hooks) {
                assertFalse(hook.created.isBlank())
                assertFalse(hook.event_type.isBlank())
                assertFalse(hook.id.isBlank())
                assertFalse(hook.request_method.isBlank())
                assertFalse(hook.target_url.isBlank())
            }
        }
    }

    @Test
    fun testJournalInbound() {
        runBlocking {
            fun each(j: JournalInbound) {}

            testJournalBase(
                journal.inbound(JournalParams(null, null, null, null, null, null)),
                ::each
            )
        }
    }

    @Test
    fun testJournalOutbound() {
        runBlocking {
            fun each(j: JournalOutbound) {
                assertFalse(j.connection.isBlank())
                j.dlr?.let { assertFalse(it.isBlank()) }
                j.dlr_timestamp?.let { assertFalse(it.isBlank()) }
                j.foreign_id?.let { assertFalse(it.isBlank()) }
                j.label?.let { assertFalse(it.isBlank()) }
                j.latency?.let { assertFalse(it.isBlank()) }
                j.mccmnc?.let { assertFalse(it.isBlank()) }
                j.type?.let { assertFalse(it.isBlank()) }
            }

            testJournalBase(
                journal.outbound(JournalParams(null, null, null, null, null, null)),
                ::each
            )
        }
    }

    @Test
    fun testJournalReplies() {
        runBlocking {
            fun each(j: JournalReplies) {}

            testJournalBase(
                journal.replies(JournalParams(null, null, null, null, null, null)),
                ::each
            )
        }
    }

    @Test
    fun testJournalVoice() {
        runBlocking {
            fun each(j: JournalVoice) {
                assertFalse(j.duration.isBlank())
                assertNotNull(j.error)
                assertNotNull(j.status)
                assertNotNull(j.xml)
            }

            testJournalBase(
                journal.voice(JournalParams(null, null, null, null, null, null)),
                ::each
            )
        }
    }

    @Test
    fun testLookupCnam() {
        runBlocking {
            val lookup = lookup.cnam(LookupParams("491771783130"))

            assertFalse(lookup.code.isBlank())
            assertFalse(lookup.name.isBlank())
            assertFalse(lookup.number.isBlank())
            assertFalse(lookup.success.isBlank())
        }
    }

    @Test
    fun testLookupFormat() {
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
    fun testLookupHlr() {
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
    fun testLookupMnp() {
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

    @Test
    fun testPricing() {
        runBlocking {
            val pricings = pricing(client, PricingParams(country = null))
            var networks = 0
            assertEquals(pricings.countCountries, pricings.countries.size)
            for (c in pricings.countries) {
                assertFalse(c.countryName.isBlank())
                for (n in c.networks) {
                    networks += 1
                    assertFalse(n.mcc.isBlank())
                    assertTrue(0 < n.price)
                    for (f in n.features) {
                        assertFalse(f.isBlank())
                    }
                    for (m in n.mncs) {
                        assertFalse(m.isBlank())
                    }
                }
            }
            assertEquals(networks, pricings.countNetworks)
        }
    }

    @Test
    fun testSms() {
        runBlocking {
            val params = SmsParams(to = "491771783130", text = "HI2U!")
            params.from = "Kotlin-Test"
            val o = sms.dispatch(params)

            assertEquals("100", o.success)
            assertEquals(0.toFloat(), o.total_price) // assertTrue(0 < o.total_price)
            assertEquals(SmsType.direct, o.sms_type)
            assertTrue(0 <= o.balance)
            assertEquals(clientParams.dummy.toString(), o.debug.toString())

            for (m in o.messages) {
                assertEquals(SmsEncoding.gsm, m.encoding)
                if (null !== m.error) {
                    assertFalse(m.error!!.isBlank())
                }
                if (null !== m.error_text) {
                    assertFalse(m.error_text!!.isBlank())
                }
                if (null !== m.id) {
                    assertFalse(m.id!!.isBlank())
                }
                if (null !== m.messages) {
                    for (mm in m.messages!!) {
                        assertFalse(mm.isBlank())
                    }
                }
                assertEquals(1, m.parts)
                assertEquals(0.toFloat(), m.price)
                assertEquals(params.to, m.recipient)
                assertEquals(true, m.success)
            }
        }

    }

    @Test
    fun testSmsStatus() {
        runBlocking {
            val text = sms.status(StatusParams("77133879512"))

            assertFalse(text.isBlank())
        }
    }

    @Test
    fun testSubscribeHook() {
        runBlocking {
            val o = hooks.subscribe(SubscribeHookParams(
                event_type = HookEventTypeSmsInbound(),
                request_method = HookRequestMethodGet(),
                target_url = "https://my.tld/kotlin/test/${System.currentTimeMillis()}"
            ))

            if (null === o.id) {
                assertFalse(o.success)
                assertNull(o.id)
            } else {
                assertTrue(o.success)
                assertTrue(0 < o.id!!)

                hookId = o.id
                testUnsubscribeHook()
            }
        }
    }

    @Test
    fun testUnsubscribeHook() {
        if (null === hookId) {
            return
        }

        runBlocking {
            val o = hooks.unsubscribe(UnsubscribeHookParams(id = hookId!!))

            if (o.success) {
                hookId = null
            }

            assertTrue(o.success)
        }
    }

    @Test
    fun testValidateForVoice() {
        runBlocking {
            val o = validateForVoice(client, ValidateForVoiceParams(
                callback = "",
                number = "77133879512"
            ))

            if (null !== o.code) {
                assertFalse(o.code!!.isBlank())
            }

            if (null !== o.error) {
                assertFalse(o.error!!.isBlank())
            }

            if (null !== o.formatted_output) {
                assertFalse(o.formatted_output!!.isBlank())
            }

            if (null !== o.id) {
                assertTrue(0 < o.id!!)
            }

            if (null !== o.sender) {
                assertFalse(o.sender!!.isBlank())
            }

            if (null !== o.voice) {
                assertNotNull(o.voice)
            }

            assertTrue(o.success)
        }
    }

    @Test
    fun testVoice() {
        runBlocking {
            val params = VoiceParams(
                from = "Kotlin-Test",
                text = "Hi my friend",
                to = "4917987654321",
                xml = false
            )
            val response = voice(client, params)
            val msg = response.messages.first()

            assertEquals(params.text, msg.text)
            assertEquals(params.to, msg.recipient)
        }
    }

    private fun <T> testAnalyticsBase(analytics: List<T>, each: (analytic: T) -> Unit) where T : AnalyticBase {
        for (analytic in analytics) {
            assertTrue(0 <= analytic.direct)
            assertTrue(0 <= analytic.economy)
            assertTrue(0 <= analytic.hlr)
            assertTrue(0 <= analytic.inbound)
            assertTrue(0 <= analytic.mnp)
            assertTrue(0 <= analytic.usage_eur)
            assertTrue(0 <= analytic.voice)

            each(analytic)
        }
    }

    private fun <T> testJournalBase(journals: List<T>, each: (journal: T) -> Unit) where T : JournalBase {
        for (journal in journals) {
            assertNotEquals(0.toBigInteger(), journal.id.toBigInteger())
            assertFalse(journal.timestamp.isBlank())
            if (0.toFloat() != journal.price.toFloat()) {
                assertFalse(journal.to.isBlank())
                assertFalse(journal.text.isBlank())
            }

            each(journal)
        }
    }
}
