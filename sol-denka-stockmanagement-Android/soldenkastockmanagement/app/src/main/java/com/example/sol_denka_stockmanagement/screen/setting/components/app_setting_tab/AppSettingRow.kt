package com.example.sol_denka_stockmanagement.screen.setting.components.app_setting_tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.share.ButtonType
import com.example.sol_denka_stockmanagement.state.ExpandState
import com.example.sol_denka_stockmanagement.state.InputState
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting.AppSettingViewModel
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSettingRow(
    isChecked: Boolean? = false,
    topText: String,
    bottomText: String,
    buttonType: String,
    buttonText: String = "",
    additionalText: String = "",
    canClick: Boolean = true,
    expandState: ExpandState? = null,
    inputState: InputState? = null,
    appSettingViewModel: AppSettingViewModel,
    appViewModel: AppViewModel? = null,
    onCheck: ((Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = topText)
            Text(
                color = Color.LightGray,
                fontSize = 14.sp,
                text = bottomText
            )
        }
        when (buttonType) {
            ButtonType.Switch.displayName -> {
                Switch(
                    checked = isChecked ?: false,
                    onCheckedChange = { newValue -> onCheck?.invoke(newValue) },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = brightAzure,
                        uncheckedTrackColor = Color.LightGray,
                        uncheckedBorderColor = Color.Transparent,
                        uncheckedThumbColor = Color.White
                    ),
                )
            }
        }
    }
}