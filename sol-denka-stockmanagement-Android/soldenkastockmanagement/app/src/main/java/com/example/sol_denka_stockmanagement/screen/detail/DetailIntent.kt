package com.example.sol_denka_stockmanagement.screen.detail

sealed class DetailIntent(){
    data object Prev: DetailIntent()
    data object Next: DetailIntent()
}
