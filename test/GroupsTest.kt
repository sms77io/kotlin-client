package com.seven

import kotlinx.coroutines.runBlocking
import kotlin.test.*

class GroupsTest : BaseTest() {
    private var groupId: Int? = null
    private val resource = GroupsResource(client)

    @Test
    fun testCreate() {
        runBlocking {
            val contact = Group()
            val response = resource.create(contact)
            assertNotNull(response.id)
            groupId = response.id
        }
    }

    @Test
    fun testUpdate() {
        runBlocking {
            val oldGroup = resource.create(Group(name = "Tim"))
            val oldName = oldGroup.name

            oldGroup.name = "Tonya"
            val group = resource.update(oldGroup)

            assertNotEquals(oldName, group.name)
        }
    }

    @Test
    fun testDelete() {
        runBlocking {
            val group = resource.create(Group(0))
            assertFails {
                resource.delete(group.id!!)
            }
        }
    }

    @Test
    fun testAll() {
        runBlocking {
            val response = resource.all(PagingParams())

            for (group in response.data) {
                assertGroup(group)
            }
        }
    }

    @Test
    fun testOne() {
        runBlocking {
            var group = resource.create(Group())
             group = resource.one(group.id!!)

            assertGroup(group)
        }
    }

    private fun assertGroup(group: Group) {
        assertNotNull(group.id)
    }
}
