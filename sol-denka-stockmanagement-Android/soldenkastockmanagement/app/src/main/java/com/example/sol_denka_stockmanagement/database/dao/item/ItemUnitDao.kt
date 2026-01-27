package com.example.sol_denka_stockmanagement.database.dao.item

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.item.ItemUnitMasterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemUnitDao {

    @Query("SELECT * FROM ItemUnitMaster")
    fun get(): Flow<List<ItemUnitMasterEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insert(e: ItemUnitMasterEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(e: List<ItemUnitMasterEntity>)

    @Update
    suspend fun update(e: ItemUnitMasterEntity)

    @Delete
    suspend fun delete(e: ItemUnitMasterEntity)

    @Query("DELETE FROM ItemUnitMaster")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(e: List<ItemUnitMasterEntity>) {
        deleteAll()
        insertAll(e)
    }
}