package com.example.sol_denka_stockmanagement.database.dao.tag

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.dao.location.LocationChangeScanResult
import com.example.sol_denka_stockmanagement.database.entity.tag.TagMasterEntity
import com.example.sol_denka_stockmanagement.model.inbound.InboundScanResult
import com.example.sol_denka_stockmanagement.model.outbound.OutboundScanResultDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface TagMasterDao : IDao<TagMasterEntity> {

    @Query("SELECT * FROM TagMaster")
    override fun get(): Flow<List<TagMasterEntity>>

    @Insert(onConflict = REPLACE)
    override suspend fun insert(e: TagMasterEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(e: List<TagMasterEntity>)

    @Update
    override suspend fun update(e: TagMasterEntity)

    @Delete
    override suspend fun delete(e: TagMasterEntity)

    @Query(
        "SELECT t.epc, i.item_type_name AS itemName, i.item_type_code AS itemCode FROM tagmaster t\n" +
                "LEFT JOIN ledgeritem l ON l.ledger_item_id = t.ledger_item_id\n" +
                "LEFT JOIN itemtypemaster i ON i.item_type_id = l.item_type_id\n" +
                "WHERE t.epc = :epc"
    )
    suspend fun getTagDetailForInbound(epc: String): InboundScanResult?

    @Query(
        "SELECT t.epc,\n" +
                "           i.item_type_name AS itemName,\n" +
                "           i.item_type_code AS itemCode,\n" +
                "           l.location_name AS location\n" +
                "    FROM tagmaster t\n" +
                "    LEFT JOIN ledgeritem li ON li.ledger_item_id = t.ledger_item_id\n" +
                "    LEFT JOIN itemtypemaster i ON i.item_type_id = li.item_type_id\n" +
                "    LEFT JOIN locationmaster l ON l.location_id = li.location_id\n" +
                "    WHERE t.epc IN (:epcList)"
    )
    suspend fun getTagDetailForLocationChange(epcList: List<String>): List<LocationChangeScanResult>

    @Query(
        "SELECT t.epc,\n" +
                "           i.item_type_name AS itemName\n" +
                "    FROM tagmaster t\n" +
                "    LEFT JOIN ledgeritem li ON li.ledger_item_id = t.ledger_item_id\n" +
                "    LEFT JOIN itemtypemaster i ON i.item_type_id = li.item_type_id\n" +
                "    WHERE t.epc IN (:epcList)"
    )
    suspend fun getItemNameByTagId(epcList: List<String>): List<OutboundScanResultDTO>?

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

}