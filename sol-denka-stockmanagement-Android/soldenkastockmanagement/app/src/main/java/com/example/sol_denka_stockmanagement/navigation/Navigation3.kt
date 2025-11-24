package com.example.sol_denka_stockmanagement.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.sol_denka_stockmanagement.screen.csv.CsvExportScreen
import com.example.sol_denka_stockmanagement.screen.csv.CsvImportScreen
import com.example.sol_denka_stockmanagement.screen.detail.DetailScreen
import com.example.sol_denka_stockmanagement.screen.home.HomeScreen
import com.example.sol_denka_stockmanagement.screen.inventory.complete.InventoryCompleteScreen
import com.example.sol_denka_stockmanagement.screen.inventory.input.InventoryScreen
import com.example.sol_denka_stockmanagement.screen.inventory.scan.InventoryScanScreen
import com.example.sol_denka_stockmanagement.screen.receiving.ReceivingScreen
import com.example.sol_denka_stockmanagement.screen.scan.ScanScreen
import com.example.sol_denka_stockmanagement.screen.shipping.ShippingScreen
import com.example.sol_denka_stockmanagement.screen.version.VersionInfoScreen
import com.example.sol_denka_stockmanagement.screen.version.sub_screen.LicenseInfoScreen
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting.AppSettingViewModel
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.reader_setting.ReaderSettingViewModel
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.screen.setting.SettingScreen
import com.example.sol_denka_stockmanagement.screen.storage_area_change.StorageAreaChangeScreen
import com.example.sol_denka_stockmanagement.search.SearchTagsScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Navigation3(
    appViewModel: AppViewModel,
    appSettingViewModel: AppSettingViewModel,
    readerSettingViewModel: ReaderSettingViewModel,
    scanViewModel: ScanViewModel,
) {
    val backStack = remember { mutableStateListOf<Screen>(Screen.Home) }
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Screen.Home> {
                HomeScreen(
                    appViewModel = appViewModel,
                    scanViewModel = scanViewModel,
                    onNavigate = { dest ->
                        if (backStack.last() != dest) {
                            backStack.add(dest)
                        }
                    }
                )
            }
            entry<Screen.Shipping> {
                ShippingScreen(
                    appViewModel = appViewModel,
                    onNavigate = { dest ->
                        if (backStack.last() != dest) {
                            backStack.add(dest)
                        }
                    }
                )
            }
            entry<Screen.Inventory> {
                InventoryScreen(
                    appViewModel = appViewModel,
                    scanViewModel = scanViewModel,
                    onNavigate = { dest ->
                        if (backStack.last() != dest) {
                            backStack.add(dest)
                        }
                    }
                )
            }
            entry<Screen.InventoryScan> { destinationScan ->
                InventoryScanScreen(
                    appViewModel = appViewModel,
                    scanViewModel = scanViewModel,
                    readerSettingViewModel = readerSettingViewModel,
                    prevScreenNameId = destinationScan.preScreen,
                    onNavigate = { dest ->
                        if (backStack.last() != dest) {
                            backStack.add(dest)
                        }
                    }
                )
            }
            entry<Screen.InventoryComplete> {
                InventoryCompleteScreen(
                    appViewModel = appViewModel,
                    scanViewModel = scanViewModel,
                    onNavigate = { dest ->
                        if (backStack.last() != dest) {
                            backStack.add(dest)
                        }
                    }
                )
            }
            entry<Screen.Receiving> {
                ReceivingScreen(
                    appViewModel = appViewModel,
                    scanViewModel = scanViewModel,
                    onNavigate = { dest ->
                        if (backStack.last() != dest) {
                            backStack.add(dest)
                        }
                    }
                )
            }
            entry<Screen.StorageAreaChange> { destinationScan ->
                StorageAreaChangeScreen(
                    appViewModel = appViewModel,
                    onNavigate = { dest ->
                        if (backStack.last() != dest) {
                            backStack.add(dest)
                        } else if (dest is Screen.Home) {
                            backStack.clear()
                            backStack.add(Screen.Home)
                        }
                    }
                )
            }
            entry<Screen.Scan> { destinationScan ->
                ScanScreen(
                    appViewModel = appViewModel,
                    scanViewModel = scanViewModel,
                    prevScreenNameId = destinationScan.preScreen,
                    onNavigate = { dest ->
                        if (backStack.last() != dest) {
                            backStack.add(dest)
                        } else if (dest is Screen.Home) {
                            backStack.clear()
                            backStack.add(Screen.Home)
                        }
                    }
                )
            }
            entry<Screen.SearchTagsScreen> { destinationScan ->
                SearchTagsScreen(
                    appViewModel = appViewModel,
                    prevScreenNameId = destinationScan.preScreen,
                    readerSettingViewModel = readerSettingViewModel,
                    scanViewModel = scanViewModel,
                    onNavigate = { dest ->
                        if (backStack.last() != dest) {
                            backStack.add(dest)
                        } else if (dest is Screen.Home) {
                            backStack.clear()
                            backStack.add(Screen.Home)
                        }
                    }
                )
            }
            entry<Screen.VersionInfo> {
                VersionInfoScreen(
                    appViewModel = appViewModel,
                    onNavigate = { dest ->
                        if (backStack.last() != dest) {
                            backStack.add(dest)
                        }
                    },
                )
            }
            entry<Screen.LicenseInfo> {
                LicenseInfoScreen(
                    onNavigate = { dest ->
                        if (backStack.last() != dest) {
                            backStack.add(dest)
                        }
                    }
                )
            }
            entry<Screen.Setting> {
                SettingScreen(
                    appViewModel = appViewModel,
                    appSettingViewModel = appSettingViewModel,
                    readerSettingViewModel = readerSettingViewModel,
                    onNavigate = { dest ->
                        if (backStack.last() != dest) {
                            backStack.add(dest)
                        }
                    }
                )
            }
            entry<Screen.CsvExport> {
                CsvExportScreen(
                    appViewModel = appViewModel,
                    onNavigate = { dest ->
                        if (backStack.last() != dest) {
                            backStack.add(dest)
                        }
                    }
                )
            }
            entry<Screen.CsvImport> {
                CsvImportScreen(
                    appViewModel = appViewModel,
                    onNavigate = { dest ->
                        if (backStack.last() != dest) {
                            backStack.add(dest)
                        }
                    }
                )
            }
            entry<Screen.Detail> {
                DetailScreen(
                    appViewModel = appViewModel,
                    scanViewModel = scanViewModel,
                    onNavigate = { dest ->
                        if (backStack.last() != dest) {
                            backStack.add(dest)
                        }
                    }
                )
            }
        }
    )
}