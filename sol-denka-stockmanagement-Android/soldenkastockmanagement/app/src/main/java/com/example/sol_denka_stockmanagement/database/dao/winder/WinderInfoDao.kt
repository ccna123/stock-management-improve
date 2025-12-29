package com.example.sol_denka_stockmanagement.database.dao.winder

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.winder.WinderInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WinderInfoDao {

    @Query("SELECT * FROM WinderInfo")
    fun get(): Flow<List<WinderInfoEntity>>

    @Query("SELECT winder_id FROM WinderInfo WHERE winder_name = :winderName")
    suspend fun getIdByName(winderName: String): Int?

    @Insert
    suspend fun insert(e: WinderInfoEntity): Long

    @Update
    suspend fun update(e: WinderInfoEntity)

    @Delete
    suspend fun delete(e: WinderInfoEntity)
}