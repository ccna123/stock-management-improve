package com.example.sol_denka_stockmanagement.database.dao.winder

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.sol_denka_stockmanagement.database.entity.winder.WinderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WinderDao {

    @Query("SELECT * FROM Winder")
    fun get(): Flow<List<WinderEntity>>

    @Query("SELECT COUNT(*) FROM Winder")
    suspend fun countRecord(): Int

    @Query("SELECT winder_id FROM Winder WHERE winder_name = :winderName")
    suspend fun getIdByName(winderName: String): Int?

    @Insert
    suspend fun insert(e: WinderEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(e: List<WinderEntity>)

    @Update
    suspend fun update(e: WinderEntity)

    @Delete
    suspend fun delete(e: WinderEntity)

    @Query("DELETE FROM Winder")
    suspend fun deleteAll()

    @Upsert
    suspend fun upsertAll(e: List<WinderEntity>)

    @Transaction
    suspend fun replaceAll(e: List<WinderEntity>) {
        deleteAll()
        insertAll(e)
    }

}