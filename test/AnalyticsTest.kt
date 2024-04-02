package com.seven

import kotlinx.coroutines.runBlocking
import kotlin.test.*

class AnalyticsTest : BaseTest() {
    private val analytics = AnalyticsResource(client)

    @Test
    fun testByCountry() {
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
    fun testByDate() {
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
    fun testByLabel() {
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
    fun testBySubaccount() {
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
}
