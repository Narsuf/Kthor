package com.jorgedguezm.routes

import com.jorgedguezm.dao.DAOFacade
import com.jorgedguezm.dao.DAOResult
import com.jorgedguezm.models.Result
import com.jorgedguezm.models.wrappers.ResultList
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.resultRouting() {

    val dao: DAOFacade<Result> = DAOResult()

    route("/result") {
        get {
            val results = dao.all()

            if (results.isNotEmpty()) {
                call.respond(ResultList(results))
            } else {
                call.respondText("No results found.", status = HttpStatusCode.NotFound)
            }
        }

        get("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()

            val result = dao.get(id) ?: return@get call.respondText(
                "No result with id $id.",
                status = HttpStatusCode.NotFound
            )

            call.respond(result)
        }

        post {
            val result = call.receive<Result>()
            dao.add(result)?.let { call.respondRedirect("/result/${it.id}") }
        }

        post("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val result = call.receive<Result>().apply { this.id = id }
            dao.edit(result)
            call.respondRedirect("/result/$id")
        }

        delete("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()

            if (dao.delete(id)) {
                call.respondText("Result removed correctly.", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("No result with id $id.", status = HttpStatusCode.NotFound)
            }
        }
    }
}