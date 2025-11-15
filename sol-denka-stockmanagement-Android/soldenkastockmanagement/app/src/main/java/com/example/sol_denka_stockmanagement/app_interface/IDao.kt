package com.example.sol_denka_stockmanagement.app_interface

import kotlinx.coroutines.flow.Flow

interface IDao<E> {
    fun get(): Flow<List<E>>
    suspend fun insert(e: E)
    suspend fun update(e: E)
    suspend fun delete(e: E)
}