package com.jorgedguezm

import com.jorgedguezm.models.Elections
import com.jorgedguezm.models.Parties
import com.jorgedguezm.models.Results
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(dbDriver: String, dbUrl: String, credentials: Pair<String, String>) {
        val database = Database.connect(dbUrl, dbDriver, user = credentials.first, password = credentials.second)

        transaction(database) {
            runCatching {
                SchemaUtils.create(Elections)
                SchemaUtils.create(Parties)
                SchemaUtils.create(Results)
                println("Tables created")
            }.getOrElse {
                println("Error while trying to create tables: ${it.message}")
            }
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
}