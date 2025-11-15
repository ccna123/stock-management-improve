package com.example.sol_denka_stockmanagement.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.InventoryTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryTaskDao: IDao<InventoryTaskEntity> {

    @Query("SELECT * FROM InventoryTask")
    override fun get(): Flow<List<InventoryTaskEntity>>

    @Insert
    override suspend fun insert(e: InventoryTaskEntity)

    @Update
    override suspend fun update(e: InventoryTaskEntity)

    @Delete
    override suspend fun delete(e: InventoryTaskEntity)
}