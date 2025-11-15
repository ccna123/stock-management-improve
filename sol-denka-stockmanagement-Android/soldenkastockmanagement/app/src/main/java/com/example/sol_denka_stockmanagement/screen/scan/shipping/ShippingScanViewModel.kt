package com.example.sol_denka_stockmanagement.screen.scan.shipping

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShippingScanViewModel @Inject constructor() : ViewModel() {

    var selectedTags = mutableStateOf<Set<String>>(emptySet())
        private set

    var isAllSelected = mutableStateOf(false)
        private set

    fun handle(intent: ShippingScanIntent) {
        when (intent) {
            ShippingScanIntent.ClearTagSelectionList -> selectedTags.value = emptySet()
            is ShippingScanIntent.ToggleSelectionAll -> {
                if (isAllSelected.value) {
                    // UNTICK ALL
                    selectedTags.value = emptySet()
                    isAllSelected.value = false
                } else {
                    // SELECT ALL
                    selectedTags.value = intent.tagList
                    isAllSelected.value = true
                }
            }
            is ShippingScanIntent.ToggleTagSelection -> {
                val updated = selectedTags.value.toMutableSet()
                if (intent.tag in updated) updated.remove(intent.tag)
                else updated.add(intent.tag)

                selectedTags.value = updated
                isAllSelected.value = updated.size == intent.totalTag
            }

            ShippingScanIntent.ResetState -> {
                selectedTags.value = emptySet()
                isAllSelected.value = false
            }
        }
    }
}