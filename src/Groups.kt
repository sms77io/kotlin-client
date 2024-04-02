package com.seven

import io.ktor.client.*
import io.ktor.client.request.*
import kotlin.reflect.full.memberProperties

class GroupsResource(client: HttpClient) : Resource(client) {
    suspend fun create(contact: Group): Group {
        return client.post {
            url("${BASE_URL}groups")
            body = contact
        }
    }

    suspend fun delete(id: Int): Void {
        return client.delete {
            url("${BASE_URL}groups/${id}")
        }
    }

    suspend fun all(paging: PagingParams): Paginated<Group> {
        return client.get {
            url(
                toQueryString(
                "groups?",
                PagingParams::class.memberProperties,
                paging
            )
            )
        }
    }

    suspend fun one(id: Int): Group {
        return client.get {
            url("groups/${id}")
        }
    }

    suspend fun update(group: Group): Group {
        return client.patch {
            url("groups/${group.id}")
            body = group
        }
    }
}

data class Group(
    val id: Int? = null,
    var name: String? = null,
    val members_count: Int? = null,
    val created: String? = null,
)