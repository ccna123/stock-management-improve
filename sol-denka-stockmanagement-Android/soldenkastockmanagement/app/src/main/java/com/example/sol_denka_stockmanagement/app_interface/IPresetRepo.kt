package com.example.sol_denka_stockmanagement.app_interface

interface IPresetRepo {
    suspend fun ensurePresetInserted()
}