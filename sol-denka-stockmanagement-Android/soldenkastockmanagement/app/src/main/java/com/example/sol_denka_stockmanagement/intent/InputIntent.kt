package com.example.sol_denka_stockmanagement.intent

sealed interface InputIntent {
    data class ChangeProcessMethod(val value: String) : InputIntent
    data class ChangeMissRoll(val value: String) : InputIntent
    data class ChangeCategory(val value: String) : InputIntent
    data class ChangeLocation(val value: String) : InputIntent
    data class ChangeMemo(val value: String) : InputIntent
    data class ChangeOccurredAtDate(val value: String) : InputIntent
    data class ChangeOccurredAtTime(val value: String) : InputIntent
    data class ChangeThickness(val value: String) : InputIntent
    data class ChangeGrade(val value: String) : InputIntent
    data class ChangeRollingMachineInfo(val value: String) : InputIntent
    data class ChangeLength(val value: String) : InputIntent
    data class ChangeWeight(val value: String) : InputIntent
    data class ChangeLotNo(val value: String) : InputIntent
    data class ChangePackingStyle(val value: String) : InputIntent
    data class ChangeFileTransferMethod(val value: String): InputIntent
    data class BulkApplyProcessMethod(val checkedTags: List<String>): InputIntent
}