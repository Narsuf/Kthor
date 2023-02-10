package com.jorgedguezm.plugins

import com.jorgedguezm.Constants.DEBUG
import com.jorgedguezm.routes.electionRouting
import com.jorgedguezm.routes.partyRouting
import com.jorgedguezm.routes.resultRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(build: String) {
    routing {
        electionRouting(build)
        if (build == DEBUG) partyRouting()
        if (build == DEBUG) resultRouting()
    }
}
