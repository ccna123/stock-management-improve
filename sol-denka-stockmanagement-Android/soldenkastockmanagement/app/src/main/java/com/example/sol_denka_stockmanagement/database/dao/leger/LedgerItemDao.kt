package com.example.sol_denka_stockmanagement.database.dao.leger

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.sol_denka_stockmanagement.database.entity.ledger.LedgerItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LedgerItemDao {

    @Query("SELECT * FROM ledgeritem")
    fun get(): Flow<List<LedgerItemEntity>>

    @Query("SELECT winder_id FROM LedgerItem WHERE ledger_item_id = :ledgerId ")
    suspend fun getWinderIdByLedgerId(ledgerId: Int): Int

    @Query(
        """
        SELECT DISTINCT tag_id
        FROM LedgerItem
        WHERE tag_id IS NOT NULL
    """
    )
    fun getMappedTagIdsFlow(): Flow<List<Int>>

    @Insert(onConflict = REPLACE)
    suspend fun insert(e: LedgerItemEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(e: List<LedgerItemEntity>)

    @Update
    suspend fun update(e: LedgerItemEntity)

    @Delete
    suspend fun delete(e: LedgerItemEntity)

    @Query("DELETE FROM ledgeritem")
    suspend fun deleteAll()

    @Upsert
    suspend fun upsertAll(e: List<LedgerItemEntity>)
}