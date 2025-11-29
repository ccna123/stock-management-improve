package com.example.sol_denka_stockmanagement.database.dao.inventory

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.inventory.InventoryResultLocalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryResultLocalDao: IDao<InventoryResultLocalEntity> {

    @Query("SELECT * FROM InventoryResultLocal")
    override fun get(): Flow<List<InventoryResultLocalEntity>>

    @Insert
    override suspend fun insert(e: InventoryResultLocalEntity)

    @Update
    override suspend fun update(e: InventoryResultLocalEntity)

    @Delete
    override suspend fun delete(e: InventoryResultLocalEntity)
}