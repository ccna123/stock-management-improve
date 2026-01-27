package com.example.sol_denka_stockmanagement.database.dao.tag

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.tag.TagStatusMasterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagStatusMasterDao {

    @Query("SELECT * FROM TagStatusMaster")
    fun get(): Flow<List<TagStatusMasterEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insert(e: TagStatusMasterEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(e: List<TagStatusMasterEntity>)

    @Update
    suspend fun update(e: TagStatusMasterEntity)

    @Delete
    suspend fun delete(e: TagStatusMasterEntity)

    @Query("DELETE FROM TagStatusMaster")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(e: List<TagStatusMasterEntity>) {
        deleteAll()
        insertAll(e)
    }



}