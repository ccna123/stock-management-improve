package com.example.sol_denka_stockmanagement.intent

sealed interface ExpandIntent{
    data object ToggleLocationExpanded: ExpandIntent
    data object ToggleMissRollExpanded: ExpandIntent
    data object TogglePackingStyleExpanded: ExpandIntent
    data object ToggleProcessMethodExpanded: ExpandIntent
    data object ToggleCategoryExpanded: ExpandIntent
    data object ToggleFileTransferMethodExpanded: ExpandIntent
    data object ToggleCsvTypeExpanded: ExpandIntent
    data class TogglePerTagProcessExpanded(val tag: String): ExpandIntent
    data class CloseProcessExpanded(val tag: String): ExpandIntent
}