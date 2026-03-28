package com.example.sol_denka_stockmanagement.data.local.repository.ledger

import com.example.sol_denka_stockmanagement.data.local.mapper.toEntity
import com.example.sol_denka_stockmanagement.data.local.mapper.toModel
import com.example.sol_denka_stockmanagement.database.dao.leger.LedgerItemDao
import com.example.sol_denka_stockmanagement.domain.model.ledger.LedgerItemModel
import com.example.sol_denka_stockmanagement.domain.repository.ledger.ILedgerItemRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LedgerItemRepositoryImpl @Inject constructor(
    private val dao: LedgerItemDao
) : ILedgerItemRepository {
    override fun get() = dao.get().map { it.map { e -> e.toModel() } }
    override suspend fun countRecord() = dao.countRecord()
    override fun getMappedTagIdsFlow() = dao.getMappedTagIdsFlow()
    override suspend fun insert(model: LedgerItemModel) = dao.insert(model.toEntity())
    override suspend fun update(model: LedgerItemModel) = dao.update(model.toEntity())
    override suspend fun delete(model: LedgerItemModel) = dao.delete(model.toEntity())
    override suspend fun upsertAll(models: List<LedgerItemModel>) = dao.upsertAll(models.map { it.toEntity() })
}