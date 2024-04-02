package com.seven

import io.ktor.client.*
import io.ktor.client.request.*
import java.util.*
import kotlin.reflect.full.memberProperties

class ContactsResource(client: HttpClient) : Resource(client) {
    suspend fun create(contact: Contact): Contact {
        return client.post {
            url("${BASE_URL}contacts")
            body = contact
        }
    }

    suspend fun delete(id: Int): Void {
        return client.delete {
            url("${BASE_URL}contacts/${id}")
        }
    }

    suspend fun all(paging: PagingParams): Paginated<Contact> {
        return client.get {
            url(toQueryString(
                "contacts?",
                PagingParams::class.memberProperties,
                paging
            ))
        }
    }

    suspend fun one(id: Int): Contact {
        return client.get {
            url("contacts/${id}")
        }
    }

    suspend fun update(contact: Contact): Contact {
        return client.patch {
            url("contacts/${contact.id}")
            body = contact
        }
    }
}

data class Contact(
    val id: Int? = null,
    val avatar: String? = null,
    val created: String? = null,
    val groups: List<Int> = listOf(),
    val initials: Initials = Initials(),
    val validation: Validation = Validation(),
    val properties: Properties = Properties(),
)

data class Validation(
    val state: String? = null,
    val timestamp: String? = null,
)

data class Initials(
    val color: String? = null,
    val initials: String? = null,
)

data class Properties(
    var firstname: String? = null,
    var lastname: String? = null,
    var mobile_number: String? = null,
    var home_number: String? = null,
    var email: String? = null,
    var address: String? = null,
    var postal_code: String? = null,
    var city: String? = null,
    var birthday: String? = null,
    var notes: String? = null,
)