package org.n27.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.n27.Constants.DEBUG
import org.n27.dao.DAOElection
import org.n27.dao.DAOFacade
import org.n27.models.Election
import org.n27.models.wrappers.ElectionList

fun Route.electionRouting(build: String) {

    val dao: DAOFacade<Election> = DAOElection()

    route("/election") {
        get {
            val elections = dao.all()

            if (elections.isNotEmpty()) {
                call.respond(ElectionList(elections))
            } else {
                call.respondText("No elections found.", status = HttpStatusCode.NotFound)
            }
        }

        get("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()

            val election = dao.get(id) ?: return@get call.respondText(
                "No election with id $id.",
                status = HttpStatusCode.NotFound
            )

            call.respond(election)
        }

        if (build == DEBUG) {
            post {
                val election = call.receive<Election>()
                dao.add(election)?.let { call.respondRedirect("/election/${it.id}") }
            }

            post("{id}") {
                val id = call.parameters.getOrFail<Int>("id").toInt()
                val election = call.receive<Election>().apply { this.id = id }
                dao.edit(election)
                call.respondRedirect("/election/$id")
            }

            delete("{id}") {
                val id = call.parameters.getOrFail<Int>("id").toInt()

                if (dao.delete(id)) {
                    call.respondText("Election removed correctly.", status = HttpStatusCode.Accepted)
                } else {
                    call.respondText("No election with id $id.", status = HttpStatusCode.NotFound)
                }
            }
        }
    }
}