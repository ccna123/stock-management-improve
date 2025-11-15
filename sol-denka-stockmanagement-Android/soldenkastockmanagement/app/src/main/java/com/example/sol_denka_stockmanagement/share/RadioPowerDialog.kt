package com.example.sol_denka_stockmanagement.share

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.intent.InventoryScanIntent
import com.example.sol_denka_stockmanagement.intent.ReaderSettingIntent
import com.example.sol_denka_stockmanagement.screen.setting.components.reader_setting_tab.SettingBoxContainer
import com.example.sol_denka_stockmanagement.screen.setting.components.reader_setting_tab.SettingItemTitle
import com.example.sol_denka_stockmanagement.search.SearchScreenIntent
import com.example.sol_denka_stockmanagement.state.ReaderSettingState
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun RadioPowerDialog(
    showDialog: Boolean,
    readerSettingState: ReaderSettingState,
    onMinPower: () -> Unit,
    onMaxPower: () -> Unit,
    onChangeSlider: (Int) -> Unit,
    onOk: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog.not()) return
    AppDialog {
        Column {
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
                        .padding(PaddingValues(vertical = 10.dp)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Red
                        ),
                        onClick = { onMinPower() }
                    ) {
                        Text(text = stringResource(R.string.setting_label_min), color = Color.White)
                    }
                    Slider(
                        value = readerSettingState.radioPowerSliderPosition.toFloat(),
                        onValueChange = { newValue ->
                            onChangeSlider(newValue.roundToInt().coerceIn(0, 30))
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
                        onClick = { onMaxPower() }
                    ) {
                        Text(text = stringResource(R.string.setting_label_max), color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ButtonContainer(
                    buttonText = stringResource(R.string.apply_setting),
                    onClick = { onOk() }
                )
                Spacer(modifier = Modifier.width(10.dp))
                ButtonContainer(
                    buttonText = stringResource(R.string.close),
                    containerColor = Color.Red,
                    onClick = { onDismiss() }
                )
            }
        }
    }
}