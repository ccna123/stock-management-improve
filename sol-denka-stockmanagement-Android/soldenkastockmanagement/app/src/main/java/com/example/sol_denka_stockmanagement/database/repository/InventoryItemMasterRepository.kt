package com.example.sol_denka_stockmanagement.database.repository

import com.example.sol_denka_stockmanagement.constant.TagStatus
import com.example.sol_denka_stockmanagement.database.dao.inventory.InventoryItemMasterDao
import com.example.sol_denka_stockmanagement.model.AdditionalFieldsModel
import com.example.sol_denka_stockmanagement.model.InventoryItemMasterModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryItemMasterRepository @Inject constructor(
    private val inventoryItemMasterDao: InventoryItemMasterDao,
) {

    private val _mockData = MutableStateFlow(
        listOf(
            "80000000", "81111111", "82222222", "83333333",
            "84444444", "85555555", "86666666", "87777777",
            "88888888", "89999999",
        ).mapIndexed { index, epc ->
            InventoryItemMasterModel(
                id = index,
                materialId = 1000 + index,
                locationId = 200 + index,
                itemName = "Item $index",
                epc = epc,
                isPresent = if (index % 2 == 0) 1 else 0,
                status = if (index % 2 == 0) "Available" else "Checked",
                createdAt = "2025-11-12T10:00:00+09:00",
                updatedAt = "2025-11-12T12:00:00+09:00",
                newField = AdditionalFieldsModel(
                    tagStatus = TagStatus.UNPROCESSED,
                    rssi = -100f
                )
            )
        }
    )


    fun get(): Flow<List<InventoryItemMasterModel>> = _mockData

}