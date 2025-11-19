package com.example.sol_denka_stockmanagement.screen.home

import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.model.MenuModel
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.AppDialog
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.ui.theme.deepOceanBlue
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomeScreen(
    appViewModel: AppViewModel,
    scanViewModel: ScanViewModel,
    onNavigate: (Screen) -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        showExitDialog = true
    }

    LaunchedEffect(Unit) {
        appViewModel.onGeneralIntent(ShareIntent.ResetState)
        scanViewModel.apply {
            clearScannedTag()
            clearProcessedTag()
        }
    }

    if (showExitDialog) {
        AppDialog{
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.app_exit_confirm),
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ButtonContainer(
                        buttonText = stringResource(R.string.ok),
                        onClick = {
                            showExitDialog = false
                            (context as? Activity)?.finish()
                        }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    ButtonContainer(
                        buttonText = stringResource(R.string.close),
                        containerColor = Color.Red,
                        onClick = {
                            showExitDialog = false
                        }
                    )
                }
            }
        }
    }

    Layout(
        topBarText = Screen.Home.displayName,
        topBarIcon = Icons.Default.Menu,
        currentScreenNameId = Screen.Home.routeId,
        hasBottomBar = false,
        onNavigate = onNavigate,
        appViewModel = appViewModel,
        onBackArrowClick = { drawerState: DrawerState ->
            scope.launch { drawerState.open() }
        }) { paddingValues ->
        HomeScreenContent(
            modifier = Modifier.padding(paddingValues),
            onNavigate = onNavigate,
        )
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier,
    onNavigate: (Screen) -> Unit,
) {
    val menuItems = listOf(
        MenuModel(screen = Screen.Receiving, icon = R.drawable.receiving),
        MenuModel(screen = Screen.Shipping, icon = R.drawable.shipping),
        MenuModel(screen = Screen.StorageAreaChange, icon = R.drawable.warehouse),
        MenuModel(screen = Screen.Inventory, icon = R.drawable.inventory),
    )
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            menuItems.forEach { menu ->
                Box(
                    modifier = Modifier
                        .padding(paddingValues = PaddingValues(top = 14.dp))
                        .height(105.dp)
                        .width(280.dp)
                        .background(
                            color = deepOceanBlue, shape = RoundedCornerShape(12.dp)
                        )
                        .clickable(
                            indication = LocalIndication.current,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            when (menu.screen) {
                                Screen.Receiving -> onNavigate(Screen.ReceivingScan)
                                Screen.Shipping -> onNavigate(Screen.Scan(Screen.Shipping.routeId))
                                Screen.StorageAreaChange -> onNavigate(Screen.Scan(Screen.StorageAreaChange.routeId))
                                Screen.Inventory -> onNavigate(Screen.Inventory)
                                else -> error("No route")
                            }
                        }, contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = menu.icon),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(45.dp)
                        )
                        Text(
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(16.dp),
                            color = Color.White,
                            fontSize = 30.sp,
                            text = menu.screen.displayName
                        )
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}