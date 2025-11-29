package com.example.sol_denka_stockmanagement.model.common

import com.example.sol_denka_stockmanagement.constant.TagStatus

data class AdditionalFieldsModel(
    var tagStatus: TagStatus,
    val rssi: Float

){
    companion object {
        fun default() = AdditionalFieldsModel(
            tagStatus = TagStatus.UNPROCESSED,
            rssi = -100f
        )
    }
}
