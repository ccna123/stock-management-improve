package com.example.sol_denka_stockmanagement.app_interface

interface ICsvExport {
    fun toHeader(): List<String>
    fun toRow(): List<String>
    fun toCsvType(): String
    fun toCsvName(): String
}
