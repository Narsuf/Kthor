package org.n27.plugins

import org.n27.Constants.DEBUG
import org.n27.routes.electionRouting
import org.n27.routes.partyRouting
import org.n27.routes.resultRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(build: String) {
    routing {
        electionRouting(build)
        if (build == DEBUG) partyRouting()
        if (build == DEBUG) resultRouting()
    }
}
