package com.example.sol_denka_stockmanagement.helper

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object TagDistanceCalculate {
    fun calculateBoxWidth(rssi: Float): Dp {
        val minRssi = -89f
        val maxRssi = -30f
        val minWidth = 0f
        val maxWidth = 120f

        // Return 0.dp for invalid (rssi >= 0) or weak signal (rssi <= -90)
        if (rssi >= 0f || rssi <= -90f) return 0.dp

        // Clamp RSSI to the valid range
        val clampedRssi = rssi.coerceIn(minRssi, maxRssi)
        // Linear interpolation: map RSSI to width
        val width = minWidth + (maxWidth - minWidth) * (clampedRssi - minRssi) / (maxRssi - minRssi)
        return width.dp
    }
}