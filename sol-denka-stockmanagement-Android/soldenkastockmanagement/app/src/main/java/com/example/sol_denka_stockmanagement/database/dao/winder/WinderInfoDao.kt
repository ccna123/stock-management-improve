package com.example.sol_denka_stockmanagement.database.dao.winder

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.winder.WinderInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WinderInfoDao: IDao<WinderInfoEntity> {

    @Query("SELECT * FROM CsvHistory")
    override fun get(): Flow<List<WinderInfoEntity>>

    @Insert
    override suspend fun insert(e: WinderInfoEntity): Long

    @Update
    override suspend fun update(e: WinderInfoEntity)

    @Delete
    override suspend fun delete(e: WinderInfoEntity)
}