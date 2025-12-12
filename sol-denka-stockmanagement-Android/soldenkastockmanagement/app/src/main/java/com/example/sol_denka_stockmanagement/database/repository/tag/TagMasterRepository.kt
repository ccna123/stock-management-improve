package com.example.sol_denka_stockmanagement.database.repository.tag

import com.example.sol_denka_stockmanagement.database.dao.tag.TagMasterDao
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import com.example.sol_denka_stockmanagement.model.tag.toEntity
import com.example.sol_denka_stockmanagement.model.tag.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagMasterRepository @Inject constructor(
    private val dao: TagMasterDao
) {

    fun get(): Flow<List<TagMasterModel>> = dao.get().map { list -> list.map { it.toModel() } }

    suspend fun getFullInfo() = dao.getFullInfo()

    suspend fun getItemTypeIdLocationIdByTagId(tagId: Int): Pair<Int, Int>{
        val itemTypeId = dao.getItemTypeIdByTagId(tagId).toInt()
        val locationId = dao.getLocationIdByTagId(tagId).toInt()
        return Pair(itemTypeId, locationId)
    }
    suspend fun getTagsByLocationAndStock(locationId: Int, isInStock: Boolean) =
        dao.getTagsByLocationAndStock(locationId, isInStock).map { it.toModel() }

    suspend fun getTagIdLedgerIdByEpc(epc: String): Pair<Int, Int?>{
        val t = dao.getTagIdLedgerIdByEpc(epc)
        return Pair(t.tagId, t.ledgerItemId)
    }

    suspend fun getLedgerIdByEpc(epc: String): Int?{
        return dao.getLedgerIdByEpc(epc)
    }


    suspend fun insert(model: TagMasterModel) = dao.insert(model.toEntity())
    suspend fun insertAll(models: List<TagMasterModel>) =
        dao.insertAll(models.map { it.toEntity() })

    suspend fun update(model: TagMasterModel) = dao.update(model.toEntity())
    suspend fun delete(model: TagMasterModel) = dao.delete(model.toEntity())
}