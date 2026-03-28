package com.example.sol_denka_stockmanagement.domain.model.inventory

import androidx.compose.ui.graphics.Color
import com.example.sol_denka_stockmanagement.constant.InventoryScanResult


data class InventoryCompleteModel(
    val status: InventoryScanResult,
    val icon: Int,
    val color: Color
)
