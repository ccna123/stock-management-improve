package com.example.sol_denka_stockmanagement.app_interface

import android.content.Context

interface IFileOperation<T> {
    fun save(context: Context, model: T): Boolean
    fun load(context: Context): T
}