package com.example.sol_denka_stockmanagement.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_34_35 = object : Migration(34, 35) {
        override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
            ALTER TABLE OutboundEvent
            ADD COLUMN tag_id INTEGER NOT NULL DEFAULT 0
        """.trimIndent())
        }
}
