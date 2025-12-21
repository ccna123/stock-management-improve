package com.example.sol_denka_stockmanagement.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.app_interface.ICsvExport
import com.example.sol_denka_stockmanagement.app_interface.IPresetRepo
import com.example.sol_denka_stockmanagement.constant.ConnectionState
import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.CsvHistoryResult
import com.example.sol_denka_stockmanagement.constant.CsvTaskType
import com.example.sol_denka_stockmanagement.constant.DialogType
import com.example.sol_denka_stockmanagement.constant.InboundInputField
import com.example.sol_denka_stockmanagement.constant.StatusCode
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.repository.csv.CsvHistoryRepository
import com.example.sol_denka_stockmanagement.database.repository.csv.CsvTaskTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.field.ItemTypeFieldSettingMasterRepository
import com.example.sol_denka_stockmanagement.database.repository.item.ItemCategoryRepository
import com.example.sol_denka_stockmanagement.database.repository.item.ItemTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.location.LocationMasterRepository
import com.example.sol_denka_stockmanagement.database.repository.winder.WinderInfoRepository
import com.example.sol_denka_stockmanagement.helper.NetworkConnectionObserver
import com.example.sol_denka_stockmanagement.constant.ProcessResult
import com.example.sol_denka_stockmanagement.database.repository.process.ProcessTypeRepository
import com.example.sol_denka_stockmanagement.helper.controller.ReaderController
import com.example.sol_denka_stockmanagement.helper.csv.CsvHelper
import com.example.sol_denka_stockmanagement.helper.message_mapper.MessageMapper
import com.example.sol_denka_stockmanagement.helper.toast.ToastType
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.model.csv.CsvHistoryModel
import com.example.sol_denka_stockmanagement.model.inbound.InboundInputFormModel
import com.example.sol_denka_stockmanagement.model.item.ItemCategoryModel
import com.example.sol_denka_stockmanagement.model.item.ItemTypeMasterModel
import com.example.sol_denka_stockmanagement.model.location.LocationMasterModel
import com.example.sol_denka_stockmanagement.model.process.ProcessTypeModel
import com.example.sol_denka_stockmanagement.model.reader.ReaderInfoModel
import com.example.sol_denka_stockmanagement.model.winder.WinderInfoModel
import com.example.sol_denka_stockmanagement.state.DialogState
import com.example.sol_denka_stockmanagement.state.DialogState.Error
import com.example.sol_denka_stockmanagement.state.DialogState.Hidden
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
    private val locationMasterRepository: LocationMasterRepository,
    private val csvTaskTypeRepository: CsvTaskTypeRepository,
    private val csvHistoryRepository: CsvHistoryRepository,
    private val itemTypeRepository: ItemTypeRepository,
    private val itemTypeFieldSettingMasterRepository: ItemTypeFieldSettingMasterRepository,
    private val itemCategoryRepository: ItemCategoryRepository,
    private val winderInfoRepository: WinderInfoRepository,
    private val processTypeRepository: ProcessTypeRepository,
    private val presetRepositories: Set<@JvmSuppressWildcards IPresetRepo>,
    private val csvHelper: CsvHelper,
) : ViewModel() {


    // region State Definitions
    private val _generalState = MutableStateFlow(GeneralState())
    val generalState: StateFlow<GeneralState> = _generalState.asStateFlow()

    private val _expandState = MutableStateFlow(ExpandState())
    val expandState: StateFlow<ExpandState> = _expandState.asStateFlow()

    private val _inputState = MutableStateFlow(InputState())
    val inputState: StateFlow<InputState> = _inputState.asStateFlow()

    private val _dialogState = MutableStateFlow<DialogState>(Hidden)
    val dialogState: StateFlow<DialogState> = _dialogState.asStateFlow()

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

    private val _showModalProcessMethod = MutableStateFlow(false)
    val showModalProcessMethod = _showModalProcessMethod.asStateFlow()

    private val _locationMaster = MutableStateFlow<List<LocationMasterModel>>(emptyList())
    val locationMaster = _locationMaster.asStateFlow()

    private val _itemCategoryMaster = MutableStateFlow<List<ItemCategoryModel>>(emptyList())
    val itemCategoryMaster = _itemCategoryMaster.asStateFlow()

    private val _winderMaster = MutableStateFlow<List<WinderInfoModel>>(emptyList())
    val winderMaster = _winderMaster.asStateFlow()

    private val _processTypeMaster = MutableStateFlow<List<ProcessTypeModel>>(emptyList())
    val processTypeMaster = _processTypeMaster.asStateFlow()

    private val _outboundProcessErrorSet = MutableStateFlow<Set<String>>(emptySet())
    val outboundProcessErrorSet = _outboundProcessErrorSet.asStateFlow()

    private val _searchResults = MutableStateFlow<List<ItemTypeMasterModel>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val _inboundInputFormResults =
        MutableStateFlow<List<InboundInputFormModel>>(emptyList())
    val inboundInputFormResults = _inboundInputFormResults.asStateFlow()

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
            csvHelper.createAppFolders(context)
        }

        viewModelScope.launch {
            locationMasterRepository.get().collect { locations ->
                _locationMaster.value = locations
            }
        }

        viewModelScope.launch {
            itemCategoryRepository.get().collect { categories ->
                _itemCategoryMaster.value = categories
            }
        }

        viewModelScope.launch {
            winderInfoRepository.get().collect { winders ->
                _winderMaster.value = winders
            }
        }

        viewModelScope.launch {
            processTypeRepository.get().collect { processTypes ->
                _processTypeMaster.value = processTypes
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
            presetRepositories.forEach {
                it.ensurePresetInserted()
            }
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
            }

            is InputIntent.ChangeLocation ->
                _inputState.update { it.copy(location = intent.value) }

            is InputIntent.ChangeMemo ->
                _inputState.update { it.copy(memo = intent.value) }

            is InputIntent.ChangeMissRollReason ->
                _inputState.update { it.copy(occurrenceReason = intent.value) }

            is InputIntent.ChangeGrade ->
                _inputState.update { it.copy(grade = intent.value) }

            is InputIntent.ChangeLength ->
                _inputState.update { it.copy(length = intent.value) }

            is InputIntent.ChangeWinderInfo ->
                _inputState.update { it.copy(winder = intent.value) }

            is InputIntent.ChangeThickness ->
                _inputState.update { it.copy(thickness = intent.value) }

            is InputIntent.ChangeWeight ->
                _inputState.update { it.copy(weight = intent.value) }

            is InputIntent.ChangeLotNo ->
                _inputState.update { it.copy(lotNo = intent.value) }

            is InputIntent.ChangePackingType ->
                _inputState.update { it.copy(packingType = intent.value) }

            is InputIntent.ChangeFileTransferMethod ->
                _inputState.update { it.copy(fileTransferMethod = intent.value) }

            is InputIntent.BulkApplyProcessMethod -> {
                val chosenMethod = _inputState.value.processMethod
                val updated = perTagProcessMethod.value.toMutableMap()

                intent.checkedTags.forEach { epc ->
                    updated[epc] = chosenMethod
                }

                perTagProcessMethod.value = updated
            }


            is InputIntent.ChangeOccurredAtDate -> _inputState.update { it.copy(occurredAtDate = intent.value) }


            is InputIntent.ChangeCategory -> {
                if (intent.categoryId == 0) {
                    _searchResults.value = emptyList()
                    _inputState.update { it.copy(category = "", itemInCategory = "") }
                    _inboundInputFormResults.value = emptyList()
                    return
                }
                _inputState.update { it.copy(category = intent.value, itemInCategory = "") }
                _inboundInputFormResults.value = emptyList()
                resetInboundInputForm()
                viewModelScope.launch {
                    val result = itemTypeRepository.getItemTypeByCategoryId(intent.categoryId)
                    _searchResults.value = result
                }
            }

            is InputIntent.ChangeOccurredAtTime -> _inputState.update { it.copy(occurredAtTime = intent.value) }
            is InputIntent.ChangeItemInCategory -> {
                _inputState.update { it.copy(itemInCategory = intent.itemName) }
                resetInboundInputForm()
                viewModelScope.launch {
                    _inboundInputFormResults.value =
                    itemTypeFieldSettingMasterRepository.getFieldForItemTypeByItemTypeId(intent.itemId)
                }
            }

            is InputIntent.SearchKeyWord -> _inputState.update { it.copy(itemInCategory = intent.itemName) }
            is InputIntent.ChangeSpecificGravity -> _inputState.update { it.copy(specificGravity = intent.value) }
            is InputIntent.ChangeWidth -> _inputState.update { it.copy(width = intent.value) }
            is InputIntent.ChangeProcessedAtDate -> _inputState.update { it.copy(processedAtDate = intent.value) }
            is InputIntent.ChangeProcessedAtTime -> _inputState.update { it.copy(processedAtTime = intent.value) }
            is InputIntent.ChangeQuantity -> _inputState.update { it.copy(quantity = intent.value) }
            is InputIntent.UpdateFieldErrors -> {
                _inputState.update { it.copy(fieldErrors = intent.errors) }
                viewModelScope.launch {
                    _toastFlow.emit("必須項目を入力してください" to ToastType.ERROR)
                }
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

            is ShareIntent.ToggleFoundTag -> {
                val current = generalState.value.foundTags.toMutableList()
                if (intent.tag in current) current.remove(intent.tag) else current.add(intent.tag)
                _generalState.update { it.copy(foundTags = current) }
            }

            ShareIntent.ClearFoundTag -> _generalState.update { it.copy(foundTags = emptyList()) }

            is ShareIntent.Next -> {
                val current = _generalState.value.currentIndex
                val last = intent.lastItemIndex
                _generalState.update { it.copy(currentIndex = minOf(current + 1, last)) }
            }

            ShareIntent.Prev -> {
                val current = _generalState.value.currentIndex
                _generalState.update { it.copy(currentIndex = maxOf(current - 1, 0)) }
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
                _showModalProcessMethod.value = false
                _inboundInputFormResults.value = emptyList()
            }

            ShareIntent.ToggleDialog -> showAppDialog.value = !showAppDialog.value
            ShareIntent.ToggleClearTagConfirmDialog -> showClearTagConfirmDialog.value =
                !showClearTagConfirmDialog.value

            ShareIntent.ToggleRadioPowerChangeDialog -> showRadioPowerChangeDialog.value =
                !showRadioPowerChangeDialog.value


            ShareIntent.ResetDetailIndex -> _generalState.update { it.copy(currentIndex = 0) }
            ShareIntent.HiddenDialog -> _dialogState.update { Hidden }
            is ShareIntent.ShowDialog -> {
                when (intent.type) {
                    DialogType.CONFIRM -> {
                        _dialogState.update {
                            DialogState.Confirm(
                                message = intent.message,
                            )
                        }
                    }

                    DialogType.SAVE_CSV_SUCCESS_FAILED_SFTP -> {
                        _dialogState.update {
                            DialogState.SaveCsvSuccessFailSftp(
                                message = intent.message,
                            )
                        }
                    }

                    DialogType.SAVE_CSV_SEND_SFTP_SUCCESS -> {
                        _dialogState.update {
                            DialogState.SaveCsvSendSftpSuccess(
                                message = intent.message,
                            )
                        }
                    }

                    DialogType.ERROR -> {
                        _dialogState.update {
                            Error(
                                message = intent.message,
                            )
                        }
                    }
                }
            }

            is ShareIntent.ToggleTimePicker -> {
                _generalState.update {
                    it.copy(
                        showTimePicker = intent.showTimePicker,
                        inboundInputFieldDateTime = when (intent.field) {
                            InboundInputField.OCCURRED_AT.code -> InboundInputField.OCCURRED_AT.code
                            InboundInputField.PROCESSED_AT.code -> InboundInputField.PROCESSED_AT.code
                            else -> ""
                        }
                    )
                }
            }

            is ShareIntent.ToggleDatePicker -> {
                _generalState.update {
                    it.copy(
                        showDatePicker = intent.showDatePicker,
                        inboundInputFieldDateTime = when (intent.field) {
                            InboundInputField.OCCURRED_AT.code -> InboundInputField.OCCURRED_AT.code
                            InboundInputField.PROCESSED_AT.code -> InboundInputField.PROCESSED_AT.code
                            else -> ""
                        }
                    )
                }
            }

            is ShareIntent.MarkOutboundProcessError -> {
                _outboundProcessErrorSet.value = intent.epcs.toSet()
            }

            ShareIntent.ClearOutboundProcessError -> _outboundProcessErrorSet.value = emptySet()
            is ShareIntent.SelectChipIndex -> _generalState.update { it.copy(selectedChipIndex = intent.index) }
            is ShareIntent.FindItemNameByKeyWord -> {
                viewModelScope.launch {
                    val keyword = intent.keyword.trim()

                    // case: keyword empty ⇒ reload full list from DB
                    if (keyword.isBlank()) {
                        val categoryId = itemCategoryRepository.getIdByName(intent.categoryName)
                        val fullList =
                            itemTypeRepository.getItemTypeByCategoryId(categoryId)
                        _searchResults.value = fullList
                    }

                    // case: progressive filtering on the current list
                    _searchResults.value = _searchResults.value.filter {
                        it.itemTypeName.contains(keyword, ignoreCase = true)
                    }
                }
            }
        }
    }

    fun onExpandIntent(intent: ExpandIntent) {
        when (intent) {

            ExpandIntent.ToggleMissRollExpanded ->

                _expandState.update { it.copy(materialSelection = !_expandState.value.materialSelection) }

            ExpandIntent.ToggleLocationExpanded ->
                _expandState.update { it.copy(locationExpanded = !_expandState.value.locationExpanded) }

            ExpandIntent.TogglePackingTypeExpanded ->
                _expandState.update { it.copy(packingStyleExpanded = !_expandState.value.packingStyleExpanded) }

            ExpandIntent.ToggleProcessMethodExpanded ->
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
            ExpandIntent.ToggleCategoryExpanded -> _expandState.update { it.copy(categoryExpanded = !_expandState.value.categoryExpanded) }
            ExpandIntent.ToggleWinderExpanded -> _expandState.update { it.copy(winderExpanded = !_expandState.value.winderExpanded) }
        }
    }

    suspend fun saveScanResultToCsv(
        taskCode: CsvTaskType,
        direction: CsvHistoryDirection,
        data: List<ICsvExport>
    ): Result<StatusCode> {
        val rows = data     // ALWAYS List<ICsvExport>
        if (rows.isEmpty()) {
            _isFileWorking.value = false
            _dialogState.value = Error(
                message = MessageMapper.toMessage(StatusCode.EMPTY_DATA)
            )
            return Result.failure(Exception(StatusCode.EMPTY_DATA.name))
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

        val csvTaskTypeId = csvTaskTypeRepository.getIdByTaskCode(taskCode.name)
        val csvHistorySaveResult = csvTaskTypeId.let {
            csvHistoryRepository.insert(
                CsvHistoryModel(
                    csvTaskTypeId = csvTaskTypeId,
                    fileName = first.toCsvName(),
                    direction = direction,
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

        return when (saveCsvResult) {
            is ProcessResult.Success ->
                if (csvHistorySaveResult > 0)
                    Result.success(StatusCode.OK)
                else
                    Result.failure(Exception(StatusCode.FAILED.name))

            is ProcessResult.Failure ->
                Result.failure(Exception(saveCsvResult.statusCode.name))
        }
    }

    private fun resetInboundInputForm() {
        _inputState.update {
            it.copy(
                width = "",
                specificGravity = "",
                length = "",
                thickness = "",
                grade = "",
                winder = "",
                memo = "",
                location = "",
                processMethod = "",
                weight = "",
                lotNo = "",
                packingType = "",
                occurrenceReason = "",
                occurredAtDate = "",
                occurredAtTime = "",
                processedAtDate = "",
                processedAtTime = "",
            )
        }
    }
}