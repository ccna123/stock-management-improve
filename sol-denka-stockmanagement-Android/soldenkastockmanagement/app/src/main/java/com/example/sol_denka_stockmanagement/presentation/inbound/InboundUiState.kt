package com.example.sol_denka_stockmanagement.presentation.inbound

import com.example.sol_denka_stockmanagement.domain.model.inbound.InboundInputFormModel
import com.example.sol_denka_stockmanagement.domain.model.item.ItemTypeMasterModel
import com.example.sol_denka_stockmanagement.domain.model.location.LocationMasterModel
import com.example.sol_denka_stockmanagement.domain.model.winder.WinderModel

data class InboundUiState(
    // input fields
    val category: String = "",
    val categoryId: Int = 0,
    val itemInCategory: String = "",
    val location: LocationMasterModel? = null,
    val winder: WinderModel? = null,
    val weight: String = "",
    val width: String = "",
    val length: String = "",
    val thickness: String = "",
    val lotNo: String = "",
    val occurrenceReason: String = "",
    val quantity: String = "",
    val memo: String = "",
    val occurredAtDate: String = "",
    val occurredAtTime: String = "",
    val processedAtDate: String = "",
    val processedAtTime: String = "",
    // expand state
    val categoryExpanded: Boolean = false,
    val locationExpanded: Boolean = false,
    val winderExpanded: Boolean = false,
    // picker state
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val inboundInputFieldDateTime: String = "",
    // search
    val searchResults: List<ItemTypeMasterModel> = emptyList(),
    // form
    val inboundInputFormResults: List<InboundInputFormModel> = emptyList(),
    val fieldErrors: Map<String, List<String>> = emptyMap(),
    // loading
    val isLoading: Boolean = false,
    val progress: Float = 0f,
    // event
    val event: InboundEvent? = null
)
