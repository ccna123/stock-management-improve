package com.example.sol_denka_stockmanagement.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.MaterialMasterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialDao : IDao<MaterialMasterEntity> {

    @Query("SELECT * FROM MaterialMaster")
    override fun get(): Flow<List<MaterialMasterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(e: MaterialMasterEntity)

    @Update
    override suspend fun update(e: MaterialMasterEntity)

    @Delete
    override suspend fun delete(e: MaterialMasterEntity)
}