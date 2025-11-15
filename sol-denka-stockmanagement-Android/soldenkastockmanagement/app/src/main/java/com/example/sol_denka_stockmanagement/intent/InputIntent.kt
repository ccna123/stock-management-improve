package com.example.sol_denka_stockmanagement.intent

sealed class InputIntent {
    data class UpdateHandlingMethod(val value: String) : InputIntent()
    data class UpdateMissRoll(val value: String) : InputIntent()
    data class UpdateStockArea(val value: String) : InputIntent()
    data class UpdateRemark(val value: String) : InputIntent()
    data class UpdateThickness(val value: String) : InputIntent()
    data class UpdateGrade(val value: String) : InputIntent()
    data class UpdateRollingMachineInfo(val value: String) : InputIntent()
    data class UpdateLength(val value: String) : InputIntent()
    data class UpdateWeight(val value: String) : InputIntent()
    data class UpdateLotNo(val value: String) : InputIntent()
    data class UpdatePackingStyle(val value: String) : InputIntent()
}