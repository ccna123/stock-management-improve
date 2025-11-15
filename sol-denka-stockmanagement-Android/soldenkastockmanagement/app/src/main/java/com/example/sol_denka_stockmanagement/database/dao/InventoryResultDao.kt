package com.example.sol_denka_stockmanagement.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.InventoryResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryResultDao: IDao<InventoryResultEntity> {

    @Query("SELECT * FROM InventoryResult")
    override fun get(): Flow<List<InventoryResultEntity>>

    @Insert
    override suspend fun insert(e: InventoryResultEntity)

    @Update
    override suspend fun update(e: InventoryResultEntity)

    @Delete
    override suspend fun delete(e: InventoryResultEntity)
}