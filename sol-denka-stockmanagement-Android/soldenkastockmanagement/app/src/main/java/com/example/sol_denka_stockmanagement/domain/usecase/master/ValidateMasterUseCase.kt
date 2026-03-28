package com.example.sol_denka_stockmanagement.domain.usecase.master

import com.example.sol_denka_stockmanagement.domain.repository.csv.ICsvTaskTypeRepository
import com.example.sol_denka_stockmanagement.domain.repository.field.IFieldMasterRepository
import com.example.sol_denka_stockmanagement.domain.repository.field.IItemTypeFieldSettingMasterRepository
import com.example.sol_denka_stockmanagement.domain.repository.item.IItemCategoryRepository
import com.example.sol_denka_stockmanagement.domain.repository.item.IItemTypeRepository
import com.example.sol_denka_stockmanagement.domain.repository.item.IItemUnitRepository
import com.example.sol_denka_stockmanagement.domain.repository.ledger.ILedgerItemRepository
import com.example.sol_denka_stockmanagement.domain.repository.location.ILocationMasterRepository
import com.example.sol_denka_stockmanagement.domain.repository.process.IProcessTypeRepository
import com.example.sol_denka_stockmanagement.domain.repository.tag.ITagMasterRepository
import com.example.sol_denka_stockmanagement.domain.repository.tag.ITagStatusMasterRepository
import com.example.sol_denka_stockmanagement.domain.repository.winder.IWinderRepository
import javax.inject.Inject

class ValidateMasterUseCase @Inject constructor(
    private val locationRepo: ILocationMasterRepository,
    private val tagRepo: ITagMasterRepository,
    private val itemTypeRepo: IItemTypeRepository,
    private val csvTaskTypeRepo: ICsvTaskTypeRepository,
    private val fieldRepo: IFieldMasterRepository,
    private val itemCategoryRepo: IItemCategoryRepository,
    private val itemUnitRepo: IItemUnitRepository,
    private val processTypeRepo: IProcessTypeRepository,
    private val tagStatusRepo: ITagStatusMasterRepository,
    private val winderRepo: IWinderRepository,
    private val ledgerRepo: ILedgerItemRepository,
    private val itemTypeFieldRepo: IItemTypeFieldSettingMasterRepository
) {
    suspend operator fun invoke(operation: String): List<String> {
        val missing = mutableListOf<String>()

        if (locationRepo.countRecord() == 0) missing.add("保管場所マスタCSV")
        if (tagRepo.countRecord() == 0) missing.add("タグマスタCSV")
        if (itemTypeRepo.countRecord() == 0) missing.add("品目マスタCSV")
        if (csvTaskTypeRepo.countRecord() == 0) missing.add("CSVタスク種別CSV")
        if (fieldRepo.countRecord() == 0) missing.add("項目マスタCSV")
        if (itemCategoryRepo.countRecord() == 0) missing.add("品目区分CSV")
        if (itemUnitRepo.countRecord() == 0) missing.add("品目単位CSV")
        if (processTypeRepo.countRecord() == 0) missing.add("処理種別CSV")
        if (tagStatusRepo.countRecord() == 0) missing.add("タグステータス種別CSV")
        if (winderRepo.countRecord() == 0) missing.add("巻取機CSV")

        if (operation != "inbound") {
            if (ledgerRepo.countRecord() == 0) missing.add("台帳アイテムCSV")
        } else {
            if (itemTypeFieldRepo.countRecord() == 0) missing.add("品目項目設定マスタCSV")
        }

        return missing
    }
}