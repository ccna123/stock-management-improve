package com.example.sol_denka_stockmanagement.model.tag

import com.example.sol_denka_stockmanagement.database.entity.tag.TagMasterEntity
import com.example.sol_denka_stockmanagement.model.common.AdditionalFieldsModel

data class TagMasterModel(
    val tagId: Int,
    val ledgerItemId: Int?,
    val epc: String,
    val createdAt: String,
    val updatedAt: String,
    val newFields: AdditionalFieldsModel = AdditionalFieldsModel.default()
)

fun TagMasterEntity.toModel() = TagMasterModel(
    tagId = tagId,
    ledgerItemId = ledgerItemId,
    epc = epc,
    createdAt = createdAt,
    updatedAt = updatedAt,
    newFields = AdditionalFieldsModel.default()
)

fun TagMasterModel.toEntity() = TagMasterEntity(
    tagId = tagId,
    ledgerItemId = ledgerItemId,
    epc = epc,
    createdAt = createdAt,
    updatedAt = updatedAt
)
