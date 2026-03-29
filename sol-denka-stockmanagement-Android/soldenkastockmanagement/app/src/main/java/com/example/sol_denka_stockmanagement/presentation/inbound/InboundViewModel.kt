package com.example.sol_denka_stockmanagement.presentation.inbound

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.CsvTaskType
import com.example.sol_denka_stockmanagement.domain.repository.item.IItemCategoryRepository
import com.example.sol_denka_stockmanagement.domain.repository.item.IItemTypeRepository
import com.example.sol_denka_stockmanagement.domain.repository.location.ILocationMasterRepository
import com.example.sol_denka_stockmanagement.domain.repository.winder.IWinderRepository
import com.example.sol_denka_stockmanagement.domain.usecase.csv.SaveCsvUseCase
import com.example.sol_denka_stockmanagement.domain.usecase.inbound.SaveInboundUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InboundViewModel @Inject constructor(
    private val saveInboundUseCase: SaveInboundUseCase,
    private val saveCsvUseCase: SaveCsvUseCase,
    private val locationRepo: ILocationMasterRepository,
    private val winderRepo: IWinderRepository,
    private val itemCategoryRepo: IItemCategoryRepository,
    private val itemTypeRepo: IItemTypeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InboundUiState())
    val uiState: StateFlow<InboundUiState> = _uiState.asStateFlow()

    val locationMaster = locationRepo.get()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val winderMaster = winderRepo.get()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val itemCategoryMaster = itemCategoryRepo.get()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var lastExecuteIntent: InboundIntent.Execute? = null

    fun onIntent(intent: InboundIntent) {
        when (intent) {
            is InboundIntent.CategoryChanged -> {
                _uiState.update {
                    it.copy(
                        category = intent.value,
                        categoryId = intent.categoryId,
                        categoryExpanded = false,
                        itemInCategory = "",
                        searchResults = emptyList()
                    )
                }
            }
            is InboundIntent.ItemInCategoryChanged -> _uiState.update {
                it.copy(itemInCategory = intent.itemName)
            }
            is InboundIntent.SearchKeywordChanged -> {
                _uiState.update { it.copy(itemInCategory = intent.keyword) }
                searchItems(intent.keyword, intent.categoryName)
            }
            is InboundIntent.LocationChanged -> _uiState.update {
                it.copy(location = intent.location, locationExpanded = false)
            }
            is InboundIntent.WinderChanged -> _uiState.update {
                it.copy(winder = intent.winder, winderExpanded = false)
            }
            is InboundIntent.WeightChanged -> _uiState.update { it.copy(weight = intent.value) }
            is InboundIntent.WidthChanged -> _uiState.update { it.copy(width = intent.value) }
            is InboundIntent.LengthChanged -> _uiState.update { it.copy(length = intent.value) }
            is InboundIntent.ThicknessChanged -> _uiState.update { it.copy(thickness = intent.value) }
            is InboundIntent.LotNoChanged -> _uiState.update { it.copy(lotNo = intent.value) }
            is InboundIntent.OccurrenceReasonChanged -> _uiState.update { it.copy(occurrenceReason = intent.value) }
            is InboundIntent.QuantityChanged -> _uiState.update { it.copy(quantity = intent.value) }
            is InboundIntent.MemoChanged -> _uiState.update { it.copy(memo = intent.value) }
            is InboundIntent.OccurredAtDateChanged -> _uiState.update { it.copy(occurredAtDate = intent.value) }
            is InboundIntent.OccurredAtTimeChanged -> _uiState.update { it.copy(occurredAtTime = intent.value) }
            is InboundIntent.ProcessedAtDateChanged -> _uiState.update { it.copy(processedAtDate = intent.value) }
            is InboundIntent.ProcessedAtTimeChanged -> _uiState.update { it.copy(processedAtTime = intent.value) }
            is InboundIntent.ToggleCategoryExpanded -> _uiState.update { it.copy(categoryExpanded = !it.categoryExpanded) }
            is InboundIntent.ToggleLocationExpanded -> _uiState.update { it.copy(locationExpanded = !it.locationExpanded) }
            is InboundIntent.ToggleWinderExpanded -> _uiState.update { it.copy(winderExpanded = !it.winderExpanded) }
            is InboundIntent.ToggleDatePicker -> _uiState.update {
                it.copy(showDatePicker = intent.show, inboundInputFieldDateTime = intent.field)
            }
            is InboundIntent.ToggleTimePicker -> _uiState.update {
                it.copy(showTimePicker = intent.show, inboundInputFieldDateTime = intent.field)
            }
            is InboundIntent.UpdateFieldErrors -> _uiState.update { it.copy(fieldErrors = intent.errors) }
            is InboundIntent.Execute -> {
                lastExecuteIntent = intent
                execute(intent)
            }
            is InboundIntent.Retry -> lastExecuteIntent?.let { execute(it) }
            is InboundIntent.ResetState -> _uiState.value = InboundUiState()
            is InboundIntent.EventConsumed -> _uiState.update { it.copy(event = null) }
        }
    }

    private fun searchItems(keyword: String, categoryName: String) {
        viewModelScope.launch {
            runCatching {
                val categoryId = itemCategoryRepo.getIdByName(categoryName)
                val results = itemTypeRepo.getItemTypeByCategoryId(categoryId)
                    .filter { it.itemTypeName.contains(keyword, ignoreCase = true) }
                _uiState.update { it.copy(searchResults = results) }
            }
        }
    }

    private fun execute(intent: InboundIntent.Execute) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val state = _uiState.value

            // 1. Save to DB
            val dbResult = saveInboundUseCase.saveToDb(
                itemInCategory = state.itemInCategory,
                locationId = state.location?.locationId ?: 0,
                winderId = state.winder?.winderId,
                weight = state.weight,
                width = state.width,
                length = state.length,
                thickness = state.thickness,
                lotNo = state.lotNo,
                occurrenceReason = state.occurrenceReason,
                quantity = state.quantity,
                memo = state.memo,
                sourceEventId = intent.sourceEventId,
                occurredAt = intent.occurredAt,
                processedAt = intent.processedAt,
                registeredAt = intent.now,
                executedAt = intent.now,
                rfidTag = intent.rfidTag
            )

            if (dbResult.isFailure) {
                _uiState.update { it.copy(isLoading = false, event = InboundEvent.SaveDbFailed) }
                return@launch
            }

            // 2. Generate CSV
            val csvData = saveInboundUseCase.generateCsvData(
                itemInCategory = state.itemInCategory,
                locationId = state.location?.locationId ?: 0,
                winderId = state.winder?.winderId,
                weight = state.weight,
                width = state.width,
                length = state.length,
                thickness = state.thickness,
                lotNo = state.lotNo,
                occurrenceReason = state.occurrenceReason,
                quantity = state.quantity,
                memo = state.memo,
                sourceEventId = intent.sourceEventId,
                occurredAt = intent.occurredAt,
                processedAt = intent.processedAt,
                registeredAt = intent.now,
                rfidTag = intent.rfidTag
            )

            // 3. Save CSV
            val csvResult = saveCsvUseCase(
                taskCode = CsvTaskType.IN,
                direction = CsvHistoryDirection.EXPORT,
                data = csvData,
                onProgress = { _uiState.update { s -> s.copy(progress = it) } }
            )

            val event = if (csvResult.isSuccess) InboundEvent.SaveCsvSuccess
            else InboundEvent.SaveCsvFailed

            _uiState.update { it.copy(isLoading = false, event = event) }
        }
    }
}