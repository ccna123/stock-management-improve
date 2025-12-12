package com.example.sol_denka_stockmanagement.intent

sealed interface InputIntent {
    data class ChangeProcessMethod(val value: String) : InputIntent
    data class ChangeMissRollReason(val value: String) : InputIntent
    data class ChangeCategory(val value: String, val categoryId: Int) : InputIntent
    data class ChangeLocation(val value: String) : InputIntent
    data class ChangeMemo(val value: String) : InputIntent
    data class ChangeOccurredAtDate(val value: String) : InputIntent
    data class ChangeOccurredAtTime(val value: String) : InputIntent
    data class ChangeWidth(val value: String) : InputIntent
    data class ChangeSpecificGravity(val value: String) : InputIntent
    data class ChangeThickness(val value: String) : InputIntent
    data class ChangeGrade(val value: String) : InputIntent
    data class ChangeWinderInfo(val value: String) : InputIntent
    data class ChangeLength(val value: String) : InputIntent
    data class ChangeWeight(val value: String) : InputIntent
    data class ChangeLotNo(val value: String) : InputIntent
    data class ChangePackingType(val value: String) : InputIntent
    data class ChangeFileTransferMethod(val value: String): InputIntent
    data class ChangeItemInCategory(val itemName: String, val itemId: Int): InputIntent
    data class SearchKeyWord(val itemName: String): InputIntent
    data class BulkApplyProcessMethod(val checkedTags: List<String>): InputIntent
}