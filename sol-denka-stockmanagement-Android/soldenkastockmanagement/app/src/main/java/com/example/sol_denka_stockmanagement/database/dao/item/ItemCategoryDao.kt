package com.example.sol_denka_stockmanagement.database.dao.item

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.item.ItemCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemCategoryDao: IDao<ItemCategoryEntity> {

    @Query("SELECT * FROM ItemCategoryMaster")
    override fun get(): Flow<List<ItemCategoryEntity>>

    @Insert(onConflict = REPLACE)
    override suspend fun insert(e: ItemCategoryEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(e: List<ItemCategoryEntity>)

    @Update
    override suspend fun update(e: ItemCategoryEntity)

    @Delete
    override suspend fun delete(e: ItemCategoryEntity)
}