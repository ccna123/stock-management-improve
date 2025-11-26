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
import com.example.sol_denka_stockmanagement.screen.inventory.scan.InventoryScanViewModel
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
import com.example.sol_denka_stockmanagement.search.SearchTagsViewModel
import kotlin.collections.listOf

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Navigation3(
    appViewModel: AppViewModel,
    searchTagsViewModel: SearchTagsViewModel,
    appSettingViewModel: AppSettingViewModel,
    readerSettingViewModel: ReaderSettingViewModel,
    scanViewModel: ScanViewModel,
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
            entry<Screen.Shipping> {
                ShippingScreen(
                    appViewModel = appViewModel,
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
                    readerSettingViewModel = readerSettingViewModel,
                    prevScreenNameId = destinationScan.preScreen,
                    onNavigate = { dest -> navigate(dest) },
                    onGoBack = { goBack() }
                )
            }
            entry<Screen.InventoryComplete> {
                InventoryCompleteScreen(
                    appViewModel = appViewModel,
                    scanViewModel = scanViewModel,
                    onGoBack = { goBack() }
                )
            }
            entry<Screen.Receiving> {
                ReceivingScreen(
                    appViewModel = appViewModel,
                    scanViewModel = scanViewModel,
                    onNavigate = {dest -> navigate(dest)},
                    onGoBack = { goBack() }
                )
            }
            entry<Screen.StorageAreaChange> { destinationScan ->
                StorageAreaChangeScreen(
                    appViewModel = appViewModel,
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
                    readerSettingViewModel = readerSettingViewModel,
                    scanViewModel = scanViewModel,
                    searchTagsViewModel = searchTagsViewModel,
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
                    appSettingViewModel = appSettingViewModel,
                    readerSettingViewModel = readerSettingViewModel,
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