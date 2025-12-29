package com.example.sol_denka_stockmanagement.database.dao.inventory

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.inventory.InventoryResultTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryResultTypeDao {

    @Query("SELECT * FROM InventoryResultType")
    fun get(): Flow<List<InventoryResultTypeEntity>>

    @Query("SELECT inventory_result_type_id FROM InventoryResultType WHERE inventory_result_code = :inventoryResultCode")
    suspend fun getInventoryResultTypeIdByCode(inventoryResultCode: String): Int

    @Insert
    suspend fun insert(e: InventoryResultTypeEntity): Long

    @Update
    suspend fun update(e: InventoryResultTypeEntity)

    @Delete
    suspend fun delete(e: InventoryResultTypeEntity)
}