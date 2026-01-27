package com.example.sol_denka_stockmanagement.model.winder

import com.example.sol_denka_stockmanagement.database.entity.winder.WinderInfoEntity

data class WinderModel(
    val winderId: Int,
    val winderName: String,
    val createdAt: String?,
    val updatedAt: String
)

fun WinderInfoEntity.toModel() = WinderModel(
    winderId = winderId,
    winderName = winderName,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun WinderModel.toEntity() = WinderInfoEntity(
    winderId = winderId,
    winderName = winderName,
    createdAt = createdAt,
    updatedAt = updatedAt
)
