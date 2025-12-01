package com.example.sol_denka_stockmanagement.database.dao.leger

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.ledger.LedgerItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LedgerItemDao: IDao<LedgerItemEntity> {

    @Query("SELECT * FROM ledgeritem")
    override fun get(): Flow<List<LedgerItemEntity>>

    @Insert(onConflict = REPLACE)
    override suspend fun insert(e: LedgerItemEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(e: List<LedgerItemEntity>)

    @Update
    override suspend fun update(e: LedgerItemEntity)

    @Delete
    override suspend fun delete(e: LedgerItemEntity)
}