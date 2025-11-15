package com.example.sol_denka_stockmanagement.helper

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

object ToastManager {
    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage

    private val _showMessage = MutableStateFlow(false)
    val showMessage: StateFlow<Boolean> = _showMessage

    private val _type = MutableStateFlow(ToastType.INFO)
    val type: StateFlow<ToastType> = _type


    fun showToast(message: String, type: ToastType) {
        _toastMessage.update { message }
        _showMessage.update { true }
        _type.update { type }
    }

    fun hideToast() {
        _showMessage.update { false }
        _toastMessage.update { "" }
    }
}