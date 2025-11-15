package com.example.sol_denka_stockmanagement.screen.shipping

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.sol_denka_stockmanagement.constant.HandlingMethod
import com.example.sol_denka_stockmanagement.constant.MaterialSelectionItem
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.intent.ExpandIntent
import com.example.sol_denka_stockmanagement.intent.InputIntent
import com.example.sol_denka_stockmanagement.model.ShippingItemModel
import com.example.sol_denka_stockmanagement.model.TagInfoModel
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.screen.scan.shipping.ShippingScanViewModel
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.state.ErrorState
import com.example.sol_denka_stockmanagement.state.ExpandState
import com.example.sol_denka_stockmanagement.state.InputState
import com.example.sol_denka_stockmanagement.ui.theme.brightGreen
import com.example.sol_denka_stockmanagement.ui.theme.brightOrange
import com.example.sol_denka_stockmanagement.ui.theme.deepBlueSky
import com.example.sol_denka_stockmanagement.ui.theme.paleSkyBlue
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.reader_setting.ReaderSettingViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ShippingScreen(
    appViewModel: AppViewModel,
    scanViewModel: ScanViewModel,
    shippingScanViewModel: ShippingScanViewModel,
    onNavigate: (Screen) -> Unit
) {

    val errorState by appViewModel.errorState.collectAsStateWithLifecycle()
    val expandState by appViewModel.expandState.collectAsStateWithLifecycle()
    val inputState by appViewModel.inputState.collectAsStateWithLifecycle()

    Layout(
        topBarText = stringResource(R.string.shipping),
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigate = onNavigate,
        appViewModel = appViewModel,
        currentScreenNameId = Screen.Shipping.routeId,
        prevScreenNameId = Screen.Shipping.routeId, // for scan screen to navigate back,
        hasBottomBar = true,
        bottomButton = {
            ButtonContainer(
                modifier = Modifier.shadow(
                    elevation = 13.dp, clip = true, ambientColor = Color.Gray.copy(alpha = 0.5f),
                    spotColor = Color.DarkGray.copy(alpha = 0.7f)
                ),
                shape = RoundedCornerShape(10.dp),
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.register),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                onClick = {
                },
                buttonText = stringResource(R.string.register_shipping),
            )
        },
        onBackArrowClick = {
            onNavigate(Screen.ShippingScan)
        }) { paddingValues ->
        ShippingScreenContent(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
            appViewModel = appViewModel,
            errorState = errorState,
            inputState = inputState,
            expandState = expandState,
            selectedTags = shippingScanViewModel.selectedTags.value,
            onUpdateInput = { intent ->
                appViewModel.onInputIntent(intent)
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShippingScreenContent(
    modifier: Modifier,
    appViewModel: AppViewModel,
    errorState: ErrorState,
    inputState: InputState,
    expandState: ExpandState,
    selectedTags: Set<String>,
    onUpdateInput: (InputIntent) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .imePadding()
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, color = paleSkyBlue, shape = RoundedCornerShape(15.dp))
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 10.dp)
                        .fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "${stringResource(R.string.material_name)}:",
                            modifier = Modifier.alignByBaseline()
                        )
                        Text(
                            text = MaterialSelectionItem.MISS_ROLL.displayName,
                            modifier = Modifier.alignByBaseline()
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "${stringResource(R.string.quantity)}:",
                            modifier = Modifier.alignByBaseline()
                        )
                        Text(
                            text = selectedTags.size.toString(),
                            modifier = Modifier.alignByBaseline()
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            ExposedDropdownMenuBox(
                expanded = expandState.handlingMethodExpanded,
                onExpandedChange = { appViewModel.onExpandIntent(ExpandIntent.ToggleHandlingMethodExpanded) }) {
                InputFieldContainer(
                    modifier = Modifier
                        .menuAnchor(
                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        )
                        .fillMaxWidth(),
                    value = if (inputState.handlingMethod == SelectTitle.SelectHandlingMethod.displayName) "" else inputState.handlingMethod,
                    isNumeric = false,
                    hintText = SelectTitle.SelectHandlingMethod.displayName,
                    shape = RoundedCornerShape(13.dp),
                    onChange = { newValue ->
                        onUpdateInput(InputIntent.UpdateHandlingMethod(newValue))
                    },
                    readOnly = true,
                    isDropDown = true,
                    enable = true,
                    onClick = {
                        appViewModel.onExpandIntent(ExpandIntent.ToggleHandlingMethodExpanded)
                    },
                )
                ExposedDropdownMenu(
                    expanded = expandState.handlingMethodExpanded,
                    onDismissRequest = { appViewModel.onExpandIntent(ExpandIntent.ToggleMissRollExpanded) }
                ) {
                    listOf(
                        HandlingMethod.SELECTION_TITLE.displayName,
                        HandlingMethod.USE.displayName,
                        HandlingMethod.SALE.displayName,
                        HandlingMethod.CRUSHING.displayName,
                    ).forEach { method ->
                        DropdownMenuItem(
                            text = { Text(text = method) },
                            onClick = {
                                appViewModel.apply {
                                    onInputIntent(InputIntent.UpdateHandlingMethod(if (method == SelectTitle.SelectHandlingMethod.displayName) "" else method))
                                    onExpandIntent(ExpandIntent.ToggleHandlingMethodExpanded)
                                }
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            InputFieldContainer(
                modifier = Modifier.fillMaxWidth(),
                value = inputState.remark,
                label = "備考",
                hintText = stringResource(R.string.remark_hint),
                isNumeric = false,
                shape = RoundedCornerShape(13.dp),
                readOnly = false,
                isDropDown = false,
                enable = true,
                onChange = { newValue ->
                    val filteredValue = newValue.trimStart().filter { char ->
                        (char.isLetterOrDigit() && char.toString()
                            .toByteArray().size == 1) || char == '-'
                    }
                    appViewModel.onInputIntent(
                        InputIntent.UpdateRemark(
                            filteredValue
                        )
                    )
                }
            )
        }
    }
}