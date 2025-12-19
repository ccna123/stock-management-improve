package com.example.sol_denka_stockmanagement.util

fun String?.toNullIfBlank(): String? = this?.trim()?.takeIf { it.isNotEmpty() }