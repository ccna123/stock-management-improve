package com.example.sol_denka_stockmanagement.database.dao.location

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.sol_denka_stockmanagement.database.entity.location.LocationMasterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Query("SELECT * FROM LocationMaster")
    fun get(): Flow<List<LocationMasterEntity>>

    @Query("SELECT COUNT(*) FROM LocationMaster")
    suspend fun countRecord(): Int

    @Insert(onConflict = REPLACE)
    suspend fun insert(e: LocationMasterEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(e: List<LocationMasterEntity>)

    @Update
    suspend fun update(e: LocationMasterEntity)

    @Delete
    suspend fun delete(e: LocationMasterEntity)

    @Query("DELETE FROM LocationMaster")
    suspend fun deleteAll()

    @Query("SELECT location_id FROM LocationMaster WHERE location_name = :locationName")
    suspend fun getLocationIdByName(locationName: String): Int

    @Upsert
    suspend fun upsertAll(e: List<LocationMasterEntity>)
}