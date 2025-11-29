package com.example.sol_denka_stockmanagement.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sol_denka_stockmanagement.database.dao.csv.CsvHistoryDao
import com.example.sol_denka_stockmanagement.database.dao.inbound.InboundEventDao
import com.example.sol_denka_stockmanagement.database.dao.inventory.InventoryItemMasterDao
import com.example.sol_denka_stockmanagement.database.dao.inventory.InventoryResultLocalDao
import com.example.sol_denka_stockmanagement.database.dao.leger.InventorySessionDao
import com.example.sol_denka_stockmanagement.database.dao.location.LocationDao
import com.example.sol_denka_stockmanagement.database.entity.csv.CsvHistoryEntity
import com.example.sol_denka_stockmanagement.database.entity.csv.CsvTaskTypeEntity
import com.example.sol_denka_stockmanagement.database.entity.inbound.InboundEventEntity
import com.example.sol_denka_stockmanagement.database.entity.inbound.InboundSessionEntity
import com.example.sol_denka_stockmanagement.database.entity.inventory.InventoryResultLocalEntity
import com.example.sol_denka_stockmanagement.database.entity.inventory.InventoryResultTypeEntity
import com.example.sol_denka_stockmanagement.database.entity.inventory.InventorySessionEntity
import com.example.sol_denka_stockmanagement.database.entity.location.LocationChangeEventEntity
import com.example.sol_denka_stockmanagement.database.entity.location.LocationChangeSessionEntity
import com.example.sol_denka_stockmanagement.database.entity.location.LocationMasterEntity
import com.example.sol_denka_stockmanagement.database.entity.item.ItemTypeMasterEntity
import com.example.sol_denka_stockmanagement.database.entity.item.ItemUnitMasterEntity
import com.example.sol_denka_stockmanagement.database.entity.leger.LedgerItemEntity
import com.example.sol_denka_stockmanagement.database.entity.tag.TagMasterEntity
import com.example.sol_denka_stockmanagement.database.entity.outbound.OutBoundEventEntity
import com.example.sol_denka_stockmanagement.database.entity.outbound.OutboundSessionEntity
import com.example.sol_denka_stockmanagement.database.entity.process.ProcessTypeEntity

@Database(
    entities = [
        CsvHistoryEntity::class,
        CsvTaskTypeEntity::class,
        InboundEventEntity::class,
        InboundSessionEntity::class,
        InventorySessionEntity::class,
        InventoryResultTypeEntity::class,
        InventoryResultLocalEntity::class,
        LocationChangeEventEntity::class,
        LocationChangeSessionEntity::class,
        LocationMasterEntity::class,
        ItemTypeMasterEntity::class,
        ItemUnitMasterEntity::class,
        LedgerItemEntity::class,
        TagMasterEntity::class,
        OutBoundEventEntity::class,
        OutboundSessionEntity::class,
        ProcessTypeEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun materialDao(): MaterialDao
    abstract fun csvHistoryDao(): CsvHistoryDao
    abstract fun eventTypeDao(): EventTypeDao
    abstract fun inventoryItemMasterDao(): InventoryItemMasterDao
    abstract fun inventoryTaskDao(): InventorySessionDao
    abstract fun inventoryResultDao(): InventoryResultLocalDao
    abstract fun inOutEventDao(): InboundEventDao
}