package com.example.sol_denka_stockmanagement.state

import com.example.sol_denka_stockmanagement.constant.FileTransferMethod
import com.example.sol_denka_stockmanagement.constant.ProcessMethod

data class InputState(
    val location: String = "",
    val thickness: String = "",
    val grade: String = "",
    val length: String = "",
    val itemInCategory: String = "",
    val memo: String = "",
    val category: String = "",
    val processMethod: String = ProcessMethod.USE.displayName,
    val winderInfo: String = "",
    val weight: String = "",
    val lotNo: String = "",
    val occurredAtDate: String = "",
    val occurredAtTime: String = "",
    val packingType: String = "",
    val width: String = "",
    val occurrenceReason: String = "",
    val specificGravity: String = "",
    var fileTransferMethod: String = FileTransferMethod.WIFI.displayName
)
