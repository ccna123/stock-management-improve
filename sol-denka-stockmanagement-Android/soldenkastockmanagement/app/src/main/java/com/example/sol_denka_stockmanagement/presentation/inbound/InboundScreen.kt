package com.example.sol_denka_stockmanagement.presentation.inbound

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.DialogType
import com.example.sol_denka_stockmanagement.constant.InboundInputField
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.constant.StatusCode
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.helper.message_mapper.MessageMapper
import com.example.sol_denka_stockmanagement.helper.validate.InputValidate
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.CardContainer
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.share.dialog.DateDialog
import com.example.sol_denka_stockmanagement.share.dialog.TimeDialog
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel
import java.util.UUID

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
    val uiState by inboundViewModel.uiState.collectAsStateWithLifecycle()
    val locationMaster by inboundViewModel.locationMaster.collectAsStateWithLifecycle()
    val winderMaster by inboundViewModel.winderMaster.collectAsStateWithLifecycle()
    val itemCategoryMaster by inboundViewModel.itemCategoryMaster.collectAsStateWithLifecycle()
    val lastInboundEpc by scanViewModel.lastInboundEpc.collectAsStateWithLifecycle()
    val rfidTagList by scanViewModel.rfidTagList.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        scanViewModel.setEnableScan(false)
    }

    DisposableEffect(Unit) {
        onDispose {
            inboundViewModel.onIntent(InboundIntent.ResetState)
        }
    }

    // Observe event → trigger global dialog qua AppViewModel
    LaunchedEffect(uiState.event) {
        when (uiState.event) {
            is InboundEvent.SaveDbFailed ->
                appViewModel.onGeneralIntent(
                    ShareIntent.ShowDialog(
                        type = DialogType.ERROR,
                        message = MessageMapper.toMessage(StatusCode.SAVE_DATA_TO_DB_FAILED)
                    )
                )
            is InboundEvent.SaveCsvSuccess ->
                appViewModel.onGeneralIntent(
                    ShareIntent.ShowDialog(
                        type = DialogType.SAVE_CSV_SUCCESS_FAILED_SFTP,
                        message = MessageMapper.toMessage(StatusCode.SAVE_CSV_SUCCESS_FAILED_SFTP)
                    )
                )
            is InboundEvent.SaveCsvFailed ->
                appViewModel.onGeneralIntent(
                    ShareIntent.ShowDialog(
                        type = DialogType.SAVE_CSV_FAILED,
                        message = MessageMapper.toMessage(StatusCode.SAVE_DATA_TO_CSV_FAILED)
                    )
                )
            null -> Unit
        }
        if (uiState.event != null) {
            inboundViewModel.onIntent(InboundIntent.EventConsumed)
        }
    }

    TimeDialog(
        showTimeDialog = uiState.showTimePicker,
        title = stringResource(R.string.choose_time),
        confirmText = stringResource(R.string.ok),
        cancelText = stringResource(R.string.cancel),
        onConfirm = { time ->
            when (uiState.inboundInputFieldDateTime) {
                InboundInputField.OCCURRED_AT.code ->
                    inboundViewModel.onIntent(InboundIntent.OccurredAtTimeChanged(time))
                InboundInputField.PROCESSED_AT.code ->
                    inboundViewModel.onIntent(InboundIntent.ProcessedAtTimeChanged(time))
            }
            inboundViewModel.onIntent(InboundIntent.ToggleTimePicker("", false))
        },
        onDismissRequest = {
            inboundViewModel.onIntent(InboundIntent.ToggleTimePicker("", false))
        }
    )

    DateDialog(
        showDateDialog = uiState.showDatePicker,
        confirmText = stringResource(R.string.ok),
        cancelText = stringResource(R.string.cancel),
        onConfirm = { date ->
            when (uiState.inboundInputFieldDateTime) {
                InboundInputField.OCCURRED_AT.code ->
                    inboundViewModel.onIntent(InboundIntent.OccurredAtDateChanged(date))
                InboundInputField.PROCESSED_AT.code ->
                    inboundViewModel.onIntent(InboundIntent.ProcessedAtDateChanged(date))
            }
            inboundViewModel.onIntent(InboundIntent.ToggleDatePicker("", false))
        },
        onDismissRequest = {
            inboundViewModel.onIntent(InboundIntent.ToggleDatePicker("", false))
        }
    )

    Layout(
        topBarText = stringResource(R.string.receiving),
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        appViewModel = appViewModel,
        onNavigate = onNavigate,
        hasBottomBar = true,
        retrySaveDb = {
            inboundViewModel.onIntent(InboundIntent.Retry)
        },
        bottomButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ButtonContainer(
                    buttonText = stringResource(R.string.cancel),
                    containerColor = Color.Red,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .shadow(
                            elevation = 13.dp,
                            clip = true,
                            ambientColor = Color.Gray.copy(alpha = 0.5f),
                            spotColor = Color.DarkGray.copy(alpha = 0.7f)
                        ),
                    onClick = {
                        appViewModel.onGeneralIntent(
                            ShareIntent.ShowDialog(
                                type = DialogType.CONFIRM,
                                message = MessageMapper.toMessage(StatusCode.CANCEL)
                            )
                        )
                    }
                )
                ButtonContainer(
                    buttonText = stringResource(R.string.register),
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.register),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .shadow(
                            elevation = 13.dp,
                            clip = true,
                            ambientColor = Color.Gray.copy(alpha = 0.5f),
                            spotColor = Color.DarkGray.copy(alpha = 0.7f)
                        ),
                    canClick = uiState.category.isNotBlank() && uiState.itemInCategory.isNotBlank(),
                    onClick = {
                        val errors = InputValidate.validate(
                            formItems = uiState.inboundInputFormResults.filter { it.isVisible },
                            uiState = uiState
                        )
                        if (errors.isNotEmpty()) {
                            inboundViewModel.onIntent(InboundIntent.UpdateFieldErrors(errors))
                            return@ButtonContainer
                        }
                        val occurredAt =
                            if (uiState.occurredAtDate.isEmpty() || uiState.occurredAtTime.isEmpty()) null
                            else "${uiState.occurredAtDate}T${uiState.occurredAtTime}"

                        val processedAt =
                            if (uiState.processedAtDate.isEmpty() || uiState.processedAtTime.isEmpty()) null
                            else "${uiState.processedAtDate}T${uiState.processedAtTime}"

                        val now = generateIso8601JstTimestamp()

                        inboundViewModel.onIntent(
                            InboundIntent.Execute(
                                sourceEventId = UUID.randomUUID().toString(),
                                occurredAt = occurredAt,
                                processedAt = processedAt,
                                now = now,
                                rfidTag = rfidTagList.find { it.epc == lastInboundEpc }
                            )
                        )
                    }
                )
            }
        },
        onBackArrowClick = { onGoBack() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                CardContainer {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = stringResource(R.string.item_code, lastInboundEpc ?: "")
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        ExposedDropdownMenuBox(
                            expanded = uiState.categoryExpanded,
                            onExpandedChange = {
                                inboundViewModel.onIntent(InboundIntent.ToggleCategoryExpanded)
                            }
                        ) {
                            InputFieldContainer(
                                modifier = Modifier
                                    .menuAnchor(
                                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                        enabled = true
                                    )
                                    .fillMaxWidth(),
                                value = if (uiState.category == SelectTitle.SelectCategory.displayName) ""
                                else uiState.category,
                                hintText = SelectTitle.SelectCategory.displayName,
                                isNumeric = false,
                                onChange = {},
                                readOnly = true,
                                isDropDown = true,
                                enable = true,
                                onEnterPressed = {}
                            )
                            ExposedDropdownMenu(
                                expanded = uiState.categoryExpanded,
                                onDismissRequest = {
                                    inboundViewModel.onIntent(InboundIntent.ToggleCategoryExpanded)
                                }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(text = SelectTitle.SelectCategory.displayName) },
                                    onClick = {
                                        inboundViewModel.onIntent(
                                            InboundIntent.CategoryChanged(categoryId = 0, value = "")
                                        )
                                    }
                                )
                                itemCategoryMaster.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(text = category.itemCategoryName) },
                                        onClick = {
                                            inboundViewModel.onIntent(
                                                InboundIntent.CategoryChanged(
                                                    categoryId = category.itemCategoryId,
                                                    value = category.itemCategoryName
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                        }
                        if (uiState.category.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            ItemSearchBar(
                                keyword = uiState.itemInCategory,
                                results = uiState.searchResults,
                                onKeywordChange = { keyword ->
                                    inboundViewModel.onIntent(
                                        InboundIntent.SearchKeywordChanged(
                                            keyword = keyword,
                                            categoryName = uiState.category
                                        )
                                    )
                                },
                                onSelectItem = { itemName, itemId ->
                                    inboundViewModel.onIntent(
                                        InboundIntent.ItemInCategoryChanged(
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
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            items(
                items = uiState.inboundInputFormResults
                    .filter { it.isVisible }
                    .sortedWith(
                        compareBy {
                            if (it.fieldName == InboundInputField.MEMO.displayName) 1 else 0
                        }
                    ),
                key = { it.fieldName }
            ) { result ->
                InboundInputFormItem(
                    result = result,
                    uiState = uiState,
                    inboundViewModel = inboundViewModel,
                    locationMaster = locationMaster,
                    winderMaster = winderMaster,
                )
            }
        }
    }
}