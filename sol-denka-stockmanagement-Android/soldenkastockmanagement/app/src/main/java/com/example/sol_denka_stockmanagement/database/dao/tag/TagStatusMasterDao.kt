package com.example.sol_denka_stockmanagement.database.dao.tag

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.tag.TagStatusMasterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagStatusMasterDao {

    @Query("SELECT * FROM TagStatusMaster")
    fun get(): Flow<List<TagStatusMasterEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insert(e: TagStatusMasterEntity): Long

    @Update
    suspend fun update(e: TagStatusMasterEntity)

    @Delete
    suspend fun delete(e: TagStatusMasterEntity)


}