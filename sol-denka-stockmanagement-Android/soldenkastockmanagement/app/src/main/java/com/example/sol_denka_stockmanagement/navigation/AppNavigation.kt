package com.example.sol_denka_stockmanagement.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.sol_denka_stockmanagement.screen.csv.CsvExportScreen
import com.example.sol_denka_stockmanagement.screen.csv.CsvImportScreen
import com.example.sol_denka_stockmanagement.screen.csv.CsvViewModel
import com.example.sol_denka_stockmanagement.screen.detail.DetailScreen
import com.example.sol_denka_stockmanagement.screen.home.HomeScreen
import com.example.sol_denka_stockmanagement.screen.inventory.complete.InventoryCompleteScreen
import com.example.sol_denka_stockmanagement.screen.inventory.input.InventoryScreen
import com.example.sol_denka_stockmanagement.screen.inventory.scan.InventoryScanScreen
import com.example.sol_denka_stockmanagement.screen.inbound.InboundScreen
import com.example.sol_denka_stockmanagement.screen.inventory.complete.InventoryCompleteViewModel
import com.example.sol_denka_stockmanagement.screen.scan.ScanScreen
import com.example.sol_denka_stockmanagement.screen.outbound.OutboundScreen
import com.example.sol_denka_stockmanagement.screen.version.VersionInfoScreen
import com.example.sol_denka_stockmanagement.screen.version.sub_screen.LicenseInfoScreen
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.screen.setting.SettingScreen
import com.example.sol_denka_stockmanagement.screen.setting.SettingViewModel
import com.example.sol_denka_stockmanagement.screen.location_change.LocationChangeScreen
import com.example.sol_denka_stockmanagement.screen.location_change.LocationChangeViewModel
import com.example.sol_denka_stockmanagement.screen.outbound.OutboundViewModel
import com.example.sol_denka_stockmanagement.search.SearchTagsScreen
import kotlin.collections.listOf

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppNavigation(
    appViewModel: AppViewModel,
    scanViewModel: ScanViewModel,
    settingViewModel: SettingViewModel
) {
    val backStack = remember { mutableStateListOf<Screen>(Screen.Home) }
    val entryDecorators =
        listOf(
            rememberSaveableStateHolderNavEntryDecorator<Screen>(),
            rememberViewModelStoreNavEntryDecorator<Screen>() // Add this line
        )

    fun navigate(dest: Screen) {
        if (backStack.last() != dest) {
            backStack.add(dest)
        }
    }

    fun goBack() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = entryDecorators,
        entryProvider = entryProvider {
            entry<Screen.Home> {
                HomeScreen(
                    appViewModel = appViewModel,
                    scanViewModel = scanViewModel,
                    onNavigate = { dest -> navigate(dest) }
                )
            }
            entry<Screen.Outbound> {
                val outboundViewModel = hiltViewModel<OutboundViewModel>()
                OutboundScreen(
                    appViewModel = appViewModel,
                    outboundViewModel = outboundViewModel,
                    scanViewModel = scanViewModel,
                    onNavigate = {dest -> navigate(dest)},
                    onGoBack = { goBack() }
                )
            }
            entry<Screen.Inventory> {
                InventoryScreen(
                    appViewModel = appViewModel,
                    scanViewModel = scanViewModel,
                    onNavigate = { dest -> navigate(dest) },
                    onGoBack = { goBack() }
                )
            }
            entry<Screen.InventoryScan> { destinationScan ->
                InventoryScanScreen(
                    appViewModel = appViewModel,
                    scanViewModel = scanViewModel,
                    settingViewModel = settingViewModel,
                    prevScreenNameId = destinationScan.preScreen,
                    onNavigate = { dest -> navigate(dest) },
                    onGoBack = { goBack() }
                )
            }
            entry<Screen.InventoryComplete> {
                val inventoryCompleteViewModel = hiltViewModel<InventoryCompleteViewModel>()
                InventoryCompleteScreen(
                    appViewModel = appViewModel,
                    scanViewModel = scanViewModel,
                    inventoryCompleteViewModel = inventoryCompleteViewModel,
                    onNavigate = {dest -> navigate(dest)},
                    onGoBack = { goBack() }
                )
            }
            entry<Screen.Inbound> {
                InboundScreen(
                    appViewModel = appViewModel,
                    scanViewModel = scanViewModel,
                    onNavigate = {dest -> navigate(dest)},
                    onGoBack = { goBack() }
                )
            }
            entry<Screen.LocationChange> { destinationScan ->
                val locationChangeViewModel = hiltViewModel<LocationChangeViewModel>()
                LocationChangeScreen(
                    appViewModel = appViewModel,
                    scanViewModel = scanViewModel,
                    locationChangeViewModel = locationChangeViewModel,
                    onNavigate = {dest -> navigate(dest)},
                    onGoBack = { goBack() }
                )
            }
            entry<Screen.Scan> { destinationScan ->
                ScanScreen(
                    appViewModel = appViewModel,
                    scanViewModel = scanViewModel,
                    prevScreenNameId = destinationScan.preScreen,
                    onNavigate = { dest -> navigate(dest) },
                    onGoBack = { goBack() }
                )
            }
            entry<Screen.SearchTagsScreen> { destinationScan ->
                SearchTagsScreen(
                    appViewModel = appViewModel,
                    prevScreenNameId = destinationScan.preScreen,
                    settingViewModel = settingViewModel,
                    scanViewModel = scanViewModel,
                    onGoBack = { goBack() }
                )
            }
            entry<Screen.VersionInfo> {
                VersionInfoScreen(
                    appViewModel = appViewModel,
                    onNavigate = { dest -> navigate(dest) },
                    onGoBack = { goBack() }
                )
            }
            entry<Screen.LicenseInfo> {
                LicenseInfoScreen(
                    onGoBack = { goBack() }
                )
            }
            entry<Screen.Setting> {
                SettingScreen(
                    appViewModel = appViewModel,
                    settingViewModel = settingViewModel,
                    onGoBack = { goBack() }
                )
            }
            entry<Screen.CsvExport> {
                val csvViewModel = hiltViewModel<CsvViewModel>()
                CsvExportScreen(
                    csvViewModel = csvViewModel,
                    appViewModel = appViewModel,
                    onGoBack = { goBack() }
                )
            }
            entry<Screen.CsvImport> {
                val csvViewModel = hiltViewModel<CsvViewModel>()
                CsvImportScreen(
                    csvViewModel = csvViewModel,
                    appViewModel = appViewModel,
                    onGoBack = { goBack() }
                )
            }
            entry<Screen.Detail> {
                DetailScreen(
                    appViewModel = appViewModel,
                    scanViewModel = scanViewModel,
                    onGoBack = { goBack() }
                )
            }
        }
    )
}