package org.n27.routes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ElectionRoutesTests {

    private val electionRequest = getStringJson("election_request.json")
    private val electionRequestEdited = getStringJson("election_request_edit.json")
    private val completeElectionRequest = getStringJson("complete_election_request.json")
    private val electionResponse = getStringJson("election_response.json").filter { !it.isWhitespace() }
    private val electionResponseEdited = getStringJson("election_response_edit.json").filter { !it.isWhitespace() }
    private val completeElectionResponse = getStringJson("complete_election_response.json")
        .filter { !it.isWhitespace() }

    private fun getStringJson(path: String) = javaClass.classLoader.getStringJson("election/$path")
    private fun String.toElectionsArray() = "{\"elections\":[$this]}"

    @Test
    fun testPostElection() = testApplication {
        environment { config = ApplicationConfig("application-test.conf") }

        // Add.
        client.post("/election") {
            contentType(ContentType.Application.Json)
            setBody(electionRequest)
        }

        // Get all.
        val getAllResponse = client.get("/election")
        val electionId = getAllResponse.bodyAsText().getId()
        val expected = electionResponse.replaceFirst("0", electionId)

        assertEquals(expected.toElectionsArray(), getAllResponse.bodyAsText())
        assertEquals(HttpStatusCode.OK, getAllResponse.status)

        // Get by id.
        val getResponse = client.get("/election/$electionId")

        assertEquals(expected, getResponse.bodyAsText())
        assertEquals(HttpStatusCode.OK, getResponse.status)

        // Delete.
        client.delete("/election/$electionId")

        val getResponseAfterDelete = client.get("/election")
        assertEquals("No elections found.", getResponseAfterDelete.bodyAsText())
        assertEquals(HttpStatusCode.NotFound, getResponseAfterDelete.status)
    }

    @Test
    fun testPostCompleteElection() = testApplication {
        environment { config = ApplicationConfig("application-test.conf") }

        // Add.
        client.post("/election") {
            contentType(ContentType.Application.Json)
            setBody(completeElectionRequest)
        }

        // Get all.
        val getAllResponse = client.get("/election")
        val bodyAsText = getAllResponse.bodyAsText()
        val electionId = bodyAsText.getId()
        val resultId = bodyAsText.split(":")[14].split(",")[0]
        val partyId = bodyAsText.split(":")[15].split(",")[0]
        val expected = completeElectionResponse
            .replace("-1", electionId)
            .replaceFirst("-2", resultId)
            .replace("-3", partyId)

        assertEquals(expected.toElectionsArray(), getAllResponse.bodyAsText())
        assertEquals(HttpStatusCode.OK, getAllResponse.status)

        // Clean.
        client.delete("/result/$resultId")
        client.delete("/party/$partyId")
        client.delete("/election/$electionId")
    }

    @Test
    fun testUpdateElection() = testApplication {
        environment { config = ApplicationConfig("application-test.conf") }

        // Add.
        client.post("/election") {
            contentType(ContentType.Application.Json)
            setBody(electionRequest)
        }

        val response = client.get("/election")
        val electionId = response.bodyAsText().getId()
        val expected = electionResponseEdited.replaceFirst("0", electionId)

        // Update.
        client.post("/election/$electionId") {
            contentType(ContentType.Application.Json)
            setBody(electionRequestEdited)
        }

        val getResponse = client.get("/election/$electionId")
        assertEquals(expected, getResponse.bodyAsText())
        assertEquals(HttpStatusCode.OK, getResponse.status)

        // Delete.
        val deleteResponse = client.delete("/election/$electionId")
        assertEquals("Election removed correctly.", deleteResponse.bodyAsText())
        assertEquals(HttpStatusCode.Accepted, deleteResponse.status)
    }

    @Test
    fun testGetNoElections() = testApplication {
        environment { config = ApplicationConfig("application-test.conf") }

        val response = client.get("/election")

        assertEquals("No elections found.", response.bodyAsText())
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testGetNoElection() = testApplication {
        environment { config = ApplicationConfig("application-test.conf") }

        val response = client.get("/election/1")

        assertEquals("No election with id 1.", response.bodyAsText())
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testDeleteNoElection() = testApplication {
        environment { config = ApplicationConfig("application-test.conf") }

        val response = client.delete("/election/1")

        assertEquals("No election with id 1.", response.bodyAsText())
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testRoutingNotWorkingForRelease() = testApplication {
        environment { config = ApplicationConfig("application-test-release.conf") }

        // Get all works.
        var response = client.get("/election")
        assertEquals("No elections found.", response.bodyAsText())
        assertEquals(HttpStatusCode.NotFound, response.status)

        // Get works.
        response = client.get("/election/1")
        assertEquals("No election with id 1.", response.bodyAsText())
        assertEquals(HttpStatusCode.NotFound, response.status)

        // Add won't work.
        response = client.post("/election")
        assertTrue(response.bodyAsText().isEmpty())
        assertEquals(HttpStatusCode.MethodNotAllowed, response.status)

        // Update won't work.
        response = client.post("/election/1")
        assertTrue(response.bodyAsText().isEmpty())
        assertEquals(HttpStatusCode.NotFound, response.status)

        // Delete won't work.
        response = client.delete("/election/1")
        assertTrue(response.bodyAsText().isEmpty())
        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}