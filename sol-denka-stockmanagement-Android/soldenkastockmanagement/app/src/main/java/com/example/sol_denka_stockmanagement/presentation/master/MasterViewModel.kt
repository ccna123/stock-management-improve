package com.example.sol_denka_stockmanagement.presentation.master

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.domain.model.item.ItemCategoryModel
import com.example.sol_denka_stockmanagement.domain.model.location.LocationMasterModel
import com.example.sol_denka_stockmanagement.domain.model.winder.WinderModel
import com.example.sol_denka_stockmanagement.domain.repository.item.IItemCategoryRepository
import com.example.sol_denka_stockmanagement.domain.repository.item.IItemTypeRepository
import com.example.sol_denka_stockmanagement.domain.repository.location.ILocationMasterRepository
import com.example.sol_denka_stockmanagement.domain.repository.process.IProcessTypeRepository
import com.example.sol_denka_stockmanagement.domain.repository.winder.IWinderRepository
import com.example.sol_denka_stockmanagement.domain.usecase.master.ValidateMasterUseCase
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.state.DialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MasterViewModel @Inject constructor(
    private val validateMaster: ValidateMasterUseCase,
    private val locationRepo: ILocationMasterRepository,
    private val itemCategoryRepo: IItemCategoryRepository,
    private val winderRepo: IWinderRepository,
    private val processTypeRepo: IProcessTypeRepository,
    private val itemTypeRepo: IItemTypeRepository
) : ViewModel() {

    private val _locationMaster = MutableStateFlow<List<LocationMasterModel>>(emptyList())
    val locationMaster = _locationMaster.asStateFlow()

    private val _itemCategoryMaster = MutableStateFlow<List<ItemCategoryModel>>(emptyList())
    val itemCategoryMaster = _itemCategoryMaster.asStateFlow()

    private val _winderMaster = MutableStateFlow<List<WinderModel>>(emptyList())
    val winderMaster = _winderMaster.asStateFlow()

    private val _dialogState = MutableStateFlow<DialogState>(DialogState.Hidden)
    val dialogState = _dialogState.asStateFlow()

    private val _navigateTo = MutableSharedFlow<Screen?>()
    val navigateTo = _navigateTo.asSharedFlow()

    init { loadMasterData() }

    private fun loadMasterData() {
        viewModelScope.launch {
            locationRepo.get().collect { _locationMaster.value = it }
        }
        viewModelScope.launch {
            itemCategoryRepo.get().collect { _itemCategoryMaster.value = it }
        }
        viewModelScope.launch {
            winderRepo.get().collect { _winderMaster.value = it }
        }
    }

    fun checkMasterAndNavigate(operation: String, screen: Screen) {
        viewModelScope.launch(Dispatchers.IO) {
            val missing = validateMaster(operation)  // gọi usecase
            if (missing.isEmpty()) {
                _navigateTo.emit(screen)
            } else {
                val message = buildString {
                    append("以下のマスタが連携されていません：\n\n")
                    missing.forEach { append("・$it\n") }
                    append("\nマスタの取込を行ってください")
                }
                _dialogState.value = DialogState.MasterInvalid(message = message)
            }
        }
    }
}