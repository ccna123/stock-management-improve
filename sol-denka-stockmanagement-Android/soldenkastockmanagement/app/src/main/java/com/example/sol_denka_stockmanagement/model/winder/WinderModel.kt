package com.example.sol_denka_stockmanagement.model.winder

import com.example.sol_denka_stockmanagement.database.entity.winder.WinderEntity

data class WinderModel(
    val winderId: Int,
    val winderName: String,
    val createdAt: String?,
    val updatedAt: String
)

fun WinderEntity.toModel() = WinderModel(
    winderId = winderId,
    winderName = winderName,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun WinderModel.toEntity() = WinderEntity(
    winderId = winderId,
    winderName = winderName,
    createdAt = createdAt,
    updatedAt = updatedAt
)
