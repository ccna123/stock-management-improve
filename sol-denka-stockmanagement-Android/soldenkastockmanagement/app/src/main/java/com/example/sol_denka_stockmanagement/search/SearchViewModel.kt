package com.example.sol_denka_stockmanagement.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sol_denka_stockmanagement.model.InventoryItemMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    var foundTags = mutableStateOf(listOf<InventoryItemMasterModel>())
        private set


    fun onIntent(intent: SearchScreenIntent){
        when(intent){
            is SearchScreenIntent.ToggleFoundTag -> {
                val current = foundTags.value.toMutableList()
                if (intent.tag in current) current.remove(intent.tag) else current.add(intent.tag)
                foundTags.value = current
            }

            SearchScreenIntent.ClearFoundTag -> {
                foundTags.value = listOf()
            }
        }
    }
}