package com.example.sol_denka_stockmanagement.database.dao.field

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.sol_denka_stockmanagement.database.entity.field.FieldMasterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FieldMasterDao {

    @Query("SELECT * FROM FieldMaster")
    fun get(): Flow<List<FieldMasterEntity>>

    @Query("SELECT COUNT(*) FROM FieldMaster")
    suspend fun countRecord(): Int

    @Insert
    suspend fun insert(e: FieldMasterEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(e: List<FieldMasterEntity>)


    @Update
    suspend fun update(e: FieldMasterEntity)

    @Delete
    suspend fun delete(e: FieldMasterEntity)

    @Query("DELETE FROM FieldMaster")
    suspend fun deleteAll()

    @Upsert
    suspend fun upsertAll(e: List<FieldMasterEntity>)
}
