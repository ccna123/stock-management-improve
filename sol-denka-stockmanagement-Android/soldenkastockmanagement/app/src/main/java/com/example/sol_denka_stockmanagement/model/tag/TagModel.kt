package com.example.sol_denka_stockmanagement.model.tag

import com.example.sol_denka_stockmanagement.database.entity.tag.TagMasterEntity
import com.example.sol_denka_stockmanagement.model.common.AdditionalFieldsModel

data class TagMasterModel(
    val tagId: Int,
    val tagStatusId: Int,
    val epc: String,
    val memo: String?,
    val newFields: AdditionalFieldsModel = AdditionalFieldsModel.default()
)

fun TagMasterEntity.toModel() = TagMasterModel(
    tagId = tagId,
    epc = epc,
    tagStatusId = tagStatusId,
    memo =  memo,
    newFields = AdditionalFieldsModel.default()
)

fun TagMasterModel.toEntity() = TagMasterEntity(
    tagId = tagId,
    epc = epc,
    tagStatusId = tagStatusId,
    memo = memo
)
