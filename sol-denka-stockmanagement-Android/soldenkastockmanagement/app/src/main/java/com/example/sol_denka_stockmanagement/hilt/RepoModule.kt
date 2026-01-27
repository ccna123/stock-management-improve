package com.example.sol_denka_stockmanagement.hilt

import com.example.sol_denka_stockmanagement.app_interface.IPresetRepo
import com.example.sol_denka_stockmanagement.database.repository.csv.CsvTaskTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.field.FieldMasterRepository
import com.example.sol_denka_stockmanagement.database.repository.inventory.InventoryResultTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.item.ItemCategoryRepository
import com.example.sol_denka_stockmanagement.database.repository.item.ItemUnitRepository
import com.example.sol_denka_stockmanagement.database.repository.process.ProcessTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagStatusMasterRepository
import com.example.sol_denka_stockmanagement.database.repository.winder.WinderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    @IntoSet
    abstract fun bindItemUnitRepo(
        repo: ItemUnitRepository
    ): IPresetRepo

    @Binds
    @IntoSet
    abstract fun bindCsvTaskTypeRepo(
        repo: CsvTaskTypeRepository
    ): IPresetRepo


    @Binds
    @IntoSet
    abstract fun bindInventoryResultTypeRepo(
        repo: InventoryResultTypeRepository
    ): IPresetRepo


    @Binds
    @IntoSet
    abstract fun bindItemCategoryRepo(
        repo: ItemCategoryRepository
    ): IPresetRepo

    @Binds
    @IntoSet
    abstract fun bindProcessTypeRepo(
        repo: ProcessTypeRepository
    ): IPresetRepo

    @Binds
    @IntoSet
    abstract fun bindWinderInfoRepo(
        repo: WinderRepository
    ): IPresetRepo

    @Binds
    @IntoSet
    abstract fun bindFieldMasterRepo(
        repo: FieldMasterRepository
    ): IPresetRepo

    @Binds
    @IntoSet
    abstract fun bindTagStatusMasterRepo(
        repo: TagStatusMasterRepository
    ): IPresetRepo

}