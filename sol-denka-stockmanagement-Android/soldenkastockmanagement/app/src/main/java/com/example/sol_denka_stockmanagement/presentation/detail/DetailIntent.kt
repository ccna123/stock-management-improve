package com.example.sol_denka_stockmanagement.presentation.detail

sealed interface DetailIntent {
    data object Prev : DetailIntent
    data class Next(val lastItemIndex: Int) : DetailIntent
    data class Init(val totalCount: Int) : DetailIntent
}
