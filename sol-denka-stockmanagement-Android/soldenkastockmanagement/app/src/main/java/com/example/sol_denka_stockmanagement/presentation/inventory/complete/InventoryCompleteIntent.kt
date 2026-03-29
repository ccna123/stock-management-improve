package com.example.sol_denka_stockmanagement.presentation.inventory.complete

import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel

sealed interface InventoryCompleteIntent {
    data class ComputeResult(
        val rfidTagList: List<TagMasterModel>,
        val locationName: String
    ) : InventoryCompleteIntent

    data class Execute(
        val memo: String,
        val locationId: Int,
        val scannedAt: String,
        val executedAt: String,
        val rfidTagList: List<TagMasterModel>
    ) : InventoryCompleteIntent

    data object Retry : InventoryCompleteIntent
    data class MemoChanged(val value: String) : InventoryCompleteIntent
    data object EventConsumed : InventoryCompleteIntent
}