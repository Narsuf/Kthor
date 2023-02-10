package com.jorgedguezm.dao

import com.jorgedguezm.DatabaseFactory.dbQuery
import com.jorgedguezm.dao.DAOResult.Companion.insertResult
import com.jorgedguezm.dao.DAOResult.Companion.resultRowToResult
import com.jorgedguezm.models.Election
import com.jorgedguezm.models.Elections
import com.jorgedguezm.models.Results
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DAOElection : DAOFacade<Election> {

    private fun resultRowToElection(row: ResultRow) = Election(
        id = row[Elections.id],
        date = row[Elections.date],
        name = row[Elections.name],
        place = row[Elections.place],
        chamberName = row[Elections.chamberName],
        totalElects = row[Elections.totalElects],
        scrutinized = row[Elections.scrutinized],
        validVotes = row[Elections.validVotes],
        abstentions = row[Elections.abstentions],
        blankVotes = row[Elections.blankVotes],
        nullVotes = row[Elections.nullVotes],
        results = Results
            .select { Results.electionId eq row[Elections.id] }
            .map(::resultRowToResult),
    )

    override suspend fun all(): List<Election> = dbQuery { Elections.selectAll().map(::resultRowToElection) }
    override suspend fun delete(id: Int): Boolean = dbQuery { Elections.deleteWhere { Elections.id eq id } > 0 }

    override suspend fun add(param: Election): Election? = dbQuery {
        val insertStatement = Elections.insert {
            it[date] = param.date
            it[name] = param.name
            it[place] = param.place
            it[chamberName] = param.chamberName
            it[totalElects] = param.totalElects
            it[scrutinized] = param.scrutinized
            it[validVotes] = param.validVotes
            it[abstentions] = param.abstentions
            it[blankVotes] = param.blankVotes
            it[nullVotes] = param.nullVotes
        }

        val resultRow = insertStatement.resultedValues?.singleOrNull()

        resultRow?.let { row ->
            param.results.takeIf { it.isNotEmpty() }?.forEach { result ->
                insertResult(result.apply { electionId = row[Elections.id] })
            }

            resultRowToElection(row)
        }
    }

    override suspend fun get(id: Int): Election? = dbQuery {
        Elections
            .select { Elections.id eq id }
            .map(::resultRowToElection)
            .singleOrNull()
    }

    override suspend fun edit(param: Election): Boolean = dbQuery {
        Elections.update({ Elections.id eq param.id }) {
            it[date] = param.date
            it[name] = param.name
            it[place] = param.place
            it[chamberName] = param.chamberName
            it[totalElects] = param.totalElects
            it[scrutinized] = param.scrutinized
            it[validVotes] = param.validVotes
            it[abstentions] = param.abstentions
            it[blankVotes] = param.blankVotes
            it[nullVotes] = param.nullVotes
        } > 0
    }
}