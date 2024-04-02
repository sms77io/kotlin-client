package com.seven

import kotlinx.coroutines.runBlocking
import kotlin.test.*

class ContactsTest : BaseTest() {
    private var contactId: Int? = null
    private val resource = ContactsResource(client)

    @Test
    fun testContactCreateEditDelete() {
        fun create() {
            runBlocking {
                val lines = resource.create().lines()
                assertEquals(2, lines.size)
                val code = lines[0]
                val id = lines[1].toInt()
                assertEquals("152", code)
                assertTrue(0 < id)
                contactId = id
            }
        }

        fun edit() {
            if (null === contactId) {
                return
            }

            runBlocking {
                assertEquals("152", resource.edit(editContactParams()))
            }
        }

        fun delete() {
            if (null === contactId) {
                return
            }

            runBlocking {
                assertEquals("152", resource.delete(DeleteContactParams(id = contactId!!)))
                contactId = null
            }
        }

        create()
        edit()
        delete()
    }

    @Test
    fun testContactCreateEditDeleteJson() {
        fun create() {
            runBlocking {
                val o = resource.createJson()
                assertEquals("152", o.`return`)
                val id = o.id.toInt()
                assertTrue(0 < id)
                contactId = id
            }
        }

        fun edit() {
            if (null === contactId) {
                return
            }

            runBlocking {
                assertEquals("152", resource.editJson(editContactParams()).`return`)
            }
        }

        fun delete() {
            if (null === contactId) {
                return
            }

            runBlocking {
                assertEquals("152", resource.deleteJson(DeleteContactParams(id = contactId!!)).`return`)
                contactId = null
            }
        }

        create()
        edit()
        delete()
    }

    @Test
    fun testGetContacts() {
        runBlocking {
            assertFalse(resource.get().isBlank())
        }
    }

    @Test
    fun testGetContactsJson() {
        runBlocking {
            for (contact in resource.getJson()) {
                assertFalse(contact.ID.isBlank())
                assertNotNull(contact.Name)
                assertNotNull(contact.Number)
            }
        }
    }

    private fun editContactParams(): EditContactParams {
        return EditContactParams(
            email = "test@seven.io",
            empfaenger = "${System.currentTimeMillis()}",
            id = contactId!!,
            nick = "Tommy Testing"
        )
    }
}
