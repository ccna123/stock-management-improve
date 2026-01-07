package com.example.sol_denka_stockmanagement.state

import com.example.sol_denka_stockmanagement.constant.FileTransferMethod
import com.example.sol_denka_stockmanagement.constant.ProcessMethod
import com.example.sol_denka_stockmanagement.model.location.LocationMasterModel
import com.example.sol_denka_stockmanagement.model.winder.WinderInfoModel

data class InputState(
    val location: LocationMasterModel? = null,
    val thickness: String = "",
    val grade: String = "",
    val length: String = "",
    val itemInCategory: String = "",
    val memo: String = "",
    val category: String = "",
    val processMethod: String = ProcessMethod.USE.displayName,
    val winder: WinderInfoModel? = null,
    val weight: String = "",
    val lotNo: String = "",
    val quantity: String = "",
    val occurredAtDate: String = "",
    val occurredAtTime: String = "",
    val processedAtDate: String = "",
    val processedAtTime: String = "",
    val packingType: String = "",
    val width: String = "",
    val occurrenceReason: String = "",
    val specificGravity: String = "",
    var fileTransferMethod: String = FileTransferMethod.WIFI.displayName,
    val fieldErrors: Map<String, Boolean> = emptyMap()
)
