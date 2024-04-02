package com.seven

import kotlinx.coroutines.runBlocking
import kotlin.test.*

class HooksTest : BaseTest() {
    private var hookId: Int? = 92
    private val hooks = HooksResource(client)

    @Test
    fun testGet() {
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
    fun testSubscribe() {
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
                testUnsubscribe()
            }
        }
    }

    @Test
    fun testUnsubscribe() {
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
}
