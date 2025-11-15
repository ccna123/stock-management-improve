package com.example.sol_denka_stockmanagement.state

import com.example.sol_denka_stockmanagement.constant.FileTransferMethod
import com.example.sol_denka_stockmanagement.constant.SelectTitle

data class InputState(
    val stockArea: String = "",
    val thickness: String = "",
    val grade: String = "",
    val length: String = "",
    val remark: String = "",
    val materialSelectedItem: String = SelectTitle.SelectMaterial.displayName,
    val handlingMethod: String = "",
    val rollingMachineInfo: String = "",
    val weight: String = "",
    val lotNo: String = "",
    val packingStyle: String = "",
    var fileTransferMethod: String = FileTransferMethod.WIFI.displayName
)
