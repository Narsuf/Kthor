package org.n27.dao

import org.n27.DatabaseFactory.dbQuery
import org.n27.models.Parties
import org.n27.models.Party
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DAOParty : DAOFacade<Party> {

    companion object {
        private fun resultRowToParty(row: ResultRow) = Party(
            id = row[Parties.id],
            name = row[Parties.name],
            color = row[Parties.color]
        )

        internal fun getParty(id: Int): Party? {
            return Parties
                .select { Parties.id eq id }
                .map(Companion::resultRowToParty)
                .singleOrNull()
        }

        internal fun insertParty(party: Party): Party? {
            val insertStatement = Parties.insert {
                it[name] = party.name
                it[color] = party.color
            }

            return insertStatement.resultedValues?.singleOrNull()?.let(Companion::resultRowToParty)
        }
    }

    override suspend fun all(): List<Party> = dbQuery { Parties.selectAll().map(Companion::resultRowToParty) }
    override suspend fun delete(id: Int): Boolean = dbQuery { Parties.deleteWhere { Parties.id eq id } > 0 }
    override suspend fun add(param: Party): Party? = dbQuery { insertParty(param) }
    override suspend fun get(id: Int): Party? = dbQuery { getParty(id) }
    override suspend fun edit(param: Party): Boolean = dbQuery {
        Parties.update({ Parties.id eq param.id }) {
            it[name] = param.name
            it[color] = param.color
        } > 0
    }

}