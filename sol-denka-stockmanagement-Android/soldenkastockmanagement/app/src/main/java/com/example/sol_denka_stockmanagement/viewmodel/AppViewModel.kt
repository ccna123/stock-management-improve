package com.example.sol_denka_stockmanagement.viewmodel

import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sol_denka_stockmanagement.constant.ConnectionState
import com.example.sol_denka_stockmanagement.constant.HandlingMethod
import com.example.sol_denka_stockmanagement.database.repository.item.ItemUnitRepository
import com.example.sol_denka_stockmanagement.database.repository.location.LocationRepository
import com.example.sol_denka_stockmanagement.helper.NetworkConnectionObserver
import com.example.sol_denka_stockmanagement.helper.ReaderController
import com.example.sol_denka_stockmanagement.helper.ToastType
import com.example.sol_denka_stockmanagement.helper.csv.CsvHelper
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.model.location.LocationMasterModel
import com.example.sol_denka_stockmanagement.model.reader.ReaderInfoModel
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting.AppSettingState
import com.example.sol_denka_stockmanagement.state.ErrorState
import com.example.sol_denka_stockmanagement.state.ExpandState
import com.example.sol_denka_stockmanagement.state.GeneralState
import com.example.sol_denka_stockmanagement.state.InputState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
    private val readerController: ReaderController,
    private val connectionObserver: NetworkConnectionObserver,
    private val locationRepository: LocationRepository,
    private val itemUnitRepository: ItemUnitRepository,
    private val csvHelper: CsvHelper,
) : ViewModel() {


    // region State Definitions
    private val _generalState = MutableStateFlow(GeneralState())
    val generalState: StateFlow<GeneralState> = _generalState.asStateFlow()

    private val _expandState = MutableStateFlow(ExpandState())
    val expandState: StateFlow<ExpandState> = _expandState.asStateFlow()

    private val _inputState = MutableStateFlow(InputState())
    val inputState: StateFlow<InputState> = _inputState.asStateFlow()

    private val _errorState = mutableStateOf(ErrorState())
    val errorState: State<ErrorState> = _errorState

    private val _appSettingState = mutableStateOf(AppSettingState())
    val appSettingState: State<AppSettingState> = _appSettingState

    var showFileProgressDialog = mutableStateOf(false)

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress
    private val _isFileWorking = MutableStateFlow(false)
    val isFileWorking = _isFileWorking.asStateFlow()

    private val _toastFlow = MutableSharedFlow<Pair<String, ToastType>>()
    val toastFlow = _toastFlow

    val perTagHandlingMethod = MutableStateFlow<Map<String, String>>(emptyMap())
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

    private val _showModalHandlingMethod = MutableStateFlow(false)
    val showModalHandlingMethod = _showModalHandlingMethod.asStateFlow()

    var bottomSheetChosenMethod = mutableStateOf(HandlingMethod.USE.displayName)
        private set

    private val _locationMaster = MutableStateFlow<List<LocationMasterModel>>(emptyList())
    val locationMaster = _locationMaster.asStateFlow()

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
        }
    }

    fun simulateFileLoading(context: Context, isUpload: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isFileWorking.value = true
                _progress.value = 0f

                // 1️⃣ Get all CSV files in both InventoryResultLocalRepository & StockEvent folders
                val csvFiles = mutableListOf<Pair<String, Long>>()
                val resolver = context.contentResolver
                val externalUri = MediaStore.Files.getContentUri("external")

                val projection = arrayOf(
                    MediaStore.Files.FileColumns.DISPLAY_NAME,
                    MediaStore.Files.FileColumns.SIZE,
                    MediaStore.Files.FileColumns.RELATIVE_PATH
                )

                val selection =
                    "${MediaStore.Files.FileColumns.RELATIVE_PATH} LIKE ? OR " +
                            "${MediaStore.Files.FileColumns.RELATIVE_PATH} LIKE ?"
                val selectionArgs = arrayOf(
                    "%Documents/StockManagementApp/InventoryResultLocalRepository%",
                    "%Documents/StockManagementApp/StockEvent%"
                )

                resolver.query(
                    externalUri,
                    projection,
                    selection,
                    selectionArgs,
                    null
                )?.use { cursor ->
                    val nameCol =
                        cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                    val sizeCol =
                        cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
                    while (cursor.moveToNext()) {
                        val name = cursor.getString(nameCol)
                        val size = cursor.getLong(sizeCol)
                        csvFiles.add(name to size)
                    }
                }

                val totalBytes = csvFiles.sumOf { it.second }.takeIf { it > 0 } ?: 1L
                Log.i("TSS", "📦 Found ${csvFiles.size} CSV files. Total size: $totalBytes bytes")

                // 2️⃣ Simulate transfer progress based on file sizes
                var processedBytes = 0L
                for ((name, size) in csvFiles) {
                    Log.d("TSS", "Processing $name ($size bytes)")
                    var chunkProcessed = 0L
                    while (chunkProcessed < size) {
                        delay(30) // simulate work
                        val chunk = (size / 50).coerceAtLeast(1024) // read ~2% each loop
                        chunkProcessed += chunk
                        processedBytes += chunk
                        _progress.value = (processedBytes.toFloat() / totalBytes).coerceIn(0f, 1f)
                    }
                }

                // 3️⃣ Complete
                _progress.value = 1f
                delay(500)
                _isFileWorking.value = false
                Log.i("TSS", "✅ Simulated ${if (isUpload) "upload" else "download"} complete")

            } catch (e: Exception) {
                Log.e("TSS", "Error simulating file loading: ${e.message}", e)
                _isFileWorking.value = false
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
            is InputIntent.ChangeHandlingMethod -> {
                _inputState.update { it.copy(handlingMethod = intent.value) }
                bottomSheetChosenMethod.value = intent.value
            }

            is InputIntent.ChangeLocation ->
                _inputState.update { it.copy(stockArea = intent.value) }

            is InputIntent.ChangeRemark ->
                _inputState.update { it.copy(remark = intent.value) }

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

            InputIntent.BulkApplyHandlingMethod -> {
                val chosenMethod = bottomSheetChosenMethod.value
                val checked = _perTagChecked.value

                val updated = perTagHandlingMethod.value.toMutableMap()

                checked.forEach { (tag, isChecked) ->
                    if (isChecked) {
                        updated[tag] = chosenMethod
                    }
                }
                perTagHandlingMethod.value = updated
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

            is ShareIntent.ChangePerTagHandlingMethod -> {
                perTagHandlingMethod.value = perTagHandlingMethod.value.toMutableMap()
                    .apply { put(intent.tag, intent.method) }
            }

            is ShareIntent.ShowModalHandlingMethod -> {
                _showModalHandlingMethod.value = intent.showBottomSheet
            }

            ShareIntent.ResetState -> {
                _inputState.value = InputState()
                _expandState.value = ExpandState()
                _errorState.value = ErrorState()
                _generalState.value = GeneralState()
                perTagExpanded.value = emptyMap()
                perTagHandlingMethod.value = emptyMap()
                _perTagChecked.value = emptyMap()
                _selectedCount.value = 0
                _showModalHandlingMethod.value = false
                _isAllSelected.value = false
                bottomSheetChosenMethod.value = HandlingMethod.USE.displayName
            }

            is ShareIntent.ChangeTabInReceivingScreen ->
                _inputState.update { it.copy(materialSelectedItem = intent.tab) }

            is ShareIntent.SaveScanResult<*> -> {
                viewModelScope.launch(Dispatchers.IO) {

//                    val ctx = intent.context
//                    val rows = intent.data      // ALWAYS List<ICsvExport>
//
//                    if (rows.isEmpty()) {
//                        _toastFlow.emit("保存するデータがありません" to ToastType.ERROR)
//                        return@launch
//                    }
//
//                    val first = rows.first()
//
//                    _isFileWorking.value = true
//                    _progress.value = 0f
//
//                    val result = csvHelper.saveCsv(
//                        context = ctx,
//                        csvType = first.toCsvType(),         // model tự biết nó thuộc loại CSV nào
//                        fileName = first.toCsvName(),       // model tự generate filename
//                        rows = rows,                        // list → nhiều row
//                        onProgress = { p -> _progress.value = p }
//                    )

                    _isFileWorking.value = false
                    showAppDialog.value = true
//                    if (result is ProcessResult.Success) {
//                        _toastFlow.emit("CSV 保存成功" to ToastType.SUCCESS)
//                    } else {
//                        _toastFlow.emit("CSV 保存失敗" to ToastType.ERROR)
//                    }
                }
            }

            ShareIntent.ToggleDialog -> showAppDialog.value = !showAppDialog.value
            ShareIntent.ToggleClearTagConfirmDialog -> showClearTagConfirmDialog.value = !showClearTagConfirmDialog.value
            ShareIntent.ToggleRadioPowerChangeDialog -> showRadioPowerChangeDialog.value = !showRadioPowerChangeDialog.value
        }
    }

    fun onExpandIntent(intent: ExpandIntent) {
        when (intent) {

            ExpandIntent.ToggleMissRollExpanded ->

                _expandState.update { it.copy(materialSelection = !_expandState.value.materialSelection) }

            ExpandIntent.ToggleStockAreaExpanded ->
                _expandState.update { it.copy(stockAreaExpanded = !_expandState.value.stockAreaExpanded) }

            ExpandIntent.TogglePackingStyleExpanded ->
                _expandState.update { it.copy(packingStyleExpanded = !_expandState.value.packingStyleExpanded) }

            ExpandIntent.ToggleHandlingMethodExpanded ->
                _expandState.update { it.copy(handlingMethodExpanded = !_expandState.value.handlingMethodExpanded) }

            ExpandIntent.ToggleFileTransferMethodExpanded ->
                _expandState.update { it.copy(fileTransferMethodExpanded = !_expandState.value.fileTransferMethodExpanded) }

            is ExpandIntent.TogglePerTagHandlingExpanded -> {
                val current = perTagExpanded.value[intent.tag] ?: false
                perTagExpanded.value =
                    perTagExpanded.value.toMutableMap().apply { put(intent.tag, !current) }
            }

            is ExpandIntent.CloseHandlingExpanded -> {
                perTagExpanded.value =
                    perTagExpanded.value.toMutableMap().apply { put(intent.tag, false) }
            }
        }
    }
}