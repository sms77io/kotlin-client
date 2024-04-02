package com.seven

import kotlinx.coroutines.runBlocking
import kotlin.test.*

class ApplicationTest : BaseTest() {
    @Test
    fun testBalance() {
        runBlocking {
            val res = balance(client)
            assertTrue(0 <= res.amount)
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
