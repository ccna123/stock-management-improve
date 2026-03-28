package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.database.entity.tag.TagStatusMasterEntity
import com.example.sol_denka_stockmanagement.domain.model.tag.TagStatusMasterModel

class TagStatusMasterMapper {
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
}