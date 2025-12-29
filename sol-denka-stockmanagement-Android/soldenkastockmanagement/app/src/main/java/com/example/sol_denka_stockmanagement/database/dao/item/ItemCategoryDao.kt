package com.example.sol_denka_stockmanagement.database.dao.item

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.database.entity.item.ItemCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemCategoryDao {

    @Query("SELECT * FROM ItemCategoryMaster")
    fun get(): Flow<List<ItemCategoryEntity>>

    @Query("SELECT item_category_id FROM ItemCategoryMaster WHERE item_category_name = :name")
    suspend fun getIdByName(name: String): Long

    @Insert(onConflict = REPLACE)
    suspend fun insert(e: ItemCategoryEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(e: List<ItemCategoryEntity>)

    @Update
    suspend fun update(e: ItemCategoryEntity)

    @Delete
    suspend fun delete(e: ItemCategoryEntity)
}