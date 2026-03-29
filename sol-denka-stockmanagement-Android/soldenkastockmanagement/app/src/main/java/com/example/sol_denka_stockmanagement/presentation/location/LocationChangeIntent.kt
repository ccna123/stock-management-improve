package com.example.sol_denka_stockmanagement.presentation.location

import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel

sealed interface LocationChangeIntent {
    data class Execute(
        val memo: String,
        val locationId: Int,
        val scannedAt: String,
        val executedAt: String,
        val sourceEventIdByTagId: Map<Int, String>,
        val rfidTagList: List<TagMasterModel>
    ) : LocationChangeIntent
    data object Retry : LocationChangeIntent
}