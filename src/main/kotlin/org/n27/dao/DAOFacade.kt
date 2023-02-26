package org.n27.dao

interface DAOFacade<T> {
    suspend fun all(): List<T>
    suspend fun get(id: Int): T?
    suspend fun add(param: T): T?
    suspend fun edit(param: T): Boolean
    suspend fun delete(id: Int): Boolean
}