package com.example.sol_denka_stockmanagement.intent

sealed class ExpandIntent{
    data object ToggleStockAreaExpanded: ExpandIntent()
    data object ToggleMissRollExpanded: ExpandIntent()
    data object TogglePackingStyleExpanded: ExpandIntent()
    data object ToggleHandlingMethodExpanded: ExpandIntent()
}