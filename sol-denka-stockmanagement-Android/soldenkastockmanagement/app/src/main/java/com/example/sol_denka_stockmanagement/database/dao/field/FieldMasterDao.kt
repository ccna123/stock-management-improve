package com.example.sol_denka_stockmanagement.database.dao.field

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.field.FieldMasterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FieldMasterDao {

    @Query("SELECT * FROM FieldMaster")
    fun get(): Flow<List<FieldMasterEntity>>

    @Insert
    suspend fun insert(e: FieldMasterEntity): Long

    @Update
    suspend fun update(e: FieldMasterEntity)

    @Delete
    suspend fun delete(e: FieldMasterEntity)
}
