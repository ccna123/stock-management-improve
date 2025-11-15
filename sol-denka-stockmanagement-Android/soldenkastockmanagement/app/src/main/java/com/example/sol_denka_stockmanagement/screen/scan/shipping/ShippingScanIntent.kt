package com.example.sol_denka_stockmanagement.screen.scan.shipping


sealed class ShippingScanIntent(){
    data class ToggleTagSelection(val tag: String, val totalTag: Int): ShippingScanIntent()
    data class ToggleSelectionAll(val tagList: Set<String>): ShippingScanIntent()
    data object ClearTagSelectionList: ShippingScanIntent()
    data object ResetState: ShippingScanIntent()
}
