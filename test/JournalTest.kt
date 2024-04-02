package com.seven

import kotlinx.coroutines.runBlocking
import kotlin.test.*

class JournalTest : BaseTest() {
    private val journal = JournalResource(client)

    @Test
    fun testInbound() {
        runBlocking {
            fun each(j: JournalInbound) {}

            testJournalBase(
                journal.inbound(JournalParams(null, null, null, null, null, null)),
                ::each
            )
        }
    }

    @Test
    fun testOutbound() {
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
    fun testReplies() {
        runBlocking {
            fun each(j: JournalReplies) {}

            testJournalBase(
                journal.replies(JournalParams(null, null, null, null, null, null)),
                ::each
            )
        }
    }

    @Test
    fun testVoice() {
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
