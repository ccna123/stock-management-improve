package com.example.sol_denka_stockmanagement.database.repository.process

import com.example.sol_denka_stockmanagement.database.dao.process.ProcessTypeDao
import com.example.sol_denka_stockmanagement.database.entity.process.ProcessTypeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProcessTypeRepository @Inject constructor(
    private val dao: ProcessTypeDao
) {
    fun get(): Flow<List<ProcessTypeEntity>> = dao.get()
    suspend fun insert(e: ProcessTypeEntity) = dao.insert(e)
    suspend fun update(e: ProcessTypeEntity) = dao.update(e)
    suspend fun delete(e: ProcessTypeEntity) = dao.delete(e)
}
