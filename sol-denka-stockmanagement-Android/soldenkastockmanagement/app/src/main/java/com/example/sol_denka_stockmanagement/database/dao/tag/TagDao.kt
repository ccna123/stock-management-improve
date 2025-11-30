package com.example.sol_denka_stockmanagement.database.dao.tag

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.tag.TagMasterEntity
import com.example.sol_denka_stockmanagement.model.inbound.InboundScanResult
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao: IDao<TagMasterEntity> {

    @Query("SELECT * FROM TagMaster")
    override fun get(): Flow<List<TagMasterEntity>>

    @Insert
    override suspend fun insert(e: TagMasterEntity)

    @Update
    override suspend fun update(e: TagMasterEntity)

    @Delete
    override suspend fun delete(e: TagMasterEntity)

    @Query("SELECT t.epc, i.item_type_name AS itemName, i.item_type_code AS itemCode FROM tagmaster t\n" +
            "LEFT JOIN ledgeritem l ON l.ledger_item_id = t.ledger_item_id\n" +
            "LEFT JOIN itemtypemaster i ON i.item_type_id = l.item_type_id\n" +
            "WHERE t.epc = :epc")
    suspend fun getTagDetailForInbound(epc: String): InboundScanResult
}