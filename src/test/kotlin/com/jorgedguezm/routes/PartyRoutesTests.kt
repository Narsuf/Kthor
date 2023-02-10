package com.jorgedguezm.routes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PartyRoutesTests {

    private val partyRequest = getStringJson("party_request.json")
    private val partyRequestEdited = getStringJson("party_request_edit.json")
    private val partyResponse = getStringJson("party_response.json").filter { !it.isWhitespace() }
    private val partyResponseEdited = getStringJson("party_response_edit.json").filter { !it.isWhitespace() }

    private fun getStringJson(path: String) = javaClass.classLoader.getStringJson("party/$path")
    private fun String.toPartiesArray() = "{\"parties\":[$this]}"

    @Test
    fun testPostParty() = testApplication {
        environment { config = ApplicationConfig("application-test.conf") }

        // Add.
        client.post("/party") {
            contentType(ContentType.Application.Json)
            setBody(partyRequest)
        }

        // Get all.
        val getAllResponse = client.get("/party")
        val partyId = getAllResponse.bodyAsText().getId()
        val expected = partyResponse.replaceFirst("0", partyId)

        assertEquals(expected.toPartiesArray(), getAllResponse.bodyAsText())
        assertEquals(HttpStatusCode.OK, getAllResponse.status)

        // Get by id.
        val getResponse = client.get("/party/$partyId")

        assertEquals(expected, getResponse.bodyAsText())
        assertEquals(HttpStatusCode.OK, getResponse.status)

        client.delete("/party/$partyId")

        // Delete.
        val getResponseAfterDelete = client.get("/party")
        assertEquals("No parties found.", getResponseAfterDelete.bodyAsText())
        assertEquals(HttpStatusCode.NotFound, getResponseAfterDelete.status)
    }

    @Test
    fun testUpdateParty() = testApplication {
        environment { config = ApplicationConfig("application-test.conf") }

        // Add.
        client.post("/party") {
            contentType(ContentType.Application.Json)
            setBody(partyRequest)
        }

        val getAllResponse = client.get("/party")
        val partyId = getAllResponse.bodyAsText().getId()
        val expected = partyResponseEdited.replaceFirst("0", partyId)

        // Update.
        client.post("/party/$partyId") {
            contentType(ContentType.Application.Json)
            setBody(partyRequestEdited)
        }

        val getResponse = client.get("/party/$partyId")
        assertEquals(expected, getResponse.bodyAsText())
        assertEquals(HttpStatusCode.OK, getResponse.status)

        // Delete.
        val deleteResponse = client.delete("/party/$partyId")
        assertEquals("Party removed correctly.", deleteResponse.bodyAsText())
        assertEquals(HttpStatusCode.Accepted, deleteResponse.status)
    }

    @Test
    fun testGetNoParties() = testApplication {
        environment { config = ApplicationConfig("application-test.conf") }

        val response = client.get("/party")

        assertEquals("No parties found.", response.bodyAsText())
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testGetNoParty() = testApplication {
        environment { config = ApplicationConfig("application-test.conf") }

        val response = client.get("/party/1")

        assertEquals("No party with id 1.", response.bodyAsText())
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testDeleteNoParty() = testApplication {
        environment { config = ApplicationConfig("application-test.conf") }

        val response = client.delete("/party/1")

        assertEquals("No party with id 1.", response.bodyAsText())
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testRoutingNotWorkingForRelease() = testApplication {
        environment { config = ApplicationConfig("application-test-release.conf") }

        // Get all.
        var response = client.get("/party")
        assertTrue(response.bodyAsText().isEmpty())
        assertEquals(HttpStatusCode.NotFound, response.status)

        // Get.
        response = client.get("/party/1")
        assertTrue(response.bodyAsText().isEmpty())
        assertEquals(HttpStatusCode.NotFound, response.status)

        // Add.
        response = client.post("/party")
        assertTrue(response.bodyAsText().isEmpty())
        assertEquals(HttpStatusCode.NotFound, response.status)

        // Update.
        response = client.post("/party/1")
        assertTrue(response.bodyAsText().isEmpty())
        assertEquals(HttpStatusCode.NotFound, response.status)

        // Delete.
        response = client.delete("/party/1")
        assertTrue(response.bodyAsText().isEmpty())
        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}