package com.example.sol_denka_stockmanagement.database.dao.field

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.field.FieldMasterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FieldMasterDao: IDao<FieldMasterEntity> {

    @Query("SELECT * FROM FieldMaster")
    override fun get(): Flow<List<FieldMasterEntity>>

    @Insert
    override suspend fun insert(e: FieldMasterEntity): Long

    @Update
    override suspend fun update(e: FieldMasterEntity)

    @Delete
    override suspend fun delete(e: FieldMasterEntity)
}
