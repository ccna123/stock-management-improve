package com.example.sol_denka_stockmanagement.model.tag

import com.example.sol_denka_stockmanagement.database.entity.tag.TagMasterEntity

data class TagMasterModel(
    val tagId: Int,
    val ledgerItemId: Int?,
    val epc: String,
    val createdAt: String,
    val updatedAt: String,
)

fun TagMasterEntity.toModel() = TagMasterModel(
    tagId,
    ledgerItemId,
    epc,
    createdAt,
    updatedAt
)

fun TagMasterModel.toEntity() = TagMasterEntity(
    tagId,
    ledgerItemId,
    epc,
    createdAt,
    updatedAt
)
