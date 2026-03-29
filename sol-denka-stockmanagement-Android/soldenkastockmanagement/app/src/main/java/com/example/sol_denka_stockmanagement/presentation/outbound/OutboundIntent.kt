package com.example.sol_denka_stockmanagement.presentation.outbound

import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel

sealed interface OutboundIntent {
    data class Execute(
        val memo: String,
        val processedAt: String?,
        val registeredAt: String,
        val executedAt: String,
        val sourceEventIdByTagId: Map<Int, String>,
        val rfidTagList: List<TagMasterModel>
    ) : OutboundIntent
    data object Retry : OutboundIntent
    data class MemoChanged(val value: String) : OutboundIntent
    data class ProcessedAtDateChanged(val value: String) : OutboundIntent
    data class ProcessedAtTimeChanged(val value: String) : OutboundIntent
    data class ToggleDatePicker(val show: Boolean) : OutboundIntent
    data class ToggleTimePicker(val show: Boolean) : OutboundIntent
    data object EventConsumed : OutboundIntent
}