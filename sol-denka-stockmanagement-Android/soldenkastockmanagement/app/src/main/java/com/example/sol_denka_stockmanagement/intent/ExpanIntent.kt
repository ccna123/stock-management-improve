package com.example.sol_denka_stockmanagement.intent

sealed interface ExpandIntent{
    data object ToggleLocationExpanded: ExpandIntent
    data object ToggleMissRollExpanded: ExpandIntent
    data object TogglePackingStyleExpanded: ExpandIntent
    data object ToggleHandlingMethodExpanded: ExpandIntent
    data object ToggleFileTransferMethodExpanded: ExpandIntent
    data class TogglePerTagHandlingExpanded(val tag: String): ExpandIntent
    data class CloseHandlingExpanded(val tag: String): ExpandIntent
}