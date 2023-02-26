package org.n27.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Result(
    var id: Int = 0,
    val partyId: Int = 0,
    var electionId: Int = 0,
    val elects: Int,
    val votes: Int,
    val party: Party? = null
)

object Results : Table() {
    val id = integer("id").autoIncrement()
    val partyId = reference("party_id", Parties.id)
    val electionId = reference("election_id", Elections.id)
    val elects = integer("elects")
    val votes = integer("votes")

    override val primaryKey = PrimaryKey(id)
}