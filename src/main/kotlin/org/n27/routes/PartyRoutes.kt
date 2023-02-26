package org.n27.routes

import org.n27.dao.DAOFacade
import org.n27.dao.DAOParty
import org.n27.models.Party
import org.n27.models.wrappers.PartyList
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.partyRouting() {

    val dao: DAOFacade<Party> = DAOParty()

    route("/party") {
        get {
            val parties = dao.all()

            if (parties.isNotEmpty()) {
                call.respond(PartyList(parties))
            } else {
                call.respondText("No parties found.", status = HttpStatusCode.NotFound)
            }
        }

        get("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()

            val party = dao.get(id) ?: return@get call.respondText(
                "No party with id $id.",
                status = HttpStatusCode.NotFound
            )

            call.respond(party)
        }

        post {
            val party = call.receive<Party>()
            dao.add(party)?.let { call.respondRedirect("/party/${it.id}") }
        }

        post("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val party = call.receive<Party>().apply { this.id = id }
            dao.edit(party)
            call.respondRedirect("/party/$id")
        }

        delete("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()

            if (dao.delete(id)) {
                call.respondText("Party removed correctly.", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("No party with id $id.", status = HttpStatusCode.NotFound)
            }
        }
    }
}