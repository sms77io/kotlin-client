package com.seven

import kotlinx.coroutines.runBlocking
import kotlin.test.*

class RcsTest : BaseTest() {
    private val resource = RcsResource(client)

    @Test
    fun testDispatch() {
        runBlocking {
            val params = RcsParams("491771783130", "HI2U!")
            params.from = "Kotlin-Test"
            val o = resource.dispatch(params)

            assertEquals("100", o.success)
            assertEquals(0.toFloat(), o.total_price) // assertTrue(0 < o.total_price)
            assertEquals(SmsType.direct, o.sms_type)
            assertTrue(0 <= o.balance)
            assertEquals(clientParams.dummy.toString(), o.debug.toString())

            for (m in o.messages) {
                assertEquals(MessageEncoding.gsm, m.encoding)
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
    fun testDelete() {
        runBlocking {
            val params = RcsParams("491771783130", "HI2U!")
            params.delay = "2033-12-12 15:13"
            val rcsResponse = resource.dispatch(params)
            val msg = rcsResponse.messages.first()
            val response = resource.delete(msg)

            assertTrue(response.success)
        }
    }

    @Test
    fun testEvents() {
        runBlocking {
            enumValues<Event>().forEach {
                val params = EventsParams("", "", it)
                val response = resource.events(params)

                assertTrue(response.success)
            }


        }
    }
}
