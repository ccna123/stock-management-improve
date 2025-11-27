package com.example.sol_denka_stockmanagement.screen.setting.sub_screen.reader_setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.reader.FakeBeeperVolume
import com.example.sol_denka_stockmanagement.reader.FakeChannel
import com.example.sol_denka_stockmanagement.reader.FakeInventoryState
import com.example.sol_denka_stockmanagement.reader.FakeSession
import com.example.sol_denka_stockmanagement.screen.setting.components.reader_setting_tab.RadioPowerSetting
import com.example.sol_denka_stockmanagement.screen.setting.components.reader_setting_tab.SettingBoxContainer
import com.example.sol_denka_stockmanagement.screen.setting.components.reader_setting_tab.SettingItemTitle
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.skyBlue

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ReaderSettingScreen(
    buzzerVolume: FakeBeeperVolume,
    radioPower: Int,
    radioPowerMw: Int,
    radioPowerSliderPosition: Int,
    session: FakeSession,
    tagPopulation: String,
    tagAccessFlag: FakeInventoryState,
    enabledChannels: List<FakeChannel>,
    supportedChannels: List<FakeChannel>,
    onChangeVolume: (FakeBeeperVolume) -> Unit,
    onChangePower: (Int) -> Unit,
    onChangeMinPower: (Int) -> Unit,
    onChangeMaxPower: (Int) -> Unit,
    onChangeSlider: (Int) -> Unit,
    onChangeSession: (FakeSession) -> Unit,
    onChangeTagAccessFlag: (FakeInventoryState) -> Unit,
    onChangeChannel: (FakeChannel) -> Unit,
    onChangeTagPopulation: (String) -> Unit
) {

    var isSessionExpanded by remember { mutableStateOf(false) }
    var isFlagExpanded by remember { mutableStateOf(false) }
    var isTagPopulationExpanded by remember { mutableStateOf(false) }
    var isUsedChannelExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .imePadding()
            .padding(bottom = 100.dp) // keep bottom button visible
    ) {
        item {
            Text(
                color = brightAzure,
                fontSize = 20.sp,
                text = stringResource(R.string.setting_basic_title),
                modifier = Modifier.padding(8.dp)
            )
            // RFID Volume
            SettingBoxContainer {
                SettingItemTitle(
                    text = stringResource(R.string.setting_rfid_volume),
                    textSize = 20.sp
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(PaddingValues(top = 10.dp, start = 12.dp, end = 12.dp)),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FakeBeeperVolume.entries.forEach { volume ->
                            ButtonContainer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                containerColor = if (buzzerVolume == volume) brightAzure else Color.LightGray.copy(
                                    alpha = 0.6f
                                ),
                                buttonText = volume.displayName,
                                buttonTextSize = 20,
                                onClick = { onChangeVolume(volume) }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = brightAzure)

            // RFID Power
            SettingBoxContainer {
                RadioPowerSetting(
                    radioPower = radioPower,
                    radioPowerMw = radioPowerMw,
                    radioPowerSliderPosition = radioPowerSliderPosition.toFloat(),
                    onChangeMinPower = {
                        onChangeMinPower(0)
                        onChangePower(0)
                    },
                    onChangeMaxPower = {
                        onChangeMaxPower(30)
                        onChangePower(30)
                    },
                    onChangeSlider = {
                        onChangeSlider(it)
                        onChangePower(it)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = brightAzure)
            }
            // RFID Advanced
            Text(
                color = brightAzure,
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp),
                text = stringResource(R.string.setting_rfid_advanced)
            )

            ExpandableSettingSection(
                title = stringResource(R.string.setting_rfid_session),
                description = stringResource(R.string.setting_rfid_session_des),
                expanded = isSessionExpanded,
                value = session.toString(),
                onExpandToggle = { isSessionExpanded = !isSessionExpanded },
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(
                            FakeSession.SESSION_S0,
                            FakeSession.SESSION_S1,
                            FakeSession.SESSION_S2,
                            FakeSession.SESSION_S3,
                        ).forEach { rfidSession ->
                            Row(
                                modifier = Modifier
                                    .background(
                                        color = if (rfidSession == session) skyBlue.copy(
                                            alpha = 0.4f
                                        ) else Color.Unspecified, shape = RoundedCornerShape(10.dp)
                                    )
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                RadioButton(
                                    selected = rfidSession == session,
                                    onClick = { onChangeSession(rfidSession) },
                                    enabled = true,
                                )
                                DropdownMenuItem(
                                    text = { Text(session.toString()) },
                                    onClick = { onChangeSession(rfidSession) }
                                )
                            }
                        }
                    }
                }
            )
            HorizontalDivider(color = brightAzure)
            // Tag Population
            ExpandableSettingSection(
                title = stringResource(R.string.setting_tag_population_string),
                description = stringResource(R.string.setting_rfid_tag_population_des),
                expanded = isTagPopulationExpanded,
                value = tagPopulation,
                onExpandToggle = { isTagPopulationExpanded = !isTagPopulationExpanded }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    mapOf(
                        10 to "少数（1〜10タグ）",
                        50 to "中程度（10〜50タグ）",
                        100 to "多い（50〜100タグ）",
                        200 to "非常に多い（100タグ以上）"
                    ).forEach { (_, label) ->
                        Text(
                            text = label,
                        )
                    }
                    // Optional manual input box
                    InputFieldContainer(
                        modifier = Modifier.fillMaxWidth(),
                        value = tagPopulation,
                        hintText = stringResource(R.string.tag_population_hint),
                        isNumeric = true,
                        readOnly = false,
                        isDropDown = false,
                        enable = true,
                        onChange = { newValue ->
                            onChangeTagPopulation(newValue)
                        }
                    )
                }
            }
            HorizontalDivider(color = brightAzure)
            // RFID Flag
            ExpandableSettingSection(
                title = stringResource(R.string.setting_rfid_flag),
                description = stringResource(R.string.setting_rfid_flag_des),
                value = tagAccessFlag.toString(),
                expanded = isFlagExpanded,
                onExpandToggle = { isFlagExpanded = !isFlagExpanded },
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(
                            FakeInventoryState.INVENTORY_STATE_A,
                            FakeInventoryState.INVENTORY_STATE_B,
                            FakeInventoryState.INVENTORY_STATE_AB_FLIP,
                        ).forEach { accessFlag ->
                            Row(
                                modifier = Modifier
                                    .background(
                                        color = if (tagAccessFlag == accessFlag) skyBlue.copy(
                                            alpha = 0.4f
                                        ) else Color.Unspecified, shape = RoundedCornerShape(10.dp)
                                    )
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                RadioButton(
                                    selected = tagAccessFlag == accessFlag,
                                    onClick = {
                                        onChangeTagAccessFlag(accessFlag)
                                    },
                                    enabled = true,
                                )
                                DropdownMenuItem(
                                    text = { Text(accessFlag.toString()) },
                                    onClick = {
                                        onChangeTagAccessFlag(accessFlag)
                                    }
                                )
                            }

                        }
                    }
                }
            )
            HorizontalDivider(color = brightAzure)
            // RFID Channel
            ExpandableSettingSection(
                title = stringResource(R.string.setting_rfid_channel),
                description = stringResource(R.string.setting_rfid_channel_des),
                value = enabledChannels.joinToString(", "),
                expanded = isUsedChannelExpanded,
                onExpandToggle = { isUsedChannelExpanded = !isUsedChannelExpanded },
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        supportedChannels.forEach { channel ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Switch(
                                    checked = channel in enabledChannels,
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,               // circle (on)
                                        checkedTrackColor = brightAzure,         // track (on)
                                        checkedBorderColor = Color.Transparent,
                                        uncheckedThumbColor = Color.White,             // circle (off)
                                        uncheckedTrackColor = Color(0xFFB0BEC5),       // track (off)
                                        uncheckedBorderColor = Color.Transparent,
                                        disabledCheckedTrackColor = Color.Gray.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier
                                        .graphicsLayer(scaleX = 0.8f, scaleY = 0.8f),
                                    onCheckedChange = { onChangeChannel(channel) },
                                )
                                DropdownMenuItem(
                                    text = { Text(channel.name) },
                                    onClick = {
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ExpandableSettingSection(
    title: String,
    description: String = "",
    expanded: Boolean,
    value: String,
    onExpandToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpandToggle() }
            .padding(8.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(title, fontSize = 20.sp)
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null
                )
            }
            Text(value, fontSize = 14.sp, color = Color.Black.copy(alpha = 0.4f))
        }
        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                if (description.isNotEmpty()) {
                    Text(description, fontSize = 13.sp, color = Color.Gray)
                    Spacer(Modifier.height(10.dp))
                }
                content()
            }
        }
    }
}