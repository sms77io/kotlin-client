package com.seven

import kotlinx.coroutines.runBlocking
import kotlin.test.*

class ApplicationTest : BaseTest() {
    private var hookId: Int? = 92
    private val hooks = HooksResource(client)
    private val sms = SmsResource(client)

    @Test
    fun testBalance() {
        runBlocking {
            val res = balance(client)
            assertTrue(0 <= res.amount)
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
}
