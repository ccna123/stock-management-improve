package com.example.sol_denka_stockmanagement.database.dao.item

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.item.ItemTypeMasterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemTypeDao: IDao<ItemTypeMasterEntity> {

    @Query("SELECT * FROM ItemTypeMaster")
    override fun get(): Flow<List<ItemTypeMasterEntity>>

    @Insert(onConflict = REPLACE)
    override suspend fun insert(e: ItemTypeMasterEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(e: List<ItemTypeMasterEntity>)

    @Update
    override suspend fun update(e: ItemTypeMasterEntity)

    @Delete
    override suspend fun delete(e: ItemTypeMasterEntity)

    @Query("SELECT item_type_name FROM ItemTypeMaster WHERE item_type_name LIKE '%' || :keyword || '%'")
    suspend fun findByName(keyword: String): List<String>?
}