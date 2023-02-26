package com.jorgedguezm

import com.jorgedguezm.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    DatabaseFactory.init(
        dbDriver = environment.config.property("ktor.deployment.dbDriver").getString(),
        dbUrl = environment.config.property("ktor.deployment.dbUrl").getString(),
        credentials = Pair(
            environment.config.property("ktor.deployment.user").getString(),
            environment.config.property("ktor.deployment.password").getString()
        )
    )
    configureSerialization()
    configureRouting(environment.config.property("ktor.deployment.build").getString())
}
