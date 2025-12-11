package com.example.sol_denka_stockmanagement

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.database.dao.csv.CsvHistoryDao
import com.example.sol_denka_stockmanagement.database.dao.csv.CsvTaskTypeDao
import com.example.sol_denka_stockmanagement.database.dao.field.FieldMasterDao
import com.example.sol_denka_stockmanagement.database.dao.field.ItemTypeFieldSettingMasterDao
import com.example.sol_denka_stockmanagement.database.dao.inbound.InboundEventDao
import com.example.sol_denka_stockmanagement.database.dao.inbound.InboundSessionDao
import com.example.sol_denka_stockmanagement.database.dao.inventory.InventoryResultLocalDao
import com.example.sol_denka_stockmanagement.database.dao.inventory.InventoryResultTypeDao
import com.example.sol_denka_stockmanagement.database.dao.inventory.InventorySessionDao
import com.example.sol_denka_stockmanagement.database.dao.item.ItemTypeDao
import com.example.sol_denka_stockmanagement.database.dao.item.ItemUnitDao
import com.example.sol_denka_stockmanagement.database.dao.leger.LedgerItemDao
import com.example.sol_denka_stockmanagement.database.dao.location.LocationChangeEventDao
import com.example.sol_denka_stockmanagement.database.dao.location.LocationChangeSessionDao
import com.example.sol_denka_stockmanagement.database.dao.location.LocationDao
import com.example.sol_denka_stockmanagement.database.dao.outbound.OutboundEventDao
import com.example.sol_denka_stockmanagement.database.dao.outbound.OutboundSessionDao
import com.example.sol_denka_stockmanagement.database.dao.process.ProcessTypeDao
import com.example.sol_denka_stockmanagement.database.dao.tag.TagMasterDao
import com.example.sol_denka_stockmanagement.helper.controller.ReaderController
import com.example.sol_denka_stockmanagement.helper.controller.TagController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Provides
    @Singleton
    fun provideReaderController(@ApplicationContext context: Context): ReaderController {
        return ReaderController(context = context as Application)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Provides
    @Singleton
    fun provideTagController(): TagController {
        return TagController()
    }

    @Provides
    @Named("ZebraPrefs")
    fun provideZebraSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("zebra_prefs", Context.MODE_PRIVATE)
    }


    @Provides
    @Named("AppPrefs")
    fun provideAppSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "stock_management_db"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideTagDao(database: AppDatabase): TagMasterDao {
        return database.tagDao()
    }

    @Provides
    @Singleton
    fun provideLocationDao(database: AppDatabase): LocationDao {
        return database.locationDao()
    }

    @Provides
    @Singleton
    fun provideItemUnitDao(database: AppDatabase): ItemUnitDao {
        return database.itemUnitDao()
    }

    @Provides
    @Singleton
    fun provideItemTypeDao(database: AppDatabase): ItemTypeDao {
        return database.itemTypeDao()
    }

    @Provides
    @Singleton
    fun provideLedgerItemDao(database: AppDatabase): LedgerItemDao {
        return database.ledgerItemDao()
    }

    @Provides
    @Singleton
    fun provideProcessTypeDao(database: AppDatabase): ProcessTypeDao {
        return database.processTypeDao()
    }

    @Provides
    @Singleton
    fun provideCsvTaskTypeDao(database: AppDatabase): CsvTaskTypeDao {
        return database.csvTaskTypeDao()
    }

    @Provides
    @Singleton
    fun provideInventoryResultTypeDao(database: AppDatabase): InventoryResultTypeDao {
        return database.inventoryResultTypeDao()
    }

    @Provides
    @Singleton
    fun provideLocationChangeSessionDao(database: AppDatabase): LocationChangeSessionDao {
        return database.locationChangeSessionDao()
    }

    @Provides
    @Singleton
    fun provideLocationChangeEventDao(database: AppDatabase): LocationChangeEventDao {
        return database.locationChangeEventDao()
    }

    @Provides
    @Singleton
    fun provideOutboundSessionDao(database: AppDatabase): OutboundSessionDao {
        return database.outboundSessionDao()
    }

    @Provides
    @Singleton
    fun provideOutboundEventDao(database: AppDatabase): OutboundEventDao {
        return database.outboundEventDao()
    }

    @Provides
    @Singleton
    fun provideInventorySessionDao(database: AppDatabase): InventorySessionDao {
        return database.inventorySessionDao()
    }

    @Provides
    @Singleton
    fun provideInventoryResultLocalDao(database: AppDatabase): InventoryResultLocalDao {
        return database.inventoryResultLocalDao()
    }

    @Provides
    @Singleton
    fun provideCsvHistoryDao(database: AppDatabase): CsvHistoryDao {
        return database.csvHistoryDao()
    }

    @Provides
    @Singleton
    fun provideInboundSessionDao(database: AppDatabase): InboundSessionDao {
        return database.inboundSessionDao()
    }

    @Provides
    @Singleton
    fun provideInboundEventDao(database: AppDatabase): InboundEventDao {
        return database.inboundEventDao()
    }
    @Provides
    @Singleton
    fun provideFieldMasterDao(database: AppDatabase): FieldMasterDao {
        return database.fieldMasterDao()
    }
    @Provides
    @Singleton
    fun provideItemTypeFieldSettingMasterDao(database: AppDatabase): ItemTypeFieldSettingMasterDao {
        return database.itemTypeFieldSettingMasterDao()
    }

}