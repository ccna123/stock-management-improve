package com.example.sol_denka_stockmanagement.database.dao.location

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.location.LocationMasterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao : IDao<LocationMasterEntity> {

    @Query("SELECT * FROM LocationMaster")
    override fun get(): Flow<List<LocationMasterEntity>>

    @Insert
    override suspend fun insert(e: LocationMasterEntity)

    @Update
    override suspend fun update(e: LocationMasterEntity)

    @Delete
    override suspend fun delete(e: LocationMasterEntity)
}