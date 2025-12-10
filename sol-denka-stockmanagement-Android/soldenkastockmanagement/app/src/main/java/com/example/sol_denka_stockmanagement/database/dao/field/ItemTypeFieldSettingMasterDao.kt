package com.example.sol_denka_stockmanagement.database.dao.field

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.field.ItemTypeFieldSettingMasterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemTypeFieldSettingMasterDao: IDao<ItemTypeFieldSettingMasterEntity> {

    @Query("SELECT * FROM ItemTypeFieldSettingMaster")
    override fun get(): Flow<List<ItemTypeFieldSettingMasterEntity>>

    @Insert(onConflict = REPLACE)
    override suspend fun insert(e: ItemTypeFieldSettingMasterEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(e: List<ItemTypeFieldSettingMasterEntity>)

    @Update
    override suspend fun update(e: ItemTypeFieldSettingMasterEntity)

    @Delete
    override suspend fun delete(e: ItemTypeFieldSettingMasterEntity)
}
