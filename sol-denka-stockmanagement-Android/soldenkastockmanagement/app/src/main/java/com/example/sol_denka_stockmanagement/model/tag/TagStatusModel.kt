package com.example.sol_denka_stockmanagement.model.tag

import com.example.sol_denka_stockmanagement.database.entity.tag.TagStatusMasterEntity

data class TagStatusMasterModel(
    val tagStatusId: Int,
    val statusCode: String,
    val statusName: String,
    val createdAt: String,
    val updatedAt: String,
)

fun TagStatusMasterEntity.toModel() = TagStatusMasterModel(
    tagStatusId = tagStatusId,
    statusCode = statusCode,
    statusName = statusName,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun TagStatusMasterModel.toEntity() = TagStatusMasterEntity(
    tagStatusId = tagStatusId,
    statusCode = statusCode,
    statusName = statusName,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
