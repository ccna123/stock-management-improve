package com.example.sol_denka_stockmanagement.presentation.inbound

import com.example.sol_denka_stockmanagement.domain.model.location.LocationMasterModel
import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel
import com.example.sol_denka_stockmanagement.domain.model.winder.WinderModel

sealed interface InboundIntent {
    // input
    data class CategoryChanged(val categoryId: Int, val value: String) : InboundIntent
    data class ItemInCategoryChanged(val itemName: String, val itemId: Int) : InboundIntent
    data class SearchKeywordChanged(val keyword: String, val categoryName: String) : InboundIntent
    data class LocationChanged(val location: LocationMasterModel?) : InboundIntent
    data class WinderChanged(val winder: WinderModel?) : InboundIntent
    data class WeightChanged(val value: String) : InboundIntent
    data class WidthChanged(val value: String) : InboundIntent
    data class LengthChanged(val value: String) : InboundIntent
    data class ThicknessChanged(val value: String) : InboundIntent
    data class LotNoChanged(val value: String) : InboundIntent
    data class OccurrenceReasonChanged(val value: String) : InboundIntent
    data class QuantityChanged(val value: String) : InboundIntent
    data class MemoChanged(val value: String) : InboundIntent
    data class OccurredAtDateChanged(val value: String) : InboundIntent
    data class OccurredAtTimeChanged(val value: String) : InboundIntent
    data class ProcessedAtDateChanged(val value: String) : InboundIntent
    data class ProcessedAtTimeChanged(val value: String) : InboundIntent
    // expand
    data object ToggleCategoryExpanded : InboundIntent
    data object ToggleLocationExpanded : InboundIntent
    data object ToggleWinderExpanded : InboundIntent
    // picker
    data class ToggleDatePicker(val field: String, val show: Boolean) : InboundIntent
    data class ToggleTimePicker(val field: String, val show: Boolean) : InboundIntent
    // form errors
    data class UpdateFieldErrors(val errors: Map<String, List<String>>) : InboundIntent
    // execute
    data class Execute(
        val sourceEventId: String,
        val occurredAt: String?,
        val processedAt: String?,
        val now: String,
        val rfidTag: TagMasterModel?
    ) : InboundIntent
    data object Retry : InboundIntent
    data object ResetState : InboundIntent
    data object EventConsumed : InboundIntent
}