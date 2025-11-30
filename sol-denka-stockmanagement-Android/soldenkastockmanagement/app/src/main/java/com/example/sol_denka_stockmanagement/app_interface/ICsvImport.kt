package com.example.sol_denka_stockmanagement.app_interface

interface ICsvImport {
    suspend fun import(csvLines: List<String>)
}