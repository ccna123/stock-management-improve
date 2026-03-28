package com.example.sol_denka_stockmanagement.database.dao.process

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.sol_denka_stockmanagement.data.local.entity.process.ProcessTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProcessTypeDao {

    @Query("SELECT * FROM ProcessType")
    fun get(): Flow<List<com.example.sol_denka_stockmanagement.data.local.entity.process.ProcessTypeEntity>>

    @Query("SELECT COUNT(*) FROM ProcessType")
    suspend fun countRecord(): Int

    @Query("SELECT process_type_id FROM ProcessType WHERE process_name LIKE '%' || :processTypeName || '%'")
    suspend fun getIdByName(processTypeName: String): Int

    @Insert(onConflict = REPLACE)
    suspend fun insert(e: com.example.sol_denka_stockmanagement.data.local.entity.process.ProcessTypeEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(e: List<com.example.sol_denka_stockmanagement.data.local.entity.process.ProcessTypeEntity>)

    @Update
    suspend fun update(e: com.example.sol_denka_stockmanagement.data.local.entity.process.ProcessTypeEntity)

    @Delete
    suspend fun delete(e: com.example.sol_denka_stockmanagement.data.local.entity.process.ProcessTypeEntity)

    @Query("DELETE FROM ProcessType")
    suspend fun deleteAll()

    @Upsert
    suspend fun upsertAll(e: List<com.example.sol_denka_stockmanagement.data.local.entity.process.ProcessTypeEntity>)
}