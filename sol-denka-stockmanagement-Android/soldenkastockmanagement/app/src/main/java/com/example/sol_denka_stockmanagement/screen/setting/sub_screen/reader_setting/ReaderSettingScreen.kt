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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.intent.ReaderSettingIntent
import com.example.sol_denka_stockmanagement.reader.FakeBeeperVolume
import com.example.sol_denka_stockmanagement.reader.FakeInventoryState
import com.example.sol_denka_stockmanagement.reader.FakeSession
import com.example.sol_denka_stockmanagement.screen.setting.components.reader_setting_tab.SettingBoxContainer
import com.example.sol_denka_stockmanagement.screen.setting.components.reader_setting_tab.SettingItemTitle
import com.example.sol_denka_stockmanagement.share.InputFieldContainer
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.skyBlue
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ReaderSettingScreen(
    readerSettingViewModel: ReaderSettingViewModel
) {
    val readerSettingState by readerSettingViewModel.readerSettingState.collectAsState()
    var isSessionExpanded by remember { mutableStateOf(false) }
    var isFlagExpanded by remember { mutableStateOf(false) }
    var isTagPopulationExpanded by remember { mutableStateOf(false) }
    var isUsedChannelExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding() // avoid bottom overlap
            .padding(bottom = 100.dp) // keep bottom button visible
    ) {
        Text(
            color = brightAzure,
            text = stringResource(R.string.setting_basic_title),
            modifier = Modifier.padding(8.dp)
        )
        // RFID Volume
        SettingBoxContainer {
            SettingItemTitle(text = stringResource(R.string.setting_rfid_volume))
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
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (readerSettingState.buzzerVolume == volume) brightAzure else Color.LightGray
                            ),
                            contentPadding = PaddingValues(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            ), // Reduced padding
                            onClick = {
                                readerSettingViewModel.onIntent(
                                    ReaderSettingIntent.ChangeVolume(
                                        volume
                                    )
                                )
                            }
                        ) {
                            Text(
                                text = volume.displayName,
                                fontSize = 20.sp, // Smaller text size
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()

        // RFID Power
        SettingBoxContainer {
            SettingItemTitle(text = stringResource(R.string.setting_rfid_power))
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "${readerSettingState.radioPower}dbm / ${
                    NumberFormat.getNumberInstance(Locale.getDefault())
                        .format(readerSettingState.radioPowerMw)
                }Mw"
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingValues(top = 10.dp, start = 15.dp, end = 15.dp)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Red
                    ),
                    onClick = {
                        readerSettingViewModel.apply {
                            onIntent(
                                ReaderSettingIntent.ChangeRadioPowerSliderPosition(
                                    0
                                )
                            )
                            onIntent(ReaderSettingIntent.ChangeRadioPower(0))
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.setting_label_min), color = Color.White)
                }
                Slider(
                    value = readerSettingState.radioPowerSliderPosition.toFloat(),
                    onValueChange = { newValue ->
                        val intValue =
                            newValue.roundToInt().coerceIn(0, 30) // Convert to Int and clamp
                        readerSettingViewModel.apply {
                            onIntent(
                                ReaderSettingIntent.ChangeRadioPowerSliderPosition(
                                    intValue
                                )
                            )
                            onIntent(ReaderSettingIntent.ChangeRadioPower(intValue))
                        }
                    },
                    onValueChangeFinished = {},
                    valueRange = 0f..30f,
                    colors = SliderDefaults.colors(
                        thumbColor = brightAzure
                    ),
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .width(200.dp)
                )
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color(0xFF8949E5)
                    ),
                    onClick = {
                        readerSettingViewModel.apply {
                            onIntent(
                                ReaderSettingIntent.ChangeRadioPowerSliderPosition(
                                    30
                                )
                            )
                            onIntent(ReaderSettingIntent.ChangeRadioPower(30))
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.setting_label_max), color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
        }
        // RFID Advanced
        Text(
            color = brightAzure,
            modifier = Modifier.padding(8.dp),
            text = stringResource(R.string.setting_rfid_advanced)
        )

        ExpandableSettingSection(
            title = stringResource(R.string.setting_rfid_session),
            description = stringResource(R.string.setting_rfid_session_des),
            expanded = isSessionExpanded,
            value = readerSettingState.rfidSession.toString(),
            onExpandToggle = { isSessionExpanded = !isSessionExpanded },
            content = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(
                        FakeSession.SESSION_S0,
                        FakeSession.SESSION_S1,
                        FakeSession.SESSION_S2,
                        FakeSession.SESSION_S3,
                    ).forEach { session ->
                        Row(
                            modifier = Modifier
                                .background(
                                    color = if (readerSettingState.rfidSession == session) skyBlue.copy(
                                        alpha = 0.4f
                                    ) else Color.Unspecified, shape = RoundedCornerShape(10.dp)
                                )
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            RadioButton(
                                selected = readerSettingState.rfidSession == session,
                                onClick = {
                                    readerSettingViewModel.onIntent(
                                        ReaderSettingIntent.ChangeSession(
                                            session
                                        )
                                    )
                                },
                                enabled = true,
                            )
                            DropdownMenuItem(
                                text = { Text(session.toString()) },
                                onClick = {
                                    readerSettingViewModel.onIntent(
                                        ReaderSettingIntent.ChangeSession(
                                            session
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        )
        HorizontalDivider()
        // Tag Population
        ExpandableSettingSection(
            title = stringResource(R.string.setting_tag_population_string),
            description = stringResource(R.string.setting_rfid_tag_population_des),
            expanded = isTagPopulationExpanded,
            value = readerSettingState.tagPopulation.toString(),
            onExpandToggle = { isTagPopulationExpanded = !isTagPopulationExpanded }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                mapOf(
                    10 to "少数（1〜10タグ）",
                    50 to "中程度（10〜50タグ）",
                    100 to "多い（50〜100タグ）",
                    200 to "非常に多い（100タグ以上）"
                ).forEach { (key, label) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            readerSettingViewModel.onIntent(
                                ReaderSettingIntent.ChangeTagPopulation(key.toShort())
                            )
                            isTagPopulationExpanded = false
                        }
                    )
                }
                // Optional manual input box
                InputFieldContainer(
                    modifier = Modifier.fillMaxWidth(),
                    value = readerSettingState.tagPopulation.toString(),
                    hintText = stringResource(R.string.tag_population_hint),
                    isNumeric = true,
                    readOnly = false,
                    isDropDown = false,
                    enable = true,
                    onChange = { newValue ->
                        val filteredValue = newValue.filter { char -> char.isDigit() || char == '.' }
                        readerSettingViewModel.onIntent(
                            ReaderSettingIntent.ChangeTagPopulation(filteredValue.toShortOrNull() ?: 0)
                        )
                    }
                )
            }
        }
        HorizontalDivider()
        // RFID Flag
        ExpandableSettingSection(
            title = stringResource(R.string.setting_rfid_flag),
            description = stringResource(R.string.setting_rfid_flag_des),
            value = readerSettingState.tagAccessFlag.toString(),
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
                                    color = if (readerSettingState.tagAccessFlag == accessFlag) skyBlue.copy(
                                        alpha = 0.4f
                                    ) else Color.Unspecified, shape = RoundedCornerShape(10.dp)
                                )
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            RadioButton(
                                selected = readerSettingState.tagAccessFlag == accessFlag,
                                onClick = {
                                    readerSettingViewModel.onIntent(
                                        ReaderSettingIntent.ChangeTagAccessFlag(
                                            accessFlag
                                        )
                                    )
                                },
                                enabled = true,
                            )
                            DropdownMenuItem(
                                text = { Text(accessFlag.toString()) },
                                onClick = {
                                    readerSettingViewModel.onIntent(
                                        ReaderSettingIntent.ChangeTagAccessFlag(
                                            accessFlag
                                        )
                                    )
                                }
                            )
                        }

                    }
                }
            }
        )
        HorizontalDivider()
        // RFID Channel
        ExpandableSettingSection(
            title = stringResource(R.string.setting_rfid_channel),
            description = stringResource(R.string.setting_rfid_channel_des),
                    value = readerSettingState.enabledRfidChannel.joinToString(", "),
            expanded = isUsedChannelExpanded,
            onExpandToggle = { isUsedChannelExpanded = !isUsedChannelExpanded },
            content = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    readerSettingState.supportedChannels.forEach { channel ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Switch(
                                checked = channel in readerSettingState.enabledRfidChannel,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,               // circle (on)
                                    checkedTrackColor = skyBlue,         // track (on)
                                    checkedBorderColor = Color.Transparent,
                                    uncheckedThumbColor = Color.White,             // circle (off)
                                    uncheckedTrackColor = Color(0xFFB0BEC5),       // track (off)
                                    uncheckedBorderColor = Color.Transparent,
                                    disabledCheckedTrackColor = Color.Gray.copy(alpha = 0.3f)
                                ),
                                modifier = Modifier
                                    .graphicsLayer(scaleX = 0.8f, scaleY = 0.8f),
                                onCheckedChange = { isChecked ->
                                    val updatedList = if (isChecked) {
                                        readerSettingState.enabledRfidChannel + channel
                                    } else {
                                        readerSettingState.enabledRfidChannel - channel
                                    }
                                    readerSettingViewModel.onIntent(
                                        ReaderSettingIntent.ChangeUsedChannel(
                                            updatedList
                                        )
                                    )
                                },
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
                Text(title, fontSize = 16.sp)
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null
                )
            }
            Text(value, fontSize = 16.sp, color = Color.LightGray)
        }
        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                if (description.isNotEmpty()){
                    Text(description, fontSize = 13.sp, color = Color.Gray)
                    Spacer(Modifier.height(10.dp))
                }
                content()
            }
        }
    }
}