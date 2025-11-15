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
import com.example.sol_denka_stockmanagement.screen.detail.DetailViewModel
import com.example.sol_denka_stockmanagement.screen.home.HomeScreen
import com.example.sol_denka_stockmanagement.screen.inventory.complete.InventoryCompleteScreen
import com.example.sol_denka_stockmanagement.screen.inventory.input.InventoryScreen
import com.example.sol_denka_stockmanagement.screen.inventory.scan.InventoryScanScreen
import com.example.sol_denka_stockmanagement.screen.inventory.scan.InventoryScanViewModel
import com.example.sol_denka_stockmanagement.screen.receiving.ReceivingScreen
import com.example.sol_denka_stockmanagement.screen.scan.receiving.ReceivingScanScreen
import com.example.sol_denka_stockmanagement.screen.scan.shipping.ShippingScanScreen
import com.example.sol_denka_stockmanagement.screen.scan.shipping.ShippingScanViewModel
import com.example.sol_denka_stockmanagement.screen.shipping.ShippingScreen
import com.example.sol_denka_stockmanagement.screen.version.VersionInfoScreen
import com.example.sol_denka_stockmanagement.screen.version.sub_screen.LicenseInfoScreen
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting.AppSettingViewModel
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.reader_setting.ReaderSettingViewModel
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.screen.setting.SettingScreen
import com.example.sol_denka_stockmanagement.search.SearchTagsScreen
import com.example.sol_denka_stockmanagement.search.SearchViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Navigation3(
    appViewModel: AppViewModel,
    appSettingViewModel: AppSettingViewModel,
    readerSettingViewModel: ReaderSettingViewModel,
    scanViewModel: ScanViewModel,
    inventoryScanViewModel: InventoryScanViewModel,
    searchViewModel: SearchViewModel,
    detailViewModel: DetailViewModel,
    shippingScanViewModel: ShippingScanViewModel,
) {
    val backStack = remember { mutableStateListOf<Screen>(Screen.Setting) }
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
                    scanViewModel = scanViewModel,
                    shippingScanViewModel = shippingScanViewModel,
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
                    inventoryScanViewModel = inventoryScanViewModel,
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
            entry<Screen.ReceivingScan> { destinationScan ->
                ReceivingScanScreen(
                    scanViewModel = scanViewModel,
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
            entry<Screen.ShippingScan> { destinationScan ->
                ShippingScanScreen(
                    scanViewModel = scanViewModel,
                    appViewModel = appViewModel,
                    shippingScanViewModel = shippingScanViewModel,
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
                    inventoryScanViewModel = inventoryScanViewModel,
                    searchViewModel = searchViewModel,
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
                    inventoryScanViewModel = inventoryScanViewModel,
                    scanViewModel = scanViewModel,
                    detailViewModel = detailViewModel,
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