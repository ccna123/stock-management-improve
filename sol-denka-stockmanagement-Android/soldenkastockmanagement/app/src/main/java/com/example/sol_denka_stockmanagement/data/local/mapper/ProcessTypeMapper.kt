package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.data.local.entity.process.ProcessTypeEntity
import com.example.sol_denka_stockmanagement.domain.model.process.ProcessTypeModel

fun ProcessTypeEntity.toModel() =
    ProcessTypeModel(
        processTypeId = processTypeId,
        processCode = processCode,
        processName = processName,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

fun ProcessTypeModel.toEntity() =
    ProcessTypeEntity(
        processTypeId = processTypeId,
        processCode = processCode,
        processName = processName,
        createdAt = createdAt,
        updatedAt = updatedAt
    )