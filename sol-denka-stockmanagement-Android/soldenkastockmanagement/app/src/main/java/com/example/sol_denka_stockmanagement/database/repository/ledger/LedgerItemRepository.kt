package com.example.sol_denka_stockmanagement.database.repository.ledger

import com.example.sol_denka_stockmanagement.database.dao.leger.LedgerItemDao
import com.example.sol_denka_stockmanagement.model.ledger.LedgerItemModel
import com.example.sol_denka_stockmanagement.model.ledger.toEntity
import com.example.sol_denka_stockmanagement.model.ledger.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LedgerItemRepository @Inject constructor(
    private val dao: LedgerItemDao
) {
    fun get(): Flow<List<LedgerItemModel>> = dao.get().map { entityList ->
        entityList.map { it.toModel() }
    }

    suspend fun insert(model: LedgerItemModel) = dao.insert(model.toEntity())
    suspend fun update(model: LedgerItemModel) = dao.update(model.toEntity())
    suspend fun delete(model: LedgerItemModel) = dao.delete(model.toEntity())

    suspend fun replaceAll(models: List<LedgerItemModel>) {
        dao.replaceAll(models.map { it.toEntity() })
    }
}
