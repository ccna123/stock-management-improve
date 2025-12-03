package com.example.sol_denka_stockmanagement.app_interface

import com.example.sol_denka_stockmanagement.constant.TagStatus

interface ITagOperation {

    fun updateTagStatus(epc: String, status: TagStatus)
    fun updateRssi(epc: String, rssi: Float)
    fun clearTagStatusAndRssi()
}