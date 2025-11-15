package com.example.sol_denka_stockmanagement.helper

import com.example.sol_denka_stockmanagement.constant.StatusCode


sealed class ProcessResult{
    data class Success(val statusCode: StatusCode = StatusCode.OK) : ProcessResult()
    data class Failure(val statusCode: StatusCode = StatusCode.FAILED, val message: String) : ProcessResult()
}