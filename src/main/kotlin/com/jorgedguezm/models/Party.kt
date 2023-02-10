package com.jorgedguezm.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Party(
    var id: Int = 0,
    val name: String,
    val color: String
)

object Parties : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)
    val color = varchar("color", 128)

    override val primaryKey = PrimaryKey(id)
}