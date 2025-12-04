package com.example.sol_denka_stockmanagement.database.dao.inventory

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.inventory.InventoryResultTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryResultTypeDao: IDao<InventoryResultTypeEntity> {

    @Query("SELECT * FROM InventoryResultType")
    override fun get(): Flow<List<InventoryResultTypeEntity>>

    @Query("SELECT inventory_result_type_id FROM InventoryResultType WHERE inventory_result_code = :inventoryResultCode")
    suspend fun getInventoryResultTypeIdByCode(inventoryResultCode: String): Int

    @Insert
    override suspend fun insert(e: InventoryResultTypeEntity)

    @Update
    override suspend fun update(e: InventoryResultTypeEntity)

    @Delete
    override suspend fun delete(e: InventoryResultTypeEntity)
}