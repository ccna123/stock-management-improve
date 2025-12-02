package com.example.sol_denka_stockmanagement.model.process

import com.example.sol_denka_stockmanagement.constant.ProcessMethod
import com.example.sol_denka_stockmanagement.database.entity.process.ProcessTypeEntity

data class ProcessTypeModel(
    val processTypeId: Int = 0,
    val processCode: ProcessMethod,
    val processName: String,
    val createdAt: String,
    val updatedAt: String,
)

fun ProcessTypeEntity.toModel() = ProcessTypeModel(
    processTypeId,
    processCode,
    processName,
    createdAt,
    updatedAt
)

fun ProcessTypeModel.toEntity() = ProcessTypeEntity(
    processTypeId,
    processCode,
    processName,
    createdAt,
    updatedAt
)
