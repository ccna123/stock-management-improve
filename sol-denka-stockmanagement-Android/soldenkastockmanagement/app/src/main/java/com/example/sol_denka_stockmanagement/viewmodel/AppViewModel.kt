package com.example.sol_denka_stockmanagement.viewmodel

import android.app.Application
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
import com.example.sol_denka_stockmanagement.helper.ReaderController
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.model.ReaderInfoModel
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting.AppSettingState
import com.example.sol_denka_stockmanagement.state.ErrorState
import com.example.sol_denka_stockmanagement.state.ExpandState
import com.example.sol_denka_stockmanagement.state.GeneralState
import com.example.sol_denka_stockmanagement.state.InputState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class AppViewModel @Inject constructor(
    private val application: Application,
    private var readerController: ReaderController
) : ViewModel() {


    // region State Definitions
    private val _generalState = mutableStateOf(GeneralState())
    val generalState: State<GeneralState> = _generalState

    private val _expandState = mutableStateOf(ExpandState())
    val expandState: State<ExpandState> = _expandState

    private val _inputState = mutableStateOf(InputState())
    val inputState: State<InputState> = _inputState

    private val _errorState = mutableStateOf(ErrorState())
    val errorState: State<ErrorState> = _errorState

    private val _appSettingState = mutableStateOf(AppSettingState())
    val appSettingState: State<AppSettingState> = _appSettingState

    var showFileProgressDialog = mutableStateOf(false)

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress
    private val _isFileWorking = MutableStateFlow(false)
    val isFileWorking = _isFileWorking.asStateFlow()


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


    init {
        viewModelScope.launch(Dispatchers.IO) {
            readerController.tryAutoConnect()
        }
    }

    fun simulateFileLoading(context: Context, isUpload: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isFileWorking.value = true
                _progress.value = 0f

                // 1️⃣ Get all CSV files in both InventoryResultDao & StockEvent folders
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
                    "%Documents/StockManagementApp/InventoryResultDao%",
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

            is InputIntent.ChangeHandlingMethod ->
                _inputState.value = _inputState.value.copy(handlingMethod = intent.value)

            is InputIntent.ChangeStockArea ->
                _inputState.value = _inputState.value.copy(stockArea = intent.value)

            is InputIntent.ChangeRemark ->
                _inputState.value = _inputState.value.copy(remark = intent.value)

            is InputIntent.ChangeMissRoll ->
                _inputState.value = _inputState.value.copy(materialSelectedItem = intent.value)

            is InputIntent.ChangeGrade ->
                _inputState.value = _inputState.value.copy(grade = intent.value)

            is InputIntent.ChangeLength ->
                _inputState.value = _inputState.value.copy(length = intent.value)

            is InputIntent.ChangeRollingMachineInfo ->
                _inputState.value = _inputState.value.copy(rollingMachineInfo = intent.value)

            is InputIntent.ChangeThickness ->
                _inputState.value = _inputState.value.copy(thickness = intent.value)

            is InputIntent.ChangeWeight ->
                _inputState.value = _inputState.value.copy(weight = intent.value)

            is InputIntent.ChangeLotNo ->
                _inputState.value = _inputState.value.copy(lotNo = intent.value)

            is InputIntent.ChangePackingStyle ->
                _inputState.value = _inputState.value.copy(packingStyle = intent.value)

            is InputIntent.ChangeFileTransferMethod ->
                _inputState.value = _inputState.value.copy(fileTransferMethod = intent.value)
        }
    }


    fun onGeneralIntent(intent: ShareIntent) {
        when (intent) {
            is ShareIntent.ChangeTab ->
                _generalState.value = _generalState.value.copy(tab = intent.tab)

            is ShareIntent.ToggleDialog ->
                _generalState.value = _generalState.value.copy(showAppDialog = !_generalState.value.showAppDialog)

            is ShareIntent.ToggleDropDown ->
                _generalState.value = _generalState.value.copy(showDropDown = intent.showDropDown)
        }
    }


    fun onExpandIntent(intent: ExpandIntent) {
        when (intent) {

            ExpandIntent.ToggleMissRollExpanded ->
                _expandState.value = _expandState.value.copy(
                    materialSelection = !_expandState.value.materialSelection
                )

            ExpandIntent.ToggleStockAreaExpanded ->
                _expandState.value = _expandState.value.copy(
                    stockAreaExpanded = !_expandState.value.stockAreaExpanded
                )

            ExpandIntent.TogglePackingStyleExpanded ->
                _expandState.value = _expandState.value.copy(
                    packingStyleExpanded = !_expandState.value.packingStyleExpanded
                )

            ExpandIntent.ToggleHandlingMethodExpanded ->
                _expandState.value = _expandState.value.copy(
                    handlingMethodExpanded = !_expandState.value.handlingMethodExpanded
                )

            ExpandIntent.ToggleFileTransferMethodExpanded ->
                _expandState.value = _expandState.value.copy(
                    fileTransferMethodExpanded = !_expandState.value.fileTransferMethodExpanded
                )
        }
    }


    var isSingleTagMode = MutableStateFlow(false)
        private set

    // endregion

    fun updateTagStatus(rfidNo: String) {
        viewModelScope.launch(Dispatchers.IO) {
        }
    }

    fun resetState() {
        _inputState.value = InputState()
        _expandState.value = ExpandState()
        _errorState.value = ErrorState()
        isSingleTagMode.value = false
        _generalState.value = GeneralState()
    }

}
