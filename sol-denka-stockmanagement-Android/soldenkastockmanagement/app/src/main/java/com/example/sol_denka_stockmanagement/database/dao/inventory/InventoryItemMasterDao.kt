package com.example.sol_denka_stockmanagement.database.dao.inventory

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.InventoryItemMasterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryItemMasterDao: IDao<InventoryItemMasterEntity> {

    @Query("SELECT * FROM InventoryItemMaster")
    override fun get(): Flow<List<InventoryItemMasterEntity>>

    @Insert
    override suspend fun insert(e: InventoryItemMasterEntity)

    @Update
    override suspend fun update(e: InventoryItemMasterEntity)

    @Delete
    override suspend fun delete(e: InventoryItemMasterEntity)
}