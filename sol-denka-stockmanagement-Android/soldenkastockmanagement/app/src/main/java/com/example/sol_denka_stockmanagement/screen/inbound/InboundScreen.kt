package com.example.sol_denka_stockmanagement.screen.inbound

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.CsvTaskType
import com.example.sol_denka_stockmanagement.constant.DialogType
import com.example.sol_denka_stockmanagement.constant.InboundInputField
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.constant.StatusCode
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.helper.message_mapper.MessageMapper
import com.example.sol_denka_stockmanagement.helper.validate.InputValidate
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.intent.InputIntent.ChangeCategory
import com.example.sol_denka_stockmanagement.intent.InputIntent.ChangeItemInCategory
import com.example.sol_denka_stockmanagement.intent.InputIntent.ChangeOccurredAtDate
import com.example.sol_denka_stockmanagement.intent.InputIntent.ChangeOccurredAtTime
import com.example.sol_denka_stockmanagement.intent.InputIntent.SearchKeyWord
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.inbound.components.InboundInputFormItem
import com.example.sol_denka_stockmanagement.screen.inbound.components.ItemSearchBar
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.share.dialog.DateDialog
import com.example.sol_denka_stockmanagement.share.dialog.TimeDialog
import com.example.sol_denka_stockmanagement.ui.theme.paleSkyBlue
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun InboundScreen(
    appViewModel: AppViewModel,
    scanViewModel: ScanViewModel,
    inboundViewModel: InboundViewModel,
    onNavigate: (Screen) -> Unit,
    onGoBack: () -> Unit,
) {

    val inputState by appViewModel.inputState.collectAsStateWithLifecycle()
    val generalState by appViewModel.generalState.collectAsStateWithLifecycle()
    val expandState by appViewModel.expandState.collectAsStateWithLifecycle()
    val searchResults by appViewModel.searchResults.collectAsStateWithLifecycle()
    val inboundInputFormResults by appViewModel.inboundInputFormResults.collectAsStateWithLifecycle()
    val lastInboundEpc by scanViewModel.lastInboundEpc.collectAsStateWithLifecycle()
    val rfidTagList by scanViewModel.rfidTagList.collectAsStateWithLifecycle()
    val locationMaster by appViewModel.locationMaster.collectAsStateWithLifecycle()
    val winderMaster by appViewModel.winderMaster.collectAsStateWithLifecycle()
    val itemCategoryMaster by appViewModel.itemCategoryMaster.collectAsStateWithLifecycle()
    val isNetworkConnected by appViewModel.isNetworkConnected.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scanViewModel.setEnableScan(false)
    }

    DisposableEffect(Unit) {
        onDispose {
            appViewModel.onGeneralIntent(ShareIntent.ResetState)
        }
    }

    TimeDialog(
        showTimeDialog = generalState.showTimePicker,
        title = stringResource(R.string.choose_time),
        confirmText = stringResource(R.string.ok),
        cancelText = stringResource(R.string.cancel),
        onConfirm = { time ->
            when (generalState.inboundInputFieldDateTime) {
                InboundInputField.OCCURRED_AT.displayName -> appViewModel.onInputIntent(
                    ChangeOccurredAtTime(time)
                )

                InboundInputField.PROCESSED_AT.displayName -> appViewModel.onInputIntent(
                    InputIntent.ChangeProcessedAtTime(time)
                )
            }
            appViewModel.onGeneralIntent(ShareIntent.ToggleTimePicker(false))
        },
        onDismissRequest = {
            appViewModel.onGeneralIntent(ShareIntent.ToggleTimePicker(false))
        }
    )

    DateDialog(
        showDateDialog = generalState.showDatePicker,
        confirmText = stringResource(R.string.ok),
        cancelText = stringResource(R.string.cancel),
        onConfirm = { date ->
            when (generalState.inboundInputFieldDateTime) {
                InboundInputField.OCCURRED_AT.displayName -> appViewModel.onInputIntent(
                    ChangeOccurredAtDate(date)
                )

                InboundInputField.PROCESSED_AT.displayName -> appViewModel.onInputIntent(
                    InputIntent.ChangeProcessedAtDate(date)
                )
            }
        },
        onDismissRequest = { appViewModel.onGeneralIntent(ShareIntent.ToggleDatePicker(false)) }
    )


    Layout(
        topBarText = stringResource(R.string.receiving),
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        appViewModel = appViewModel,
        onNavigate = onNavigate,
        currentScreenNameId = Screen.Inbound.routeId,
        prevScreenNameId = Screen.Inbound.routeId, // for scan screen to navigate back,
        hasBottomBar = true,
        bottomButton = {
            ButtonContainer(
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.register),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier.shadow(
                    elevation = 13.dp, clip = true, ambientColor = Color.Gray.copy(alpha = 0.5f),
                    spotColor = Color.DarkGray.copy(alpha = 0.7f)
                ),
                canClick = inputState.category.isNotBlank() && inputState.itemInCategory.isNotBlank(),
                onClick = {
                    val errors = InputValidate.validateRequiredFields(
                        formItems = inboundInputFormResults.filter { it.isVisible },
                        inputState = inputState
                    )
                    if (errors.isNotEmpty()){
                        appViewModel.onInputIntent(InputIntent.UpdateFieldErrors(errors))
                        return@ButtonContainer
                    }
                    scope.launch {
                        val result = inboundViewModel.saveInboundToDb(
                            rfidTag = rfidTagList.find { it.epc == lastInboundEpc },
                            weight = inputState.weight,
                            width = inputState.width,
                            length = inputState.length,
                            thickness = inputState.thickness,
                            lotNo = inputState.lotNo,
                            occurrenceReason = inputState.occurrenceReason,
                            quantity = inputState.quantity,
                            memo = inputState.memo,
                            occurredAt = "${inputState.occurredAtDate}_${inputState.occurredAtTime}",
                            processedAt = "${inputState.processedAtDate}_${inputState.processedAtTime}",
                            registeredAt = generateIso8601JstTimestamp()
                        )
                        result.exceptionOrNull()?.let { e ->
                            appViewModel.onGeneralIntent(
                                ShareIntent.ShowDialog(
                                    type = DialogType.ERROR,
                                    message = MessageMapper.toMessage(StatusCode.FAILED)
                                )
                            )
                            return@launch
                        }
                        val csvModels = inboundViewModel.generateCsvData(
                            weight = inputState.weight,
                            width = inputState.width,
                            length = inputState.length,
                            thickness = inputState.thickness,
                            lotNo = inputState.lotNo,
                            occurrenceReason = inputState.occurrenceReason,
                            quantity = inputState.quantity,
                            memo = inputState.memo,
                            occurredAt = "${inputState.occurredAtDate}_${inputState.occurredAtTime}",
                            processedAt = "${inputState.processedAtDate}_${inputState.processedAtTime}",
                            rfidTag = rfidTagList.find { it.epc == lastInboundEpc },
                        )
                        val saveResult = appViewModel.saveScanResultToCsv(
                            data = csvModels,
                            direction = CsvHistoryDirection.EXPORT,
                            taskCode = CsvTaskType.IN,
                        )
                        saveResult
                            .onSuccess {
                                // SAVE CSV success
                                // Now check network and send SFTP
                                if (isNetworkConnected) {
                                    //sftp send
                                } else {
                                    appViewModel.onGeneralIntent(
                                        ShareIntent.ShowDialog(
                                            type = DialogType.SAVE_CSV_SUCCESS_FAILED_SFTP,
                                            message = MessageMapper.toMessage(StatusCode.EXPORT_OK)
                                        )
                                    )
                                }
                            }
                            .onFailure { throwable ->
                                val code = StatusCode.valueOf(throwable.message!!)
                                appViewModel.onGeneralIntent(
                                    ShareIntent.ShowDialog(
                                        type = DialogType.ERROR,
                                        message = MessageMapper.toMessage(code)
                                    )
                                )
                            }
                    }
                },
                buttonText = stringResource(R.string.register),
            )
        },
        onBackArrowClick = {
            onGoBack()
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .imePadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 3.dp,
                        shape = RoundedCornerShape(12.dp),
                        clip = false, // 👈 allow the shadow to bleed outside the box
                    )
                    .border(1.dp, color = paleSkyBlue, shape = RoundedCornerShape(12.dp))
                    .background(Color.White, RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                ) {
                    Text(
                        text = stringResource(
                            R.string.item_code, lastInboundEpc ?: ""
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ExposedDropdownMenuBox(
                        expanded = expandState.categoryExpanded,
                        onExpandedChange = { appViewModel.onExpandIntent(ExpandIntent.ToggleCategoryExpanded) }) {
                        InputFieldContainer(
                            modifier = Modifier
                                .menuAnchor(
                                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                    enabled = true
                                )
                                .fillMaxWidth(),
                            value = if (inputState.category == SelectTitle.SelectCategory.displayName) "" else inputState.category,
                            hintText = SelectTitle.SelectCategory.displayName,
                            isNumeric = false,
                            onChange = { newValue ->
                                appViewModel.onInputIntent(
                                    ChangeCategory(
                                        categoryId = 0,
                                        value = newValue
                                    )
                                )
                            },
                            readOnly = true,
                            isDropDown = true,
                            enable = true,
                            onEnterPressed = {}
                        )
                        ExposedDropdownMenu(
                            expanded = expandState.categoryExpanded,
                            onDismissRequest = { appViewModel.onExpandIntent(ExpandIntent.ToggleCategoryExpanded) }
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = SelectTitle.SelectCategory.displayName) },
                                onClick = {
                                    appViewModel.apply {
                                        onInputIntent(
                                            ChangeCategory(
                                                categoryId = 0,
                                                value = ""
                                            )
                                        )
                                        onExpandIntent(ExpandIntent.ToggleCategoryExpanded)
                                    }
                                }
                            )
                            itemCategoryMaster.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(text = category.itemCategoryName) },
                                    onClick = {
                                        appViewModel.apply {
                                            onInputIntent(
                                                ChangeCategory(
                                                    categoryId = category.itemCategoryId,
                                                    value = category.itemCategoryName
                                                )
                                            )
                                            onExpandIntent(ExpandIntent.ToggleCategoryExpanded)
                                        }
                                    }
                                )
                            }
                        }
                    }
                    if (inputState.category.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))

                        ItemSearchBar(
                            keyword = inputState.itemInCategory,
                            results = searchResults,
                            onKeywordChange = { itemName ->
                                appViewModel.onInputIntent(
                                    SearchKeyWord(
                                        itemName = itemName,
                                    )
                                )
                                appViewModel.onGeneralIntent(
                                    ShareIntent.FindItemNameByKeyWord(
                                        categoryName = inputState.category,
                                        keyword = itemName
                                    )
                                )
                            },
                            onSelectItem = { itemName, itemId ->
                                appViewModel.onInputIntent(
                                    ChangeItemInCategory(
                                        itemName = itemName,
                                        itemId = itemId
                                    )
                                )
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(
                    items = inboundInputFormResults.filter { it.isVisible }.sortedWith(
                        compareBy {
                            if (it.fieldName == InboundInputField.MEMO.displayName) 1 else 0
                        }
                    ),
                    key = { it.fieldName }
                ) { result ->
                    InboundInputFormItem(
                        result = result,
                        inputState = inputState,
                        appViewModel = appViewModel,
                        expandState = expandState,
                        locationMaster = locationMaster,
                        winderMaster = winderMaster,
                    )
                }
            }
        }
    }
}
