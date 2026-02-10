package com.example.sol_denka_stockmanagement.database.dao.inventory

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.inventory.InventoryDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDetailDao {

    @Query("SELECT * FROM InventoryDetail")
    fun get(): Flow<List<InventoryDetailEntity>>

    @Insert
    suspend fun insert(e: InventoryDetailEntity): Long

    @Update
    suspend fun update(e: InventoryDetailEntity)

    @Delete
    suspend fun delete(e: InventoryDetailEntity)
}