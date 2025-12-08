package com.example.sol_denka_stockmanagement.state

import com.example.sol_denka_stockmanagement.constant.FileTransferMethod
import com.example.sol_denka_stockmanagement.constant.MaterialSelectionItem
import com.example.sol_denka_stockmanagement.constant.ProcessMethod

data class InputState(
    val location: String = "",
    val thickness: String = "",
    val grade: String = "",
    val length: String = "",
    val memo: String = "",
    val category: String = "",
    val materialSelectedItem: String = MaterialSelectionItem.MISS_ROLL.displayName,
    val processMethod: String = ProcessMethod.USE.displayName,
    val winderInfo: String = "",
    val weight: String = "",
    val lotNo: String = "",
    val occurredAt: String = "",
    val packingStyle: String = "",
    var fileTransferMethod: String = FileTransferMethod.WIFI.displayName
)
