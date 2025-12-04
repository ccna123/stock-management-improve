package com.example.sol_denka_stockmanagement.database.dao.inventory

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.inventory.InventorySessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventorySessionDao: IDao<InventorySessionEntity> {

    @Query("SELECT * FROM InventorySession")
    override fun get(): Flow<List<InventorySessionEntity>>

    @Insert
    override suspend fun insert(e: InventorySessionEntity): Long

    @Update
    override suspend fun update(e: InventorySessionEntity)

    @Delete
    override suspend fun delete(e: InventorySessionEntity)
}