package com.example.sol_denka_stockmanagement.state

import com.example.sol_denka_stockmanagement.constant.FileTransferMethod
import com.example.sol_denka_stockmanagement.constant.MaterialSelectionItem

data class InputState(
    val location: String = "",
    val thickness: String = "",
    val grade: String = "",
    val length: String = "",
    val remark: String = "",
    val materialSelectedItem: String = MaterialSelectionItem.MISS_ROLL.displayName,
    val handlingMethod: String = "",
    val rollingMachineInfo: String = "",
    val weight: String = "",
    val lotNo: String = "",
    val packingStyle: String = "",
    var fileTransferMethod: String = FileTransferMethod.WIFI.displayName
)
