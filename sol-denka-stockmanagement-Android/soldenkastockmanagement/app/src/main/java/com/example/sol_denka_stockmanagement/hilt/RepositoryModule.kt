package com.example.sol_denka_stockmanagement.hilt

import com.example.sol_denka_stockmanagement.data.local.repository.field.FieldMasterRepositoryImpl
import com.example.sol_denka_stockmanagement.data.local.repository.field.ItemTypeFieldSettingMasterRepositoryImpl
import com.example.sol_denka_stockmanagement.data.local.repository.inbound.InboundEventRepositoryImpl
import com.example.sol_denka_stockmanagement.data.local.repository.inbound.InboundSessionRepositoryImpl
import com.example.sol_denka_stockmanagement.data.local.repository.inventory.InventoryDetailRepositoryImpl
import com.example.sol_denka_stockmanagement.data.local.repository.inventory.InventoryResultTypeRepositoryImpl
import com.example.sol_denka_stockmanagement.data.local.repository.inventory.InventorySessionRepositoryImpl
import com.example.sol_denka_stockmanagement.data.local.repository.item.ItemCategoryRepositoryImpl
import com.example.sol_denka_stockmanagement.data.local.repository.item.ItemTypeRepositoryImpl
import com.example.sol_denka_stockmanagement.data.local.repository.item.ItemUnitRepositoryImpl
import com.example.sol_denka_stockmanagement.data.local.repository.ledger.LedgerItemRepositoryImpl
import com.example.sol_denka_stockmanagement.data.local.repository.location.LocationChangeEventRepositoryImpl
import com.example.sol_denka_stockmanagement.data.local.repository.location.LocationChangeSessionRepositoryImpl
import com.example.sol_denka_stockmanagement.data.local.repository.location.LocationMasterRepositoryImpl
import com.example.sol_denka_stockmanagement.data.local.repository.outbound.OutboundEventRepositoryImpl
import com.example.sol_denka_stockmanagement.data.local.repository.outbound.OutboundSessionRepositoryImpl
import com.example.sol_denka_stockmanagement.data.local.repository.process.ProcessTypeRepositoryImpl
import com.example.sol_denka_stockmanagement.data.local.repository.winder.WinderRepositoryImpl
import com.example.sol_denka_stockmanagement.domain.repository.field.IFieldMasterRepository
import com.example.sol_denka_stockmanagement.domain.repository.field.IItemTypeFieldSettingMasterRepository
import com.example.sol_denka_stockmanagement.domain.repository.inbound.IInboundEventRepository
import com.example.sol_denka_stockmanagement.domain.repository.inbound.IInboundSessionRepository
import com.example.sol_denka_stockmanagement.domain.repository.inventory.IInventoryDetailRepository
import com.example.sol_denka_stockmanagement.domain.repository.inventory.IInventoryResultTypeRepository
import com.example.sol_denka_stockmanagement.domain.repository.inventory.IInventorySessionRepository
import com.example.sol_denka_stockmanagement.domain.repository.item.IItemCategoryRepository
import com.example.sol_denka_stockmanagement.domain.repository.item.IItemTypeRepository
import com.example.sol_denka_stockmanagement.domain.repository.item.IItemUnitRepository
import com.example.sol_denka_stockmanagement.domain.repository.ledger.ILedgerItemRepository
import com.example.sol_denka_stockmanagement.domain.repository.location.ILocationChangeEventRepository
import com.example.sol_denka_stockmanagement.domain.repository.location.ILocationChangeSessionRepository
import com.example.sol_denka_stockmanagement.domain.repository.location.ILocationMasterRepository
import com.example.sol_denka_stockmanagement.domain.repository.outbound.IOutboundEventRepository
import com.example.sol_denka_stockmanagement.domain.repository.outbound.IOutboundSessionRepository
import com.example.sol_denka_stockmanagement.domain.repository.process.IProcessTypeRepository
import com.example.sol_denka_stockmanagement.domain.repository.winder.IWinderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindFieldMasterRepository(impl: FieldMasterRepositoryImpl): IFieldMasterRepository

    @Binds @Singleton
    abstract fun bindItemTypeFieldSettingMasterRepository(impl: ItemTypeFieldSettingMasterRepositoryImpl): IItemTypeFieldSettingMasterRepository

    @Binds @Singleton
    abstract fun bindInboundEventRepository(impl: InboundEventRepositoryImpl): IInboundEventRepository

    @Binds @Singleton
    abstract fun bindInboundSessionRepository(impl: InboundSessionRepositoryImpl): IInboundSessionRepository

    @Binds @Singleton
    abstract fun bindInventoryDetailRepository(impl: InventoryDetailRepositoryImpl): IInventoryDetailRepository

    @Binds @Singleton
    abstract fun bindInventoryResultTypeRepository(impl: InventoryResultTypeRepositoryImpl): IInventoryResultTypeRepository

    @Binds @Singleton
    abstract fun bindInventorySessionRepository(impl: InventorySessionRepositoryImpl): IInventorySessionRepository

    // --- item ---
    @Binds @Singleton
    abstract fun bindItemCategoryRepository(impl: ItemCategoryRepositoryImpl): IItemCategoryRepository

    @Binds @Singleton
    abstract fun bindItemTypeRepository(impl: ItemTypeRepositoryImpl): IItemTypeRepository

    @Binds @Singleton
    abstract fun bindItemUnitRepository(impl: ItemUnitRepositoryImpl): IItemUnitRepository

    // --- ledger ---
    @Binds @Singleton
    abstract fun bindLedgerItemRepository(impl: LedgerItemRepositoryImpl): ILedgerItemRepository

    // --- location ---
    @Binds @Singleton
    abstract fun bindLocationChangeEventRepository(impl: LocationChangeEventRepositoryImpl): ILocationChangeEventRepository

    @Binds @Singleton
    abstract fun bindLocationChangeSessionRepository(impl: LocationChangeSessionRepositoryImpl): ILocationChangeSessionRepository

    @Binds @Singleton
    abstract fun bindLocationMasterRepository(impl: LocationMasterRepositoryImpl): ILocationMasterRepository

    @Binds
    @Singleton
    abstract fun bindOutboundEventRepository(impl: OutboundEventRepositoryImpl): IOutboundEventRepository

    @Binds @Singleton
    abstract fun bindOutboundSessionRepository(impl: OutboundSessionRepositoryImpl): IOutboundSessionRepository

    @Binds @Singleton
    abstract fun bindWinderRepository(impl: WinderRepositoryImpl): IWinderRepository

    @Binds @Singleton
    abstract fun bindProcessTypeRepository(impl: ProcessTypeRepositoryImpl): IProcessTypeRepository
}
