package com.jorgedguezm.routes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ResultRoutesTests {

    private val electionRequest = javaClass.classLoader.getStringJson("election/election_request.json")
    private val partyRequest = javaClass.classLoader.getStringJson("party/party_request.json")
    private val resultRequest = getStringJson("result_request.json")
    private val resultRequestEdited = getStringJson("result_request_edit.json")
    private val resultWithPartyRequest = getStringJson("result_party_request.json")
    private val resultResponse = getStringJson("result_response.json").filter { !it.isWhitespace() }
    private val resultResponseEdited = getStringJson("result_response_edit.json").filter { !it.isWhitespace() }

    private fun getStringJson(path: String) = javaClass.classLoader.getStringJson("result/$path")
    private fun String.toResultsArray() = "{\"results\":[$this]}"

    private var electionId = ""
    private var partyId = ""
    private var finalResultRequest = ""

    private fun createForeignKeys(createParty: Boolean = true) = testApplication {
        environment { config = ApplicationConfig("application-test.conf") }

        client.post("/election") {
            contentType(ContentType.Application.Json)
            setBody(electionRequest)
        }

        if (createParty) {
            client.post("/party") {
                contentType(ContentType.Application.Json)
                setBody(partyRequest)
            }

            partyId = client.get("/party").bodyAsText().getId()
        }

        electionId = client.get("/election").bodyAsText().getId()
        finalResultRequest = resultRequest
            .replaceFirst("0", partyId)
            .replaceFirst("-1", electionId)
    }

    private fun cleanForeignKeys() = testApplication {
        environment { config = ApplicationConfig("application-test.conf") }

        client.delete("/election/$electionId")
        client.delete("/party/$partyId")
    }

    @Test
    fun testPostResult() = testApplication {
        environment { config = ApplicationConfig("application-test.conf") }

        createForeignKeys()

        // Add.
        client.post("/result") {
            contentType(ContentType.Application.Json)
            setBody(finalResultRequest)
        }

        // Get all.
        val getAllResponse = client.get("/result")
        val resultId = getAllResponse.bodyAsText().getId()
        val expected = resultResponse
            .replaceFirst("0", resultId)
            .replace("-1", partyId)
            .replaceFirst("-2", electionId)

        assertEquals(expected.toResultsArray(), getAllResponse.bodyAsText())
        assertEquals(HttpStatusCode.OK, getAllResponse.status)

        // Get by id.
        val getResponse = client.get("/result/$resultId")

        assertEquals(expected, getResponse.bodyAsText())
        assertEquals(HttpStatusCode.OK, getResponse.status)

        // Delete.
        client.delete("/result/$resultId")

        val getResponseAfterDelete = client.get("/result")
        assertEquals("No results found.", getResponseAfterDelete.bodyAsText())
        assertEquals(HttpStatusCode.NotFound, getResponseAfterDelete.status)

        cleanForeignKeys()
    }

    @Test
    fun testPostResultWithParty() = testApplication {
        environment { config = ApplicationConfig("application-test.conf") }

        createForeignKeys(false)

        val finalResultWithPartyRequest = resultWithPartyRequest.replaceFirst("-1", electionId)

        // Add.
        client.post("/result") {
            contentType(ContentType.Application.Json)
            setBody(finalResultWithPartyRequest)
        }

        // Get all.
        val getAllResponse = client.get("/result")
        val getAllPartiesResponse = client.get("/party")
        val resultId = getAllResponse.bodyAsText().getId()
        partyId = getAllPartiesResponse.bodyAsText().getId()
        val expected = resultResponse
            .replaceFirst("0", resultId)
            .replace("-1", partyId)
            .replaceFirst("-2", electionId)

        assertEquals(expected.toResultsArray(), getAllResponse.bodyAsText())
        assertEquals(HttpStatusCode.OK, getAllResponse.status)

        // Clean.
        client.delete("/result/$resultId")
        cleanForeignKeys()
    }

    @Test
    fun testUpdateResult() = testApplication {
        environment { config = ApplicationConfig("application-test.conf") }

        createForeignKeys()

        // Add.
        client.post("/result") {
            contentType(ContentType.Application.Json)
            setBody(finalResultRequest)
        }

        val getAllResponse = client.get("/result")
        val resultId = getAllResponse.bodyAsText().getId()
        val expected = resultResponseEdited
            .replaceFirst("0", resultId)
            .replace("-1", partyId)
            .replaceFirst("-2", electionId)

        // Update.
        client.post("/result/$resultId") {
            contentType(ContentType.Application.Json)
            setBody(resultRequestEdited)
        }

        val getResponse = client.get("/result/$resultId")
        assertEquals(expected, getResponse.bodyAsText())
        assertEquals(HttpStatusCode.OK, getResponse.status)

        // Delete.
        val deleteResponse = client.delete("/result/$resultId")
        assertEquals("Result removed correctly.", deleteResponse.bodyAsText())
        assertEquals(HttpStatusCode.Accepted, deleteResponse.status)

        cleanForeignKeys()
    }

    @Test
    fun testGetNoResults() = testApplication {
        environment { config = ApplicationConfig("application-test.conf") }

        val response = client.get("/result")

        assertEquals("No results found.", response.bodyAsText())
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testGetNoResult() = testApplication {
        environment { config = ApplicationConfig("application-test.conf") }

        val response = client.get("/result/1")

        assertEquals("No result with id 1.", response.bodyAsText())
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testDeleteNoResult() = testApplication {
        environment { config = ApplicationConfig("application-test.conf") }

        val response = client.delete("/result/1")

        assertEquals("No result with id 1.", response.bodyAsText())
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testRoutingNotWorkingForRelease() = testApplication {
        environment { config = ApplicationConfig("application-test-release.conf") }

        // Get all.
        var response = client.get("/result")
        assertTrue(response.bodyAsText().isEmpty())
        assertEquals(HttpStatusCode.NotFound, response.status)

        // Get.
        response = client.get("/result/1")
        assertTrue(response.bodyAsText().isEmpty())
        assertEquals(HttpStatusCode.NotFound, response.status)

        // Add.
        response = client.post("/result")
        assertTrue(response.bodyAsText().isEmpty())
        assertEquals(HttpStatusCode.NotFound, response.status)

        // Update.
        response = client.post("/result/1")
        assertTrue(response.bodyAsText().isEmpty())
        assertEquals(HttpStatusCode.NotFound, response.status)

        // Delete.
        response = client.delete("/result/1")
        assertTrue(response.bodyAsText().isEmpty())
        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}