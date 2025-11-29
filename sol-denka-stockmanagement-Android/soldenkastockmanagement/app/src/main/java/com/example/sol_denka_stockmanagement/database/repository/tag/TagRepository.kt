package com.example.sol_denka_stockmanagement.database.repository.tag

import com.example.sol_denka_stockmanagement.database.dao.tag.TagDao
import com.example.sol_denka_stockmanagement.database.entity.tag.TagMasterEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagRepository @Inject constructor(
    private val dao: TagDao
) {
    fun get(): Flow<List<TagMasterEntity>> = dao.get()
    suspend fun insert(e: TagMasterEntity) = dao.insert(e)
    suspend fun update(e: TagMasterEntity) = dao.update(e)
    suspend fun delete(e: TagMasterEntity) = dao.delete(e)
}
