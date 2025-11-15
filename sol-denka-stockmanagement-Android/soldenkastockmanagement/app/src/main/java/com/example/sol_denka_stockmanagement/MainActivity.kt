package com.example.sol_denka_stockmanagement

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sol_denka_stockmanagement.navigation.Navigation3
import com.example.sol_denka_stockmanagement.screen.detail.DetailViewModel
import com.example.sol_denka_stockmanagement.screen.inventory.scan.InventoryScanViewModel
import com.example.sol_denka_stockmanagement.screen.scan.shipping.ShippingScanViewModel
import com.example.sol_denka_stockmanagement.ui.theme.SoldenkastockmanagementTheme
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.app_setting.AppSettingViewModel
import com.example.sol_denka_stockmanagement.screen.setting.sub_screen.reader_setting.ReaderSettingViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val appViewModel: AppViewModel by viewModels()
    private val appSettingViewModel: AppSettingViewModel by viewModels()
    private val readerSettingViewModel: ReaderSettingViewModel by viewModels()
    private val scanViewModel: ScanViewModel by viewModels()
    private val inventoryScanViewModel: InventoryScanViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private val detailViewModel: DetailViewModel by viewModels()
    private val shippingScanViewModel: ShippingScanViewModel by viewModels()


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
                    inventoryScanViewModel = inventoryScanViewModel,
                    searchViewModel = searchViewModel,
                    detailViewModel = detailViewModel,
                    shippingScanViewModel = shippingScanViewModel
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SoldenkastockmanagementTheme {
        Greeting("Android")
    }
}