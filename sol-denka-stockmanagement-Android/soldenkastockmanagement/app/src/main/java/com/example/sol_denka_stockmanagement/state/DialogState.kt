package com.example.sol_denka_stockmanagement.state

sealed interface DialogState {
    data object Hidden: DialogState

    data class Error(
        val message: String,
    ) : DialogState

    data class Confirm(
        val message: String = "",
    ) : DialogState

}