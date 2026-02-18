package com.example.sol_denka_stockmanagement.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_33_34 = object : Migration(33, 34) {
    override fun migrate(db: SupportSQLiteDatabase) {

        // ---------------- MASTER TABLES ----------------

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS TagStatusMaster (
                tag_status_id INTEGER NOT NULL,
                status_code TEXT NOT NULL,
                status_name TEXT NOT NULL,
                created_at TEXT NOT NULL,
                updated_at TEXT NOT NULL,
                PRIMARY KEY(tag_status_id)
            )
        """)

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS ProcessType (
                process_type_id INTEGER NOT NULL,
                process_code TEXT NOT NULL,
                process_name TEXT NOT NULL,
                created_at TEXT NOT NULL,
                updated_at TEXT NOT NULL,
                PRIMARY KEY(process_type_id)
            )
        """)

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS Winder (
                winder_id INTEGER NOT NULL,
                winder_name TEXT NOT NULL,
                created_at TEXT,
                updated_at TEXT NOT NULL,
                PRIMARY KEY(winder_id)
            )
        """)

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS ItemCategoryMaster (
                item_category_id INTEGER NOT NULL,
                item_category_name TEXT NOT NULL,
                created_at TEXT NOT NULL,
                updated_at TEXT NOT NULL,
                PRIMARY KEY(item_category_id)
            )
        """)

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS ItemUnitMaster (
                item_unit_id INTEGER NOT NULL,
                item_unit_code TEXT NOT NULL,
                unit_category INTEGER NOT NULL,
                item_unit_name TEXT NOT NULL,
                created_at TEXT NOT NULL,
                updated_at TEXT NOT NULL,
                PRIMARY KEY(item_unit_id)
            )
        """)

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS LocationMaster (
                location_id INTEGER NOT NULL,
                location_code TEXT,
                location_name TEXT NOT NULL,
                memo TEXT,
                PRIMARY KEY(location_id)
            )
        """)

        // ---------------- TAG ----------------

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS TagMaster (
                tag_id INTEGER NOT NULL,
                tag_status_id INTEGER NOT NULL,
                epc TEXT NOT NULL,
                memo TEXT,
                PRIMARY KEY(tag_id),
                FOREIGN KEY(tag_status_id) REFERENCES TagStatusMaster(tag_status_id)
            )
        """)

        db.execSQL("CREATE INDEX IF NOT EXISTS index_TagMaster_tag_status_id ON TagMaster(tag_status_id)")

        // ---------------- ITEM TYPE ----------------

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS ItemTypeMaster (
                item_type_id INTEGER NOT NULL,
                item_count_unit_id INTEGER,
                item_weight_unit_id INTEGER,
                item_category_id INTEGER NOT NULL,
                item_type_code TEXT,
                item_type_name TEXT NOT NULL,
                packing_type TEXT,
                specific_gravity TEXT,
                grade TEXT,
                memo TEXT,
                unit_weight INTEGER,
                PRIMARY KEY(item_type_id),
                FOREIGN KEY(item_count_unit_id) REFERENCES ItemUnitMaster(item_unit_id) ON UPDATE CASCADE ON DELETE CASCADE,
                FOREIGN KEY(item_weight_unit_id) REFERENCES ItemUnitMaster(item_unit_id) ON UPDATE CASCADE ON DELETE CASCADE,
                FOREIGN KEY(item_category_id) REFERENCES ItemCategoryMaster(item_category_id) ON UPDATE CASCADE ON DELETE CASCADE
            )
        """)

        db.execSQL("CREATE INDEX IF NOT EXISTS index_ItemTypeMaster_item_count_unit_id ON ItemTypeMaster(item_count_unit_id)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_ItemTypeMaster_item_weight_unit_id ON ItemTypeMaster(item_weight_unit_id)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_ItemTypeMaster_item_category_id ON ItemTypeMaster(item_category_id)")

        // ---------------- LEDGER ----------------

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS LedgerItem (
                ledger_item_id INTEGER NOT NULL,
                item_type_id INTEGER NOT NULL,
                location_id INTEGER NOT NULL,
                winder_id INTEGER,
                tag_id INTEGER,
                is_in_stock INTEGER NOT NULL,
                weight INTEGER,
                width INTEGER,
                length INTEGER,
                thickness TEXT,
                lot_no TEXT,
                occurrence_reason TEXT,
                quantity INTEGER,
                memo TEXT,
                occurred_at TEXT,
                processed_at TEXT,
                registered_at TEXT NOT NULL,
                updated_at TEXT NOT NULL,
                PRIMARY KEY(ledger_item_id),
                FOREIGN KEY(item_type_id) REFERENCES ItemTypeMaster(item_type_id) ON UPDATE CASCADE ON DELETE CASCADE,
                FOREIGN KEY(location_id) REFERENCES LocationMaster(location_id) ON UPDATE CASCADE ON DELETE CASCADE,
                FOREIGN KEY(winder_id) REFERENCES Winder(winder_id) ON UPDATE CASCADE ON DELETE CASCADE,
                FOREIGN KEY(tag_id) REFERENCES TagMaster(tag_id) ON UPDATE CASCADE ON DELETE SET NULL
            )
        """)

        db.execSQL("CREATE INDEX IF NOT EXISTS index_LedgerItem_item_type_id ON LedgerItem(item_type_id)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_LedgerItem_location_id ON LedgerItem(location_id)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_LedgerItem_winder_id ON LedgerItem(winder_id)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_LedgerItem_tag_id ON LedgerItem(tag_id)")

        // ---------------- INVENTORY ----------------

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS InventoryResultType (
                inventory_result_type_id INTEGER NOT NULL,
                inventory_result_code TEXT NOT NULL,
                inventory_result_name TEXT NOT NULL,
                created_at TEXT NOT NULL,
                updated_at TEXT NOT NULL,
                PRIMARY KEY(inventory_result_type_id)
            )
        """)

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS InventorySession (
                inventory_session_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                source_session_uuid TEXT NOT NULL,
                location_id INTEGER NOT NULL,
                device_id TEXT NOT NULL,
                memo TEXT,
                executed_at TEXT NOT NULL,
                FOREIGN KEY(location_id) REFERENCES LocationMaster(location_id)
                ON UPDATE CASCADE ON DELETE CASCADE
            )
        """)

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS InventoryDetail (
                inventory_detail_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                inventory_session_id INTEGER NOT NULL,
                ledger_item_id INTEGER NOT NULL,
                tag_id INTEGER NOT NULL,
                scanned_at TEXT NOT NULL,
                FOREIGN KEY(inventory_session_id) REFERENCES InventorySession(inventory_session_id)
                ON UPDATE CASCADE ON DELETE CASCADE,
                FOREIGN KEY(ledger_item_id) REFERENCES LedgerItem(ledger_item_id)
                ON UPDATE CASCADE ON DELETE CASCADE,
                FOREIGN KEY(tag_id) REFERENCES TagMaster(tag_id)
                ON UPDATE CASCADE ON DELETE CASCADE
            )
        """)

        db.execSQL("CREATE INDEX IF NOT EXISTS index_InventoryDetail_inventory_session_id ON InventoryDetail(inventory_session_id)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_InventoryDetail_ledger_item_id ON InventoryDetail(ledger_item_id)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_InventoryDetail_tag_id ON InventoryDetail(tag_id)")

        // ---------------- SESSION TABLES ----------------

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS LocationChangeSession (
                location_change_session_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                device_id TEXT NOT NULL,
                executed_at TEXT NOT NULL
            )
        """)

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS OutboundSession (
                outbound_session_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                device_id TEXT NOT NULL,
                executed_at TEXT NOT NULL
            )
        """)

        // ---------------- EVENT TABLES ----------------

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS LocationChangeEvent (
                location_change_event_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                location_change_session_id INTEGER NOT NULL,
                ledger_item_id INTEGER NOT NULL,
                location_id INTEGER NOT NULL,
                source_event_id TEXT NOT NULL,
                memo TEXT,
                scanned_at TEXT NOT NULL,
                FOREIGN KEY(location_change_session_id) REFERENCES LocationChangeSession(location_change_session_id) ON UPDATE CASCADE ON DELETE CASCADE,
                FOREIGN KEY(ledger_item_id) REFERENCES LedgerItem(ledger_item_id) ON UPDATE CASCADE ON DELETE CASCADE,
                FOREIGN KEY(location_id) REFERENCES LocationMaster(location_id) ON UPDATE CASCADE ON DELETE CASCADE
            )
        """)

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS OutboundEvent (
                outbound_event_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                outbound_session_id INTEGER NOT NULL,
                ledger_item_id INTEGER NOT NULL,
                process_type_id INTEGER NOT NULL,
                source_event_id TEXT NOT NULL,
                memo TEXT,
                processed_at TEXT,
                registered_at TEXT NOT NULL,
                FOREIGN KEY(outbound_session_id) REFERENCES OutboundSession(outbound_session_id) ON UPDATE CASCADE ON DELETE CASCADE,
                FOREIGN KEY(ledger_item_id) REFERENCES LedgerItem(ledger_item_id) ON UPDATE CASCADE ON DELETE CASCADE,
                FOREIGN KEY(process_type_id) REFERENCES ProcessType(process_type_id) ON UPDATE CASCADE ON DELETE CASCADE
            )
        """)
    }
}
