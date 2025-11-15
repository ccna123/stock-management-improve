package com.example.sol_denka_stockmanagement.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sol_denka_stockmanagement.database.dao.CsvHistoryDao
import com.example.sol_denka_stockmanagement.database.dao.EventTypeDao
import com.example.sol_denka_stockmanagement.database.dao.InOutEventDao
import com.example.sol_denka_stockmanagement.database.dao.InventoryItemMasterDao
import com.example.sol_denka_stockmanagement.database.dao.InventoryResultDao
import com.example.sol_denka_stockmanagement.database.dao.InventoryTaskDao
import com.example.sol_denka_stockmanagement.database.dao.LocationDao
import com.example.sol_denka_stockmanagement.database.dao.MaterialDao
import com.example.sol_denka_stockmanagement.database.entity.CsvHistoryEntity
import com.example.sol_denka_stockmanagement.database.entity.EventTypeMasterEntity
import com.example.sol_denka_stockmanagement.database.entity.InOutEventEntity
import com.example.sol_denka_stockmanagement.database.entity.InventoryItemMasterEntity
import com.example.sol_denka_stockmanagement.database.entity.InventoryResultEntity
import com.example.sol_denka_stockmanagement.database.entity.InventoryTaskEntity
import com.example.sol_denka_stockmanagement.database.entity.LocationMasterEntity
import com.example.sol_denka_stockmanagement.database.entity.MaterialMasterEntity

@Database(
    entities = [
        LocationMasterEntity::class,
        MaterialMasterEntity::class,
        CsvHistoryEntity::class,
        EventTypeMasterEntity::class,
        InventoryItemMasterEntity::class,
        InventoryTaskEntity::class,
        InventoryResultEntity::class,
        InOutEventEntity::class,
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun materialDao(): MaterialDao
    abstract fun csvHistoryDao(): CsvHistoryDao
    abstract fun eventTypeDao(): EventTypeDao
    abstract fun inventoryItemMasterDao(): InventoryItemMasterDao
    abstract fun inventoryTaskDao(): InventoryTaskDao
    abstract fun inventoryResultDao(): InventoryResultDao
    abstract fun inOutEventDao(): InOutEventDao
}