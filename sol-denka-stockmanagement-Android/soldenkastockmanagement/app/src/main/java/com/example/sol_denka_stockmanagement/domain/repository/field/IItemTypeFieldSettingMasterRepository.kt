package com.example.sol_denka_stockmanagement.domain.repository.field

import com.example.sol_denka_stockmanagement.domain.model.field.ItemTypeFieldSettingMasterModel
import com.example.sol_denka_stockmanagement.domain.model.inbound.InboundInputFormModel
import kotlinx.coroutines.flow.Flow

interface IItemTypeFieldSettingMasterRepository {
    fun get(): Flow<List<ItemTypeFieldSettingMasterModel>>
    suspend fun countRecord(): Int
    suspend fun getFieldForItemTypeByItemTypeId(id: Int): List<InboundInputFormModel>
    suspend fun insert(model: ItemTypeFieldSettingMasterModel)
    suspend fun update(model: ItemTypeFieldSettingMasterModel)
    suspend fun delete(model: ItemTypeFieldSettingMasterModel)
    suspend fun upsertAll(models: List<ItemTypeFieldSettingMasterModel>)
}