package com.example.sol_denka_stockmanagement.database.dao.item

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.sol_denka_stockmanagement.data.local.entity.item.ItemUnitMasterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemUnitDao {

    @Query("SELECT * FROM ItemUnitMaster")
    fun get(): Flow<List<com.example.sol_denka_stockmanagement.data.local.entity.item.ItemUnitMasterEntity>>

    @Query("SELECT COUNT(*) FROM ItemUnitMaster")
    suspend fun countRecord(): Int

    @Insert(onConflict = REPLACE)
    suspend fun insert(e: com.example.sol_denka_stockmanagement.data.local.entity.item.ItemUnitMasterEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(e: List<com.example.sol_denka_stockmanagement.data.local.entity.item.ItemUnitMasterEntity>)

    @Update
    suspend fun update(e: com.example.sol_denka_stockmanagement.data.local.entity.item.ItemUnitMasterEntity)

    @Delete
    suspend fun delete(e: com.example.sol_denka_stockmanagement.data.local.entity.item.ItemUnitMasterEntity)

    @Query("DELETE FROM ItemUnitMaster")
    suspend fun deleteAll()

    @Upsert
    suspend fun upsertAll(e: List<com.example.sol_denka_stockmanagement.data.local.entity.item.ItemUnitMasterEntity>)
}