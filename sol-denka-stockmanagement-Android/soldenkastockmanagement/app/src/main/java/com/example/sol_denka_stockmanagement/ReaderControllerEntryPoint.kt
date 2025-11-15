package com.example.sol_denka_stockmanagement

import com.example.sol_denka_stockmanagement.helper.ReaderController
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ReaderControllerEntryPoint {
    fun readerController(): ReaderController
}