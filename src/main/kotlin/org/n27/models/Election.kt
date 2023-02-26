package org.n27.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Election(
    var id: Int = 0,
    val date: String,
    val name: String,
    val place: String,
    val chamberName: String,
    val totalElects: Int,
    val scrutinized: Float,
    val validVotes: Int,
    val abstentions: Int,
    val blankVotes: Int,
    val nullVotes: Int,
    val results: List<Result> = listOf()
)

object Elections : Table() {
    val id = integer("id").autoIncrement()
    val date = varchar("date", 128)
    val name = varchar("name", 128)
    val place = varchar("body", 128)
    val chamberName = varchar("chamber_name", 128)
    val totalElects = integer("total_elects")
    val scrutinized = float("scrutinized")
    val validVotes = integer("valid_votes")
    val abstentions = integer("abstentions")
    val blankVotes = integer("blank_votes")
    val nullVotes = integer("null_votes")

    override val primaryKey = PrimaryKey(id)
}