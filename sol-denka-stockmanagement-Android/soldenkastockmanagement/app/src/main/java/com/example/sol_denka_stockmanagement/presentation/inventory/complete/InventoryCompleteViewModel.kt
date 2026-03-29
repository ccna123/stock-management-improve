package com.example.sol_denka_stockmanagement.presentation.inventory.complete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.CsvTaskType
import com.example.sol_denka_stockmanagement.constant.InventoryResultType
import com.example.sol_denka_stockmanagement.constant.TagScanStatus
import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel
import com.example.sol_denka_stockmanagement.domain.usecase.csv.SaveCsvUseCase
import com.example.sol_denka_stockmanagement.domain.usecase.inventory.SaveInventoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class InventoryCompleteViewModel @Inject constructor(
    private val saveInventoryUseCase: SaveInventoryUseCase,
    private val saveCsvUseCase: SaveCsvUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InventoryCompleteUiState())
    val uiState: StateFlow<InventoryCompleteUiState> = _uiState.asStateFlow()

    private var lastExecuteIntent: InventoryCompleteIntent.Execute? = null

    fun onIntent(intent: InventoryCompleteIntent) {
        when (intent) {
            is InventoryCompleteIntent.ComputeResult -> computeResult(intent.rfidTagList, intent.locationName)
            is InventoryCompleteIntent.Execute -> {
                lastExecuteIntent = intent
                execute(intent)
            }
            is InventoryCompleteIntent.Retry -> lastExecuteIntent?.let { execute(it) }
            is InventoryCompleteIntent.MemoChanged -> _uiState.update { it.copy(memo = intent.value) }
            is InventoryCompleteIntent.EventConsumed -> _uiState.update { it.copy(event = null) }
        }
    }

    private fun computeResult(rfidTagList: List<TagMasterModel>, locationName: String) {
        viewModelScope.launch {
            val taggedList = rfidTagList.map { tag ->
                val wrongLocation = tag.newFields.tagScanStatus == TagScanStatus.PROCESSED && tag.newFields.location != locationName
                val shortage = tag.newFields.tagScanStatus != TagScanStatus.PROCESSED && tag.newFields.isInStock && tag.newFields.location == locationName
                val over = tag.newFields.tagScanStatus == TagScanStatus.PROCESSED && !tag.newFields.isInStock && tag.newFields.location == locationName
                val ok = tag.newFields.tagScanStatus == TagScanStatus.PROCESSED && tag.newFields.isInStock && tag.newFields.location == locationName

                val resultType = when {
                    wrongLocation -> InventoryResultType.FOUND_WRONG_LOCATION
                    shortage -> InventoryResultType.NOT_FOUND
                    over -> InventoryResultType.FOUND_OVER_STOCK
                    ok -> InventoryResultType.FOUND_OK
                    else -> InventoryResultType.UNKNOWN
                }
                tag.copy(newFields = tag.newFields.copy(inventoryResultType = resultType))
            }

            _uiState.update {
                it.copy(
                    okCount = taggedList.count { t -> t.newFields.inventoryResultType == InventoryResultType.FOUND_OK },
                    shortageCount = taggedList.count { t -> t.newFields.inventoryResultType == InventoryResultType.NOT_FOUND },
                    overCount = taggedList.count { t -> t.newFields.inventoryResultType == InventoryResultType.FOUND_OVER_STOCK },
                    wrongLocationCount = taggedList.count { t -> t.newFields.inventoryResultType == InventoryResultType.FOUND_WRONG_LOCATION }
                )
            }
        }
    }

    private fun execute(intent: InventoryCompleteIntent.Execute) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val sourceSessionUuid = UUID.randomUUID().toString()

            // 1. Save to DB
            val dbResult = saveInventoryUseCase.saveToDb(
                memo = intent.memo,
                sourceSessionUuid = sourceSessionUuid,
                scannedAt = intent.scannedAt,
                executedAt = intent.executedAt,
                locationId = intent.locationId,
                rfidTagList = intent.rfidTagList
            )

            if (dbResult.isFailure) {
                _uiState.update { it.copy(isLoading = false, event = InventoryCompleteEvent.SaveDbFailed) }
                return@launch
            }

            // 2. Generate CSV
            val csvData = saveInventoryUseCase.generateCsvData(
                memo = intent.memo,
                sourceSessionUuid = sourceSessionUuid,
                scannedAt = intent.scannedAt,
                executedAt = intent.executedAt,
                locationId = intent.locationId,
                rfidTagList = intent.rfidTagList
            )

            // 3. Save CSV
            val csvResult = saveCsvUseCase(
                taskCode = CsvTaskType.INVENTORY,
                direction = CsvHistoryDirection.EXPORT,
                data = csvData,
                onProgress = { _uiState.update { s -> s.copy(progress = it) } }
            )

            val event = if (csvResult.isSuccess)
                InventoryCompleteEvent.SaveCsvSuccess
            else
                InventoryCompleteEvent.SaveCsvFailed

            _uiState.update { it.copy(isLoading = false, event = event) }
        }
    }
}