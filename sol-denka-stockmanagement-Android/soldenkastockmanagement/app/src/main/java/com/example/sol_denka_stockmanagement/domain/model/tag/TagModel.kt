package com.example.sol_denka_stockmanagement.domain.model.tag

import com.example.sol_denka_stockmanagement.domain.model.common.AdditionalFieldsModel

data class TagMasterModel(
    val tagId: Int,
    val tagStatusId: Int,
    val epc: String,
    val memo: String?,
    val newFields: AdditionalFieldsModel = AdditionalFieldsModel.default()
)
