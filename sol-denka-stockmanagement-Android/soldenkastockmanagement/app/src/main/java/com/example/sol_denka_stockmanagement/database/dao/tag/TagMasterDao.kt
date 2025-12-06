package com.example.sol_denka_stockmanagement.database.dao.tag

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.dao.location.LocationChangeScanDataTable
import com.example.sol_denka_stockmanagement.database.entity.tag.TagMasterEntity
import com.example.sol_denka_stockmanagement.model.inbound.InboundScanResult
import com.example.sol_denka_stockmanagement.model.outbound.EpcNameMapperResult
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

    @Query(
        "SELECT li.location_id\n" +
                "FROM tagmaster t\n" +
                "LEFT JOIN ledgeritem li ON li.ledger_item_id = t.ledger_item_id\n" +
                "WHERE t.epc = :epc"
    )
    suspend fun getLocationIdByTag(epc: String): Int?

    @Query(
        """
    SELECT * FROM tagmaster t
    LEFT JOIN ledgeritem le ON le.ledger_item_id = t.ledger_item_id
    WHERE le.location_id = :locationId AND le.is_in_stock = :isInStock
    """
    )
    suspend fun getTagsByLocationAndStock(locationId: Int, isInStock: Boolean): List<TagMasterEntity>

    @Query("SELECT * FROM tagmaster WHERE epc = :epc")
    suspend fun getTagIdLedgerIdByEpc(epc: String): TagMasterEntity

    @Query("""
        SELECT t.tag_id AS tagId, t.epc , it.item_type_name AS itemName, it.item_type_code AS itemCode, lo.location_name AS location
            FROM tagmaster AS t 
            LEFT JOIN ledgeritem le ON le.ledger_item_id = t.ledger_item_id
            LEFT JOIN locationmaster lo ON lo.location_id = le.location_id
            LEFT JOIN itemtypemaster it ON it.item_type_id = le.item_type_id
    """)
    suspend fun getFullInfo(): List<SingleTagInfoModel>

    @Query("SELECT ledger_item_id FROM tagmaster WHERE epc = :epc")
    suspend fun getLedgerIdByEpc(epc: String): Int

}