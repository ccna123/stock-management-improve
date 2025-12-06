package com.example.sol_denka_stockmanagement.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.constant.ConnectionState
import com.example.sol_denka_stockmanagement.constant.CsvHistoryResult
import com.example.sol_denka_stockmanagement.constant.ProcessMethod
import com.example.sol_denka_stockmanagement.constant.StatusCode
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.repository.csv.CsvHistoryRepository
import com.example.sol_denka_stockmanagement.database.repository.csv.CsvTaskTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.inventory.InventoryResultTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.item.ItemUnitRepository
import com.example.sol_denka_stockmanagement.database.repository.location.LocationRepository
import com.example.sol_denka_stockmanagement.database.repository.process.ProcessTypeRepository
import com.example.sol_denka_stockmanagement.helper.NetworkConnectionObserver
import com.example.sol_denka_stockmanagement.helper.ProcessResult
import com.example.sol_denka_stockmanagement.helper.controller.ReaderController
import com.example.sol_denka_stockmanagement.helper.csv.CsvHelper
import com.example.sol_denka_stockmanagement.helper.message_mapper.MessageMapper
import com.example.sol_denka_stockmanagement.helper.toast.ToastType
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.model.csv.CsvHistoryModel
import com.example.sol_denka_stockmanagement.model.location.LocationMasterModel
import com.example.sol_denka_stockmanagement.model.reader.ReaderInfoModel
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting.AppSettingState
import com.example.sol_denka_stockmanagement.state.ErrorState
import com.example.sol_denka_stockmanagement.state.ExpandState
import com.example.sol_denka_stockmanagement.state.GeneralState
import com.example.sol_denka_stockmanagement.state.InputState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class AppViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val readerController: ReaderController,
    private val connectionObserver: NetworkConnectionObserver,
    private val itemUnitRepository: ItemUnitRepository,
    private val locationRepository: LocationRepository,
    private val processTypeRepository: ProcessTypeRepository,
    private val csvTaskTypeRepository: CsvTaskTypeRepository,
    private val inventoryResultTypeRepository: InventoryResultTypeRepository,
    private val csvHistoryRepository: CsvHistoryRepository,
    private val csvHelper: CsvHelper,
) : ViewModel() {


    // region State Definitions
    private val _generalState = MutableStateFlow(GeneralState())
    val generalState: StateFlow<GeneralState> = _generalState.asStateFlow()

    private val _expandState = MutableStateFlow(ExpandState())
    val expandState: StateFlow<ExpandState> = _expandState.asStateFlow()

    private val _inputState = MutableStateFlow(InputState())
    val inputState: StateFlow<InputState> = _inputState.asStateFlow()

    var showFileProgressDialog = mutableStateOf(false)

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress
    private val _isFileWorking = MutableStateFlow(false)
    val isFileWorking = _isFileWorking.asStateFlow()

    private val _toastFlow = MutableSharedFlow<Pair<String, ToastType>>()
    val toastFlow = _toastFlow

    val perTagProcessMethod = MutableStateFlow<Map<String, String>>(emptyMap())
    val perTagExpanded = MutableStateFlow<Map<String, Boolean>>(emptyMap())

    val showConnectingDialog = MutableStateFlow(false)

    var showAppDialog = mutableStateOf(false)
        private set

    var showClearTagConfirmDialog = mutableStateOf(false)
        private set

    var showRadioPowerChangeDialog = mutableStateOf(false)
        private set

    private val _perTagChecked = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val perTagChecked: StateFlow<Map<String, Boolean>> = _perTagChecked.asStateFlow()

    private val _isAllSelected = MutableStateFlow(false)
    val isAllSelected = _isAllSelected.asStateFlow()

    private val _selectedCount = MutableStateFlow(0)
    val selectedCount = _selectedCount.asStateFlow()

    private val _showModalProcessMethod = MutableStateFlow(false)
    val showModalProcessMethod = _showModalProcessMethod.asStateFlow()

    private val _locationMaster = MutableStateFlow<List<LocationMasterModel>>(emptyList())
    val locationMaster = _locationMaster.asStateFlow()

    var csvDialogMessage = mutableStateOf<String?>(null)
        private set

    var csvDialogIsError = mutableStateOf(false)
        private set

    val readerInfo = readerController.readerInfo.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ReaderInfoModel()
    )

    val connectionState = readerController.connectionState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ConnectionState.DISCONNECTED
    )

    val isNetworkConnected = connectionObserver.isConnected.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false // Compute initial value synchronously
    )

    val isPerformingInventory = readerController.isPerformingInventory
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )


    init {
        viewModelScope.launch(Dispatchers.IO) {
            readerController.tryAutoConnect()
        }

        viewModelScope.launch {
            locationRepository.get().collect { locations ->
                _locationMaster.value = locations
            }
        }

        viewModelScope.launch {
            readerController.connectionEvents.collect { evt ->
                when (evt) {
                    ConnectionState.DISCONNECTED -> {
                        showConnectingDialog.value = false
                        _toastFlow.emit("リーダーが切断されました" to ToastType.ERROR)
                    }

                    ConnectionState.CONNECTING -> showConnectingDialog.value = true
                    ConnectionState.CONNECTED -> {
                        showConnectingDialog.value = false
                        _toastFlow.emit("リーダー接続に成功しました" to ToastType.SUCCESS)
                    }
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            itemUnitRepository.ensurePresetInserted()
            processTypeRepository.ensurePresetInserted()
            csvTaskTypeRepository.ensurePresetInserted()
            inventoryResultTypeRepository.ensurePresetInserted()
        }
    }

    fun showProgressDialog() {
        showFileProgressDialog.value = true
    }

    fun hideProgressDialog() {
        showFileProgressDialog.value = false
    }


    fun onInputIntent(intent: InputIntent) {
        when (intent) {
            is InputIntent.ChangeProcessMethod -> {
                _inputState.update { it.copy(processMethod = intent.value) }
//                processMethod.value = intent.value
            }

            is InputIntent.ChangeLocation ->
                _inputState.update { it.copy(location = intent.value) }

            is InputIntent.ChangeMemo ->
                _inputState.update { it.copy(memo = intent.value) }

            is InputIntent.ChangeMissRoll ->
                _inputState.update { it.copy(materialSelectedItem = intent.value) }

            is InputIntent.ChangeGrade ->
                _inputState.update { it.copy(grade = intent.value) }

            is InputIntent.ChangeLength ->
                _inputState.update { it.copy(length = intent.value) }

            is InputIntent.ChangeRollingMachineInfo ->
                _inputState.update { it.copy(rollingMachineInfo = intent.value) }

            is InputIntent.ChangeThickness ->
                _inputState.update { it.copy(thickness = intent.value) }

            is InputIntent.ChangeWeight ->
                _inputState.update { it.copy(weight = intent.value) }

            is InputIntent.ChangeLotNo ->
                _inputState.update { it.copy(lotNo = intent.value) }

            is InputIntent.ChangePackingStyle ->
                _inputState.update { it.copy(packingStyle = intent.value) }

            is InputIntent.ChangeFileTransferMethod ->
                _inputState.update { it.copy(fileTransferMethod = intent.value) }

            InputIntent.BulkApplyProcessMethod -> {
                val chosenMethod = _inputState.value.processMethod
                val checked = _perTagChecked.value

                val updated = perTagProcessMethod.value.toMutableMap()

                checked.forEach { (tag, isChecked) ->
                    if (isChecked) {
                        updated[tag] = chosenMethod
                    }
                }
                perTagProcessMethod.value = updated
            }

            is InputIntent.ChangeOccurredAt -> {
                _inputState.update { it.copy(occurredAt = intent.value) }
            }
        }
    }


    fun onGeneralIntent(intent: ShareIntent) {
        when (intent) {
            is ShareIntent.ChangeTab ->
                _generalState.update { it.copy(tab = intent.tab) }

            is ShareIntent.ToggleDropDown ->
                _generalState.update { it.copy(showDropDown = intent.showDropDown) }

            is ShareIntent.ToggleSelectionMode ->
                _generalState.update { it.copy(isSelectionMode = intent.selectionMode) }

            is ShareIntent.ToggleTagSelection -> {
                val current = generalState.value.selectedTags.toMutableList()
                if (intent.item in current) current.remove(intent.item) else current.add(intent.item)
                _generalState.update { it.copy(selectedTags = current) }
                if (current.isEmpty()) _generalState.update { it.copy(isSelectionMode = false) }
            }

            is ShareIntent.ToggleFoundTag -> {
                val current = generalState.value.foundTags.toMutableList()
                if (intent.tag in current) current.remove(intent.tag) else current.add(intent.tag)
                _generalState.update { it.copy(foundTags = current) }
            }

            ShareIntent.ClearTagSelectionList -> {
                _generalState.update {
                    it.copy(
                        selectedTags = emptyList(),
                        isAllSelected = false
                    )
                }
            }

            ShareIntent.ClearFoundTag -> _generalState.update { it.copy(foundTags = emptyList()) }

            ShareIntent.Next -> {
                val current = _generalState.value.currentIndex
                val last = _generalState.value.selectedTags.lastIndex
                _generalState.update { it.copy(currentIndex = minOf(current + 1, last)) }
            }

            ShareIntent.Prev -> {
                val current = _generalState.value.currentIndex
                _generalState.update { it.copy(currentIndex = maxOf(current - 1, 0)) }
            }

            is ShareIntent.ToggleSelectionAll -> {
                val allTags = intent.tagList
                if (allTags.isEmpty()) return

                val currentlyAll = allTags.all { tag -> _perTagChecked.value[tag] == true }

                if (currentlyAll) {
                    _perTagChecked.value = allTags.associateWith { false }
                    _selectedCount.value = 0
                    _isAllSelected.value = false
                } else {
                    _perTagChecked.value = allTags.associateWith { true }
                    _selectedCount.value = allTags.size
                    _isAllSelected.value = true
                }
            }

            is ShareIntent.ToggleTagSelection1 -> {
                val updated = _perTagChecked.value.toMutableMap()
                updated[intent.tag] = !(updated[intent.tag] ?: false)
                _perTagChecked.value = updated

                val selectedCount = updated.values.count { it }
                val allSelected = selectedCount == intent.totalTag

                _isAllSelected.value = allSelected
                _selectedCount.value = selectedCount

            }

            is ShareIntent.ToggleNetworkDialog ->
                _generalState.update { it.copy(showNetworkDialog = intent.doesOpenDialog) }

            is ShareIntent.ChangePerTagProcessMethod -> {
                perTagProcessMethod.value = perTagProcessMethod.value.toMutableMap()
                    .apply { put(intent.tag, intent.method) }
            }

            is ShareIntent.ShowModalProcessMethod -> {
                _showModalProcessMethod.value = intent.showBottomSheet
            }

            ShareIntent.ResetState -> {
                _inputState.value = InputState()
                _expandState.value = ExpandState()
                _generalState.value = GeneralState()
                perTagExpanded.value = emptyMap()
                perTagProcessMethod.value = emptyMap()
                _perTagChecked.value = emptyMap()
                _selectedCount.value = 0
                _showModalProcessMethod.value = false
                _isAllSelected.value = false
            }

            is ShareIntent.ChangeTabInReceivingScreen ->
                _inputState.update { it.copy(materialSelectedItem = intent.tab) }

            is ShareIntent.SaveScanResult<*> -> {
                viewModelScope.launch(Dispatchers.IO) {

                    val rows = intent.data      // ALWAYS List<ICsvExport>
                    if (rows.isEmpty()) {
                        _isFileWorking.value = false
                        csvDialogIsError.value = true
                        csvDialogMessage.value = MessageMapper.toMessage(StatusCode.EMPTY_DATA)
                        showAppDialog.value = true
                        return@launch
                    }
                    val first =
                        rows.first()   // fetch 1st row to present other row for csvType and file name

                    _isFileWorking.value = true
                    _progress.value = 0f

                    val saveCsvResult = csvHelper.saveCsv(
                        context = context,
                        csvType = first.toCsvType(),
                        fileName = first.toCsvName(),
                        rows = rows,
                        onProgress = { p -> _progress.value = p },
                    )

                    val csvTaskTypeId = csvTaskTypeRepository.getIdByTaskCode(intent.taskCode.name)
                    val csvHistorySaveResult = csvTaskTypeId.let {
                        csvHistoryRepository.insert(
                            CsvHistoryModel(
                                csvTaskTypeId = csvTaskTypeId,
                                fileName = first.toCsvName(),
                                direction = intent.direction,
                                result = when (saveCsvResult) {
                                    is ProcessResult.Success -> CsvHistoryResult.SUCCESS
                                    is ProcessResult.Failure -> CsvHistoryResult.FAILURE
                                },
                                recordNum = rows.size,
                                errorMessage = when (saveCsvResult) {
                                    is ProcessResult.Failure -> MessageMapper.toMessage(
                                        saveCsvResult.statusCode
                                    )

                                    is ProcessResult.Success -> ""
                                },
                                executedAt = generateIso8601JstTimestamp()
                            )
                        )
                    }

                    if (saveCsvResult is ProcessResult.Success && csvHistorySaveResult > 0) {
                        _isFileWorking.value = false
                        csvDialogIsError.value = false
                        csvDialogMessage.value = MessageMapper.toMessage(saveCsvResult.statusCode)
                        showAppDialog.value = true

                    } else if (saveCsvResult is ProcessResult.Failure) {
                        _isFileWorking.value = false
                        csvDialogIsError.value = true
                        csvDialogMessage.value = MessageMapper.toMessage(saveCsvResult.statusCode)
                        showAppDialog.value = true
                    }
                }
            }

            ShareIntent.ToggleDialog -> showAppDialog.value = !showAppDialog.value
            ShareIntent.ToggleClearTagConfirmDialog -> showClearTagConfirmDialog.value =
                !showClearTagConfirmDialog.value

            ShareIntent.ToggleRadioPowerChangeDialog -> showRadioPowerChangeDialog.value =
                !showRadioPowerChangeDialog.value

            is ShareIntent.UpdateSelectionStatus -> {
                _selectedCount.value = intent.selectedCount
                _isAllSelected.value = intent.allSelected
            }

            is ShareIntent.ToggleTimePicker -> _generalState.update { it.copy(showTimePicker = intent.showTimePicker) }
        }
    }

    fun onExpandIntent(intent: ExpandIntent) {
        when (intent) {

            ExpandIntent.ToggleMissRollExpanded ->

                _expandState.update { it.copy(materialSelection = !_expandState.value.materialSelection) }

            ExpandIntent.ToggleLocationExpanded ->
                _expandState.update { it.copy(locationExpanded = !_expandState.value.locationExpanded) }

            ExpandIntent.TogglePackingStyleExpanded ->
                _expandState.update { it.copy(packingStyleExpanded = !_expandState.value.packingStyleExpanded) }

            ExpandIntent.ToggleHandlingMethodExpanded ->
                _expandState.update { it.copy(handlingMethodExpanded = !_expandState.value.handlingMethodExpanded) }

            ExpandIntent.ToggleFileTransferMethodExpanded ->
                _expandState.update { it.copy(fileTransferMethodExpanded = !_expandState.value.fileTransferMethodExpanded) }

            is ExpandIntent.TogglePerTagProcessExpanded -> {
                val current = perTagExpanded.value[intent.tag] ?: false
                perTagExpanded.value =
                    perTagExpanded.value.toMutableMap().apply { put(intent.tag, !current) }
            }

            is ExpandIntent.CloseProcessExpanded -> {
                perTagExpanded.value =
                    perTagExpanded.value.toMutableMap().apply { put(intent.tag, false) }
            }

            ExpandIntent.ToggleCsvTypeExpanded -> _expandState.update { it.copy(csvTypeExpanded = !_expandState.value.csvTypeExpanded) }
        }
    }
}