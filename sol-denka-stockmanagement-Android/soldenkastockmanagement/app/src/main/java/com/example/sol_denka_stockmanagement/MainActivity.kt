package com.example.sol_denka_stockmanagement

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.sol_denka_stockmanagement.navigation.Navigation3
import com.example.sol_denka_stockmanagement.screen.inventory.InventoryViewModel
import com.example.sol_denka_stockmanagement.screen.setting.SettingViewModel
import com.example.sol_denka_stockmanagement.ui.theme.SoldenkastockmanagementTheme
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting.AppSettingViewModel
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.reader_setting.ReaderSettingViewModel
import com.example.sol_denka_stockmanagement.search.SearchTagsViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val appViewModel: AppViewModel by viewModels()
    private val appSettingViewModel: AppSettingViewModel by viewModels()
    private val readerSettingViewModel: ReaderSettingViewModel by viewModels()
    private val scanViewModel: ScanViewModel by viewModels()
    private val searchTagsViewModel: SearchTagsViewModel by viewModels()
    private val inventoryViewModel: InventoryViewModel by viewModels()
    private val settingViewModel: SettingViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SoldenkastockmanagementTheme {
                Navigation3(
                    appViewModel = appViewModel,
                    appSettingViewModel = appSettingViewModel,
                    readerSettingViewModel = readerSettingViewModel,
                    scanViewModel = scanViewModel,
                    searchTagsViewModel = searchTagsViewModel,
                    inventoryViewModel = inventoryViewModel,
                    settingViewModel = settingViewModel
                )
            }
        }
    }
}
