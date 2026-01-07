package com.example.sol_denka_stockmanagement.database

import androidx.room.Database
import androidx.room.RoomDatabase

import com.example.sol_denka_stockmanagement.database.dao.csv.CsvHistoryDao
import com.example.sol_denka_stockmanagement.database.dao.csv.CsvTaskTypeDao
import com.example.sol_denka_stockmanagement.database.dao.field.FieldMasterDao
import com.example.sol_denka_stockmanagement.database.dao.field.ItemTypeFieldSettingMasterDao
import com.example.sol_denka_stockmanagement.database.dao.inbound.InboundEventDao
import com.example.sol_denka_stockmanagement.database.dao.inbound.InboundSessionDao
import com.example.sol_denka_stockmanagement.database.dao.inventory.InventorySessionDao
import com.example.sol_denka_stockmanagement.database.dao.inventory.InventoryResultLocalDao
import com.example.sol_denka_stockmanagement.database.dao.inventory.InventoryResultTypeDao
import com.example.sol_denka_stockmanagement.database.dao.item.ItemCategoryDao
import com.example.sol_denka_stockmanagement.database.dao.location.LocationDao
import com.example.sol_denka_stockmanagement.database.dao.location.LocationChangeEventDao
import com.example.sol_denka_stockmanagement.database.dao.location.LocationChangeSessionDao
import com.example.sol_denka_stockmanagement.database.dao.item.ItemTypeDao
import com.example.sol_denka_stockmanagement.database.dao.item.ItemUnitDao
import com.example.sol_denka_stockmanagement.database.dao.leger.LedgerItemDao
import com.example.sol_denka_stockmanagement.database.dao.tag.TagMasterDao
import com.example.sol_denka_stockmanagement.database.dao.outbound.OutboundEventDao
import com.example.sol_denka_stockmanagement.database.dao.outbound.OutboundSessionDao
import com.example.sol_denka_stockmanagement.database.dao.process.ProcessTypeDao
import com.example.sol_denka_stockmanagement.database.dao.tag.TagStatusMasterDao
import com.example.sol_denka_stockmanagement.database.dao.winder.WinderInfoDao
import com.example.sol_denka_stockmanagement.database.entity.csv.CsvHistoryEntity
import com.example.sol_denka_stockmanagement.database.entity.csv.CsvTaskTypeEntity
import com.example.sol_denka_stockmanagement.database.entity.field.FieldMasterEntity
import com.example.sol_denka_stockmanagement.database.entity.field.ItemTypeFieldSettingMasterEntity
import com.example.sol_denka_stockmanagement.database.entity.inbound.InboundEventEntity
import com.example.sol_denka_stockmanagement.database.entity.inbound.InboundSessionEntity
import com.example.sol_denka_stockmanagement.database.entity.inventory.InventorySessionEntity
import com.example.sol_denka_stockmanagement.database.entity.inventory.InventoryResultLocalEntity
import com.example.sol_denka_stockmanagement.database.entity.inventory.InventoryResultTypeEntity
import com.example.sol_denka_stockmanagement.database.entity.item.ItemCategoryEntity
import com.example.sol_denka_stockmanagement.database.entity.location.LocationChangeEventEntity
import com.example.sol_denka_stockmanagement.database.entity.location.LocationChangeSessionEntity
import com.example.sol_denka_stockmanagement.database.entity.location.LocationMasterEntity
import com.example.sol_denka_stockmanagement.database.entity.item.ItemTypeMasterEntity
import com.example.sol_denka_stockmanagement.database.entity.item.ItemUnitMasterEntity
import com.example.sol_denka_stockmanagement.database.entity.ledger.LedgerItemEntity
import com.example.sol_denka_stockmanagement.database.entity.tag.TagMasterEntity
import com.example.sol_denka_stockmanagement.database.entity.outbound.OutBoundEventEntity
import com.example.sol_denka_stockmanagement.database.entity.outbound.OutboundSessionEntity
import com.example.sol_denka_stockmanagement.database.entity.process.ProcessTypeEntity
import com.example.sol_denka_stockmanagement.database.entity.tag.TagStatusMasterEntity
import com.example.sol_denka_stockmanagement.database.entity.winder.WinderInfoEntity

@Database(
    entities = [
        CsvHistoryEntity::class,
        CsvTaskTypeEntity::class,

        InboundEventEntity::class,
        InboundSessionEntity::class,

        InventorySessionEntity::class,
        InventoryResultLocalEntity::class,
        InventoryResultTypeEntity::class,

        LocationChangeEventEntity::class,
        LocationChangeSessionEntity::class,
        LocationMasterEntity::class,

        ItemTypeMasterEntity::class,
        ItemUnitMasterEntity::class,
        ItemCategoryEntity::class,

        LedgerItemEntity::class,

        TagMasterEntity::class,
        TagStatusMasterEntity::class,

        OutBoundEventEntity::class,
        OutboundSessionEntity::class,

        ProcessTypeEntity::class,

        FieldMasterEntity::class,
        ItemTypeFieldSettingMasterEntity::class,

        WinderInfoEntity::class
    ],
    version = 26
)
abstract class AppDatabase : RoomDatabase() {

    // CSV
    abstract fun csvHistoryDao(): CsvHistoryDao
    abstract fun csvTaskTypeDao(): CsvTaskTypeDao

    // Inbound
    abstract fun inboundEventDao(): InboundEventDao
    abstract fun inboundSessionDao(): InboundSessionDao

    // Inventory
    abstract fun inventorySessionDao(): InventorySessionDao
    abstract fun inventoryResultLocalDao(): InventoryResultLocalDao
    abstract fun inventoryResultTypeDao(): InventoryResultTypeDao

    // Location
    abstract fun locationDao(): LocationDao
    abstract fun locationChangeEventDao(): LocationChangeEventDao
    abstract fun locationChangeSessionDao(): LocationChangeSessionDao

    // Item
    abstract fun itemTypeDao(): ItemTypeDao
    abstract fun itemUnitDao(): ItemUnitDao
    abstract fun itemCategoryDao(): ItemCategoryDao


    // Ledger
    abstract fun ledgerItemDao(): LedgerItemDao

    // Tag
    abstract fun tagDao(): TagMasterDao
    abstract fun tagStatusDao(): TagStatusMasterDao

    // Outbound
    abstract fun outboundEventDao(): OutboundEventDao
    abstract fun outboundSessionDao(): OutboundSessionDao

    // Process
    abstract fun processTypeDao(): ProcessTypeDao

    //Field
    abstract fun fieldMasterDao(): FieldMasterDao
    abstract fun itemTypeFieldSettingMasterDao(): ItemTypeFieldSettingMasterDao

    //Winder
    abstract fun winderInfoDao(): WinderInfoDao

}
