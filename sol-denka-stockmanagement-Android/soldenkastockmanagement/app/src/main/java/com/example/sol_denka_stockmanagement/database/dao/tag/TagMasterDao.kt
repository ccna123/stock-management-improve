package com.example.sol_denka_stockmanagement.database.dao.tag

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.tag.TagMasterEntity
import com.example.sol_denka_stockmanagement.model.tag.SingleTagInfoModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TagMasterDao : IDao<TagMasterEntity> {

    @Query("SELECT * FROM TagMaster")
    override fun get(): Flow<List<TagMasterEntity>>

    @Insert(onConflict = REPLACE)
    override suspend fun insert(e: TagMasterEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(e: List<TagMasterEntity>)

    @Update
    override suspend fun update(e: TagMasterEntity)

    @Delete
    override suspend fun delete(e: TagMasterEntity)

    @Query("DELETE FROM tagmaster")
    suspend fun deleteAll()

    @Query(
        """
    SELECT * FROM tagmaster t
    LEFT JOIN ledgeritem le ON le.tag_id = t.tag_id
    WHERE le.location_id = :locationId AND le.is_in_stock = :isInStock
    """
    )
    suspend fun getTagsByLocationAndStock(locationId: Int, isInStock: Boolean): List<TagMasterEntity>

    @Query("SELECT * FROM tagmaster WHERE epc = :epc")
    suspend fun getTagIdLedgerIdByEpc(epc: String): TagMasterEntity

    @Query("""
        SELECT 
                t.tag_id AS tagId, 
                t.epc , 
                le.is_in_stock AS isInStock,
                IFNULL(it.item_type_name, '') AS itemName,
                IFNULL(it.item_type_code, '') AS itemCode,
                IFNULL(lo.location_name, '') AS location
            FROM tagmaster AS t 
            LEFT JOIN ledgeritem le ON le.tag_id = t.tag_id
            LEFT JOIN locationmaster lo ON lo.location_id = le.location_id
            LEFT JOIN itemtypemaster it ON it.item_type_id = le.item_type_id
    """)
    suspend fun getFullInfo(): List<SingleTagInfoModel>

    @Query("SELECT ledger_item_id FROM ledgeritem WHERE tag_id = :tagId")
    suspend fun getLedgerIdByTagId(tagId: Int): Int

    @Query("""
        SELECT le.item_type_id
            FROM tagmaster t
            LEFT JOIN ledgeritem le ON le.tag_id = t.tag_id
            WHERE t.tag_id = :tagId
    """)
    suspend fun getItemTypeIdByTagId(tagId: Int): Long

    @Query("""
        SELECT le.location_id
            FROM tagmaster t
            LEFT JOIN ledgeritem le ON le.tag_id = t.tag_id
            WHERE t.tag_id = :tagId
    """)
    suspend fun getLocationIdByTagId(tagId: Int): Long

    @Transaction
    suspend fun replaceAll(e: List<TagMasterEntity>){
        deleteAll()
        insertAll(e)
    }

}