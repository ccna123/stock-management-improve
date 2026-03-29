package com.example.sol_denka_stockmanagement.presentation.detail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun onIntent(intent: DetailIntent) {
        when (intent) {
            is DetailIntent.Init -> _uiState.update {
                it.copy(currentIndex = 0, totalCount = intent.totalCount)
            }
            is DetailIntent.Prev -> _uiState.update {
                it.copy(currentIndex = (it.currentIndex - 1).coerceAtLeast(0))
            }
            is DetailIntent.Next -> _uiState.update {
                it.copy(currentIndex = (it.currentIndex + 1).coerceAtMost(intent.lastItemIndex))
            }
        }
    }
}