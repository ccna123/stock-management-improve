package com.example.sol_denka_stockmanagement

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.database.dao.InventoryItemMasterDao
import com.example.sol_denka_stockmanagement.helper.ReaderController
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
    fun provideInventoryItemMasterDao(db: AppDatabase): InventoryItemMasterDao {
        return db.inventoryItemMasterDao()
    }

//    @Provides
//    @Singleton
//    fun provideAppSettingStorage(): JsonFileSettingStorage<AppSettingModel> {
//        return JsonFileSettingStorage(
//            fileName = "app_setting.json",
//            clazz = AppSettingModel::class.java
//        )
//    }
}