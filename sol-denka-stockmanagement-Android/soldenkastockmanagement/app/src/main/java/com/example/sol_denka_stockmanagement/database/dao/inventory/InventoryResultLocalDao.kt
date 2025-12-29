package com.example.sol_denka_stockmanagement.database.dao.inventory

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.inventory.InventoryResultLocalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryResultLocalDao {

    @Query("SELECT * FROM InventoryResultLocal")
    fun get(): Flow<List<InventoryResultLocalEntity>>

    @Insert
    suspend fun insert(e: InventoryResultLocalEntity): Long

    @Update
    suspend fun update(e: InventoryResultLocalEntity)

    @Delete
    suspend fun delete(e: InventoryResultLocalEntity)
}