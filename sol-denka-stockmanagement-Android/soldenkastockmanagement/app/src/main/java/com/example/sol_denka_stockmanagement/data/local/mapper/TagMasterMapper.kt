package com.example.sol_denka_stockmanagement.data.local.mapper

import com.example.sol_denka_stockmanagement.database.entity.tag.TagMasterEntity
import com.example.sol_denka_stockmanagement.domain.model.common.AdditionalFieldsModel
import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel

class TagMasterMapper {

    fun TagMasterEntity.toModel() = TagMasterModel(
        tagId = tagId,
        epc = epc,
        tagStatusId = tagStatusId,
        memo =  memo,
        newFields = AdditionalFieldsModel.default()
    )

    fun TagMasterModel.toEntity() = TagMasterEntity(
        tagId = tagId,
        epc = epc,
        tagStatusId = tagStatusId,
        memo = memo
    )
}