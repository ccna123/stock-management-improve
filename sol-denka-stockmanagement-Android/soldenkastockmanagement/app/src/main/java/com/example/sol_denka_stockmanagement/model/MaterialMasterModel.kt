package com.example.sol_denka_stockmanagement.model

import com.example.sol_denka_stockmanagement.database.entity.MaterialMasterEntity

data class MaterialMasterModel(
    val id: Int,
    val materialCode: String,
    val materialName: String,
    val createdAt: String,
    val updatedAt: String
)

fun MaterialMasterModel.toMaterialMasterEntity(): MaterialMasterEntity {
    return MaterialMasterEntity(
        id = this.id,
        materialCode = this.materialCode,
        materialName = this.materialName,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun MaterialMasterEntity.toMaterialMasterModel(): MaterialMasterModel {
    return MaterialMasterModel(
        id = this.id,
        materialCode = this.materialCode,
        materialName = this.materialName,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

