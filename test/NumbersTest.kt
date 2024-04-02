package com.seven

import kotlinx.coroutines.runBlocking
import kotlin.test.*

class NumbersTest : BaseTest() {
    private val resource = NumbersResource(client)

    @Test
    fun testAvailable() {
        runBlocking {
            val params = AvailableParams()

            for (m in resource.available(params).availableNumbers) {
                assertNumber(m)
            }
        }

    }

    @Test
    fun testOrder() {
        runBlocking {
            val (_, response) = orderNumber()

            assertTrue(response.success)
        }
    }

    @Test
    fun testDelete() {
        runBlocking {
            val (available) = orderNumber()
            val response = resource.delete(available.number)

            assertTrue(response.success)
        }
    }

    @Test
    fun testActive() {
        runBlocking {
            for (m in resource.active()) {
                assertNumber(m)
            }
        }
    }

    @Test
    fun testOne() {
        runBlocking {
            val (available) = orderNumber()
            val response = resource.one(available.number)

            assertEquals(response.number, available.number)
        }
    }

    @Test
    fun testUpdate() {
        runBlocking {
            val (available) = orderNumber()
            available.friendly_name = "Friendly Name ${System.currentTimeMillis()}"
            val response = resource.update(available)

            assertEquals(available.friendly_name, response.friendly_name)
        }
    }

    private fun orderNumber(): Pair<Number, OrderResponse> {
        var available: Number
        var response: OrderResponse
        runBlocking {
             available = resource.available(AvailableParams()).availableNumbers.first()
             response = resource.order(OrderParams(available.number))
        }

        return available to response
    }

    private fun assertNumber(number: Number) {
        assertNotNull(number.created)
    }
}
