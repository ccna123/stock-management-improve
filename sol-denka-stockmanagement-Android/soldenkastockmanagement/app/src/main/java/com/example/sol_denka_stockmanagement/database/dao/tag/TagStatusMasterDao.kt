package com.example.sol_denka_stockmanagement.database.dao.tag

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.sol_denka_stockmanagement.data.local.entity.tag.TagStatusMasterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagStatusMasterDao {

    @Query("SELECT * FROM TagStatusMaster")
    fun get(): Flow<List<com.example.sol_denka_stockmanagement.data.local.entity.tag.TagStatusMasterEntity>>

    @Query("SELECT COUNT(*) FROM TagStatusMaster")
    suspend fun countRecord(): Int

    @Insert(onConflict = REPLACE)
    suspend fun insert(e: com.example.sol_denka_stockmanagement.data.local.entity.tag.TagStatusMasterEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(e: List<com.example.sol_denka_stockmanagement.data.local.entity.tag.TagStatusMasterEntity>)

    @Update
    suspend fun update(e: com.example.sol_denka_stockmanagement.data.local.entity.tag.TagStatusMasterEntity)

    @Delete
    suspend fun delete(e: com.example.sol_denka_stockmanagement.data.local.entity.tag.TagStatusMasterEntity)

    @Query("DELETE FROM TagStatusMaster")
    suspend fun deleteAll()

    @Upsert
    suspend fun upsertAll(e: List<com.example.sol_denka_stockmanagement.data.local.entity.tag.TagStatusMasterEntity>)
}