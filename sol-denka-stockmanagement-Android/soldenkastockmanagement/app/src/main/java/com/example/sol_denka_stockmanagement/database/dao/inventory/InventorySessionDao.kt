package com.example.sol_denka_stockmanagement.database.dao.inventory

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.data.local.entity.inventory.InventorySessionEntity
import com.example.sol_denka_stockmanagement.domain.model.session.SessionModel
import kotlinx.coroutines.flow.Flow

@Dao
interface InventorySessionDao {

    @Query("SELECT * FROM InventorySession")
    fun get(): Flow<List<com.example.sol_denka_stockmanagement.data.local.entity.inventory.InventorySessionEntity>>

    @Query("SELECT inventory_session_id AS sessionId, executed_at AS timeStamp FROM InventorySession")
    suspend fun getSession(): List<com.example.sol_denka_stockmanagement.domain.model.session.SessionModel>

    @Insert
    suspend fun insert(e: com.example.sol_denka_stockmanagement.data.local.entity.inventory.InventorySessionEntity): Long

    @Update
    suspend fun update(e: com.example.sol_denka_stockmanagement.data.local.entity.inventory.InventorySessionEntity)

    @Delete
    suspend fun delete(e: com.example.sol_denka_stockmanagement.data.local.entity.inventory.InventorySessionEntity)
}