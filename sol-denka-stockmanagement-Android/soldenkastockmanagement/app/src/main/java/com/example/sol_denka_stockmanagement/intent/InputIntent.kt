package com.example.sol_denka_stockmanagement.intent

import com.example.sol_denka_stockmanagement.model.location.LocationMasterModel
import com.example.sol_denka_stockmanagement.model.winder.WinderModel

sealed interface InputIntent {
    data class ChangeProcessMethod(val value: String) : InputIntent
    data class ChangeMissRollReason(val value: String) : InputIntent
    data class ChangeCategory(val value: String, val categoryId: Int) : InputIntent
    data class ChangeLocation(val value: LocationMasterModel?) : InputIntent
    data class ChangeQuantity(val value: String) : InputIntent
    data class ChangeMemo(val value: String) : InputIntent
    data class ChangeOccurredAtDate(val value: String) : InputIntent
    data class ChangeProcessedAtDate(val value: String) : InputIntent
    data class ChangeOccurredAtTime(val value: String) : InputIntent
    data class ChangeProcessedAtTime(val value: String) : InputIntent
    data class ChangeWidth(val value: String) : InputIntent
    data class ChangeThickness(val value: String) : InputIntent
    data class ChangeWinderType(val value: WinderModel?) : InputIntent
    data class ChangeLength(val value: String) : InputIntent
    data class ChangeWeight(val value: String) : InputIntent
    data class ChangeLotNo(val value: String) : InputIntent
    data class ChangeFileTransferMethod(val value: String): InputIntent
    data class ChangeItemInCategory(val itemName: String, val itemId: Int): InputIntent
    data class SearchKeyWord(val itemName: String): InputIntent
    data class BulkApplyProcessMethod(val checkedTags: List<String>): InputIntent
    data class UpdateFieldErrors(val errors: Map<String, Boolean>): InputIntent
}