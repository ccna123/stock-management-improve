package com.example.sol_denka_stockmanagement.model.process

import com.example.sol_denka_stockmanagement.constant.ProcessMethod
import com.example.sol_denka_stockmanagement.database.entity.process.ProcessTypeEntity

data class ProcessTypeModel(
    val processTypeId: Int,
    val processCode: ProcessMethod,
    val processName: String,
    val createdAt: String,
    val updatedAt: String,
)

fun ProcessTypeEntity.toModel() = ProcessTypeModel(
    processTypeId = processTypeId,
    processCode = processCode,
    processName = processName,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun ProcessTypeModel.toEntity() = ProcessTypeEntity(
    processTypeId = processTypeId,
    processCode = processCode,
    processName = processName,
    createdAt = createdAt,
    updatedAt = updatedAt
)
