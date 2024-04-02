package com.seven

import kotlinx.coroutines.runBlocking
import kotlin.test.*

class SmsTest : BaseTest() {
    private val sms = SmsResource(client)

    @Test
    fun testDispatch() {
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
    fun testStatus() {
        runBlocking {
            val text = sms.status(StatusParams("77133879512"))

            assertFalse(text.isBlank())
        }
    }
}
