package com.jorgedguezm.dao

import com.jorgedguezm.DatabaseFactory.dbQuery
import com.jorgedguezm.dao.DAOParty.Companion.getParty
import com.jorgedguezm.dao.DAOParty.Companion.insertParty
import com.jorgedguezm.models.Result
import com.jorgedguezm.models.Results
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DAOResult : DAOFacade<Result> {

    companion object {
        internal fun resultRowToResult(row: ResultRow) = Result(
            id = row[Results.id],
            partyId = row[Results.partyId],
            electionId = row[Results.electionId],
            elects = row[Results.elects],
            votes = row[Results.votes],
            party = getParty(row[Results.partyId])
        )

        internal fun insertResult(result: Result): Result? {
            val partyId = if (result.partyId == 0 && result.party != null)
                insertParty(result.party)?.id
            else
                result.partyId

            val insertStatement = Results.insert {
                it[Results.partyId] = partyId ?: 0
                it[electionId] = result.electionId
                it[elects] = result.elects
                it[votes] = result.votes
            }

            return insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToResult)
        }
    }

    override suspend fun all(): List<Result> = dbQuery { Results.selectAll().map(::resultRowToResult) }
    override suspend fun delete(id: Int): Boolean = dbQuery { Results.deleteWhere { Results.id eq id } > 0 }
    override suspend fun add(param: Result): Result? = dbQuery { insertResult(param) }

    override suspend fun get(id: Int): Result? = dbQuery {
        Results
            .select { Results.id eq id }
            .map(::resultRowToResult)
            .singleOrNull()
    }

    override suspend fun edit(param: Result): Boolean = dbQuery {
        Results.update({ Results.id eq param.id }) {
            it[elects] = param.elects
            it[votes] = param.votes
        } > 0
    }
}