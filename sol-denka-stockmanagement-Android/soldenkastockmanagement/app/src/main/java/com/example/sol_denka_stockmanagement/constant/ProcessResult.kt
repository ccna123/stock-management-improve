package com.example.sol_denka_stockmanagement.constant

sealed class ProcessResult{
    data class Success(val statusCode: StatusCode = StatusCode.OK) : ProcessResult()
    data class Failure(val statusCode: StatusCode, val rawMessage: String? = null) : ProcessResult()
}