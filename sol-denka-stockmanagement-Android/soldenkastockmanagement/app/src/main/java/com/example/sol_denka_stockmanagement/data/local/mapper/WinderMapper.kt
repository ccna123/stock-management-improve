package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.database.entity.winder.WinderEntity
import com.example.sol_denka_stockmanagement.domain.model.winder.WinderModel

class WinderMapper {

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
}