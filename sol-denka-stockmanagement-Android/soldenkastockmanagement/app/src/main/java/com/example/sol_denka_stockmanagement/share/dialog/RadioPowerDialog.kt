package com.example.sol_denka_stockmanagement.share.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.screen.setting.components.reader_setting_tab.RadioPowerSetting
import com.example.sol_denka_stockmanagement.screen.setting.components.reader_setting_tab.SettingBoxContainer
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.state.ReaderSettingState

@Composable
fun RadioPowerDialog(
    showDialog: Boolean,
    readerSettingState: ReaderSettingState,
    onChangeMinPower: () -> Unit,
    onChangeMaxPower: () -> Unit,
    onChangeSlider: (Int) -> Unit,
    onOk: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog.not()) return
    AppDialog {
        Column {
            SettingBoxContainer {
                RadioPowerSetting(
                    radioPower = readerSettingState.radioPower,
                    radioPowerMw = readerSettingState.radioPowerMw,
                    radioPowerSliderPosition = readerSettingState.radioPowerSliderPosition.toFloat(),
                    onChangeMinPower = { onChangeMinPower() },
                    onChangeMaxPower = { onChangeMaxPower() },
                    onChangeSlider = { onChangeSlider(it) }
                )
                Spacer(modifier = Modifier.height(8.dp))
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