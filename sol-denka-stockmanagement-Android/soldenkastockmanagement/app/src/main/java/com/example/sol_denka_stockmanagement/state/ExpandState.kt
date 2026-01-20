package com.example.sol_denka_stockmanagement.state


data class ExpandState(
    val materialSelection: Boolean = false,
    val categoryExpanded: Boolean = false,
    val locationExpanded: Boolean = false,
    val handlingMethodExpanded: Boolean = false,
    val fileTransferMethodExpanded: Boolean = false,
    val csvTypeExpanded: Boolean = false,
    val winderExpanded: Boolean = false,
)
