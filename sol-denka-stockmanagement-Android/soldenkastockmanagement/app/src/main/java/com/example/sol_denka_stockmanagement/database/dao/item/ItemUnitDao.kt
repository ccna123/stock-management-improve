package com.example.sol_denka_stockmanagement.database.dao.item

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.item.ItemUnitMasterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemUnitDao: IDao<ItemUnitMasterEntity> {

    @Query("SELECT * FROM ItemUnitMaster")
    override fun get(): Flow<List<ItemUnitMasterEntity>>

    @Insert
    override suspend fun insert(e: ItemUnitMasterEntity)

    @Update
    override suspend fun update(e: ItemUnitMasterEntity)

    @Delete
    override suspend fun delete(e: ItemUnitMasterEntity)
}