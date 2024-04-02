package com.seven

import kotlinx.coroutines.runBlocking
import kotlin.test.*

class ContactsTest : BaseTest() {
    private var contactId: Int? = null
    private val resource = ContactsResource(client)

    @Test
    fun testCreate() {
        runBlocking {
            val contact = Contact()
            val response = resource.create(contact)
            assertNotNull(response.id)
            contactId = response.id
        }
    }

    @Test
    fun testUpdate() {
        runBlocking {
            val oldContact = resource.create(Contact(properties = Properties(firstname = "Tim")))
            val oldFirstName = oldContact.properties.firstname

            oldContact.properties.firstname = "Tonya"
            val contact = resource.update(oldContact)

            assertNotEquals(oldFirstName, contact.properties.firstname)
        }
    }

    @Test
    fun testDelete() {
        runBlocking {
            val contact = resource.create(Contact(0))
            assertFails {
                resource.delete(contact.id!!)
            }
        }
    }

    @Test
    fun testAll() {
        runBlocking {
            val response = resource.all(PagingParams())

            for (contact in response.data) {
                assertContact(contact)
            }
        }
    }

    @Test
    fun testOne() {
        runBlocking {
            var contact = resource.create(Contact())
             contact = resource.one(contact.id!!)

            assertContact(contact)
        }
    }

    private fun assertContact(contact: Contact) {
        assertNotNull(contact.id)
    }
}
