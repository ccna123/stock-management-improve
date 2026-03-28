package com.example.sol_denka_stockmanagement.database.dao.field

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.sol_denka_stockmanagement.data.local.entity.field.FieldMasterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FieldMasterDao {

    @Query("SELECT * FROM FieldMaster")
    fun get(): Flow<List<com.example.sol_denka_stockmanagement.data.local.entity.field.FieldMasterEntity>>

    @Query("SELECT COUNT(*) FROM FieldMaster")
    suspend fun countRecord(): Int

    @Insert
    suspend fun insert(e: com.example.sol_denka_stockmanagement.data.local.entity.field.FieldMasterEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(e: List<com.example.sol_denka_stockmanagement.data.local.entity.field.FieldMasterEntity>)


    @Update
    suspend fun update(e: com.example.sol_denka_stockmanagement.data.local.entity.field.FieldMasterEntity)

    @Delete
    suspend fun delete(e: com.example.sol_denka_stockmanagement.data.local.entity.field.FieldMasterEntity)

    @Query("DELETE FROM FieldMaster")
    suspend fun deleteAll()

    @Upsert
    suspend fun upsertAll(e: List<com.example.sol_denka_stockmanagement.data.local.entity.field.FieldMasterEntity>)
}
