package com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.screen.setting.components.app_setting_tab.AppSettingRow
import com.example.sol_denka_stockmanagement.share.ButtonType
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting.AppSettingViewModel
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppSettingScreen(
    appSettingViewModel: AppSettingViewModel,
    appViewModel: AppViewModel,
    onAutoConnectCheck: (Boolean) -> Unit,
) {
    val inputState by appViewModel.inputState.collectAsStateWithLifecycle()
    val expandState by appViewModel.expandState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current


    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        // Auto connect
        AppSettingRow(
            topText = "Setting",
            bottomText = "Setting",
            onCheck = onAutoConnectCheck,
            appSettingViewModel = appSettingViewModel,
            buttonType = ButtonType.Switch.displayName
        )
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider()
    }
}