package com.example.sol_denka_stockmanagement.screen.detail

import androidx.lifecycle.ViewModel
import com.example.sol_denka_stockmanagement.model.InventoryItemMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(): ViewModel() {

    private val _selectedList = MutableStateFlow<List<InventoryItemMasterModel>>(emptyList())
    val selectedList: StateFlow<List<InventoryItemMasterModel>> = _selectedList

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    fun setItems(items: List<InventoryItemMasterModel>) {
        _selectedList.value = items
        _currentIndex.value = 0
    }

    fun handle(intent: DetailIntent) {
        when (intent) {
            DetailIntent.Prev -> {
                _currentIndex.update { maxOf(0, it - 1) }
            }
            DetailIntent.Next -> {
                val lastIndex = _selectedList.value.lastIndex
                _currentIndex.update { minOf(lastIndex, it + 1) }
            }
        }
    }
}