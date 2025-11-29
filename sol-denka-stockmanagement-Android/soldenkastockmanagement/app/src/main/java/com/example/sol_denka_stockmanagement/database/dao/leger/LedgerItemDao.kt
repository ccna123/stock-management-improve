package com.example.sol_denka_stockmanagement.database.dao.leger

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.leger.LedgerItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LedgerItemDao: IDao<LedgerItemEntity> {

    @Query("SELECT * FROM ledgeritem")
    override fun get(): Flow<List<LedgerItemEntity>>

    @Insert
    override suspend fun insert(e: LedgerItemEntity)

    @Update
    override suspend fun update(e: LedgerItemEntity)

    @Delete
    override suspend fun delete(e: LedgerItemEntity)
}