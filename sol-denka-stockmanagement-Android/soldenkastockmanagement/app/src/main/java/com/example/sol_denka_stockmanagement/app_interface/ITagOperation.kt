package com.example.sol_denka_stockmanagement.app_interface

import com.example.sol_denka_stockmanagement.constant.TagScanStatus

interface ITagOperation {

    fun updateTagStatus(epc: String, status: TagScanStatus)
    fun updateRssi(epc: String, rssi: Float)
    fun clearTagStatusAndRssi()
}