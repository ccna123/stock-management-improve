package com.example.sol_denka_stockmanagement.model.winder

import com.example.sol_denka_stockmanagement.database.entity.winder.WinderInfoEntity

data class WinderInfoModel(
    val winderId: Int,
    val winderName: String,
    val createdAt: String?,
    val updatedAt: String
)

fun WinderInfoEntity.toModel() = WinderInfoModel(
    winderId = winderId,
    winderName = winderName,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun WinderInfoModel.toEntity() = WinderInfoEntity(
    winderId = winderId,
    winderName = winderName,
    createdAt = createdAt,
    updatedAt = updatedAt
)
