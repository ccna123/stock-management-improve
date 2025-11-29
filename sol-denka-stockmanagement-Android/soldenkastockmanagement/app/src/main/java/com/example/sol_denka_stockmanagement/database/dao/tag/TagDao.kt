package com.example.sol_denka_stockmanagement.database.dao.tag

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.tag.TagMasterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao: IDao<TagMasterEntity> {

    @Query("SELECT * FROM TagMaster")
    override fun get(): Flow<List<TagMasterEntity>>

    @Insert
    override suspend fun insert(e: TagMasterEntity)

    @Update
    override suspend fun update(e: TagMasterEntity)

    @Delete
    override suspend fun delete(e: TagMasterEntity)
}