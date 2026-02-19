package com.example.sol_denka_stockmanagement.screen.layout

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.ConnectionState
import com.example.sol_denka_stockmanagement.helper.toast.ToastManager
import com.example.sol_denka_stockmanagement.helper.toast.ToastMessage
import com.example.sol_denka_stockmanagement.intent.ShareIntent
import com.example.sol_denka_stockmanagement.model.reader.ReaderInfoModel
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.share.MenuDrawer
import com.example.sol_denka_stockmanagement.share.dialog.AppDialog
import com.example.sol_denka_stockmanagement.share.dialog.ConfirmDialog
import com.example.sol_denka_stockmanagement.state.DialogState
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.brightGreenPrimary
import com.example.sol_denka_stockmanagement.ui.theme.brightOrange
import com.example.sol_denka_stockmanagement.ui.theme.deepOceanBlue
import com.example.sol_denka_stockmanagement.ui.theme.orange
import com.example.sol_denka_stockmanagement.ui.theme.paleSkyBlue
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Layout(
    modifier: Modifier = Modifier,
    topBarText: String,
    topBarIcon: ImageVector,
    hasBottomBar: Boolean = true,
    appViewModel: AppViewModel? = null,
    onBackArrowClick: (DrawerState) -> Unit,
    onNavigate: ((Screen) -> Unit)? = null,
    bottomButton: (@Composable () -> Unit)? = null,
    topBarButton: (@Composable () -> Unit)? = null,
    retrySaveDb: (suspend () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    // ✅ Get ReaderController via Hilt entry point
    val connectionState by appViewModel?.connectionState?.collectAsStateWithLifecycle()
        ?: remember {
            mutableStateOf(
                ConnectionState.DISCONNECTED
            )
        }
    val readerInfo by appViewModel?.readerInfo?.collectAsState() ?: remember {
        mutableStateOf(
            ReaderInfoModel()
        )
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val focusManager = LocalFocusManager.current
    val showFileProgressBar = appViewModel?.showFileProgressDialog?.value ?: remember {
        mutableStateOf(false)
    }
    val showConnectingDialog =
        appViewModel?.showConnectingDialog?.collectAsStateWithLifecycle()?.value ?: remember {
            mutableStateOf(false)
        }
    val isPerformingInventory by (appViewModel?.isPerformingInventory
        ?: MutableStateFlow(false)).collectAsStateWithLifecycle()

    val dialogState by appViewModel?.dialogState?.collectAsStateWithLifecycle() ?: remember {
        mutableStateOf(
            DialogState.Hidden
        )
    }

    val scope = rememberCoroutineScope()

    when (val d = dialogState) {
        is DialogState.Error -> {
            ConfirmDialog(
                showDialog = true,
                textColor = Color.Red,
                dialogTitle = d.message,
                buttons = listOf(
                    {
                        ButtonContainer(
                            buttonText = stringResource(R.string.close),
                            onClick = {
                                appViewModel?.onGeneralIntent(ShareIntent.HiddenDialog)
                            }
                        )
                    }
                )
            )
        }

        is DialogState.SaveCsvSuccessFailedSftp -> {
            ConfirmDialog(
                showDialog = true,
                textColor = Color.Black,
                dialogTitle = d.message,
                buttons = listOf(
                    {
                        ButtonContainer(
                            buttonText = stringResource(R.string.close),
                            onClick = {
                                appViewModel?.onGeneralIntent(ShareIntent.HiddenDialog)
                                onNavigate?.invoke(Screen.Home)
                            }
                        )
                    }
                )
            )
        }

        is DialogState.Confirm -> {
            ConfirmDialog(
                showDialog = true,
                textColor = Color.Black,
                dialogTitle = d.message,
                buttons = listOf(
                    {
                        ButtonContainer(
                            buttonText = stringResource(R.string.return_home),
                            onClick = {
                                appViewModel?.onGeneralIntent(ShareIntent.HiddenDialog)
                                onNavigate?.invoke(Screen.Home)
                            }
                        )
                    }
                )
            )
        }

        is DialogState.SaveCsvSendSftpSuccess -> {
            ConfirmDialog(
                showDialog = true,
                textColor = Color.Black,
                dialogTitle = d.message,
                buttons = listOf(
                    {
                        ButtonContainer(
                            buttonText = stringResource(R.string.close),
                            onClick = {
                                appViewModel?.onGeneralIntent(ShareIntent.HiddenDialog)
                                onNavigate?.invoke(Screen.Home)
                            }
                        )
                    }
                )
            )
        }

        is DialogState.SaveCsvFailed -> {
            ConfirmDialog(
                showDialog = true,
                textColor = Color.Black,
                dialogTitle = d.message,
                buttons = listOf(
                    {
                        ButtonContainer(
                            buttonText = stringResource(R.string.ok),
                            onClick = {
                                appViewModel?.onGeneralIntent(ShareIntent.HiddenDialog)
                                onNavigate?.invoke(Screen.Home)
                            }
                        )
                    }
                )
            )
        }

        is DialogState.CancelOperation -> {
            ConfirmDialog(
                showDialog = true,
                textColor = Color.Black,
                dialogTitle = d.message,
                buttons = listOf(
                    {
                        ButtonContainer(
                            buttonText = stringResource(R.string.yes),
                            onClick = {
                                appViewModel?.onGeneralIntent(ShareIntent.HiddenDialog)
                                onNavigate?.invoke(Screen.Home)
                            }
                        )
                        ButtonContainer(
                            buttonText = stringResource(R.string.no),
                            containerColor = Color.Red,
                            onClick = {
                                appViewModel?.onGeneralIntent(ShareIntent.HiddenDialog)
                            }
                        )
                    }
                )
            )
        }

        is DialogState.SaveDataToDbFailed -> {
            ConfirmDialog(
                showDialog = true,
                textColor = Color.Black,
                dialogTitle = d.message,
                buttons = listOf(
                    {
                        ButtonContainer(
                            buttonText = stringResource(R.string.retry),
                            onClick = {
                                appViewModel?.onGeneralIntent(ShareIntent.HiddenDialog)
                                scope.launch {
                                    retrySaveDb?.invoke()
                                }
                            }
                        )
                        ButtonContainer(
                            buttonText = stringResource(R.string.cancel),
                            containerColor = Color.Red,
                            onClick = {
                                appViewModel?.onGeneralIntent(ShareIntent.HiddenDialog)
                            }
                        )
                    }
                )
            )
        }

        is DialogState.ExportCsvSuccess -> {
            ConfirmDialog(
                showDialog = true,
                textColor = Color.Black,
                dialogTitle = d.message,
                buttons = listOf(
                    {
                        ButtonContainer(
                            buttonText = stringResource(R.string.close),
                            onClick = {
                                appViewModel?.onGeneralIntent(ShareIntent.HiddenDialog)
                            }
                        )
                    }
                )
            )
        }
        is DialogState.ExportCsvFailed -> {
            ConfirmDialog(
                showDialog = true,
                textColor = Color.Black,
                dialogTitle = d.message,
                buttons = listOf(
                    {
                        ButtonContainer(
                            buttonText = stringResource(R.string.close),
                            onClick = {
                                appViewModel?.onGeneralIntent(ShareIntent.HiddenDialog)
                            }
                        )
                    }
                )
            )
        }
        DialogState.Hidden -> Unit
    }

    if (showConnectingDialog == true) {
        AppDialog {
            Column {
                Text(text = "接続中")
                CircularProgressIndicator()
            }
        }
    }

    LaunchedEffect(Unit) {
        appViewModel?.toastFlow?.collect { (msg, type) ->
            ToastManager.showToast(msg, type)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .height(150.dp)
                            .fillMaxWidth()
                            .background(color = paleSkyBlue)
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable(
                                    indication = LocalIndication.current,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { }
                                .padding(paddingValues = PaddingValues(start = 16.dp))
                                .fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier.size(70.dp),
                                painter = if (connectionState == ConnectionState.DISCONNECTED)
                                    painterResource(R.drawable.reader_icon_off)
                                else
                                    painterResource(R.drawable.reader_icon_rfd40),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            if (connectionState == ConnectionState.CONNECTED) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(text = readerInfo.readerName)
                                        Text(text = readerInfo.firmwareVer)
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Image(
                                            modifier = Modifier.size(35.dp),
                                            painter = painterResource(
                                                when (readerInfo.batteryLevel) {
                                                    in 81..100 -> R.drawable.battery_100
                                                    in 61..80 -> R.drawable.battery_80
                                                    in 41..60 -> R.drawable.battery_60
                                                    in 21..40 -> R.drawable.battery_40
                                                    in 0..20 -> R.drawable.battery_20
                                                    else -> R.drawable.battery_0
                                                }
                                            ),
                                            contentDescription = null
                                        )
                                        Text(
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            text = "${readerInfo.batteryLevel}%"
                                        )
                                    }
                                }
                            } else {
                                Text(
                                    fontSize = 24.sp,
                                    text = stringResource(R.string.no_device_connect)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth()
                            .background(color = paleSkyBlue)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(paddingValues = PaddingValues(start = 16.dp))
                                .fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            Column {
                                Text(
                                    fontSize = 20.sp,
                                    text = stringResource(R.string.drawer_volume)
                                )
                                Text(
                                    fontSize = 20.sp,
                                    text = stringResource(R.string.drawer_radio_power)
                                )
                            }
                            Column {
                                Text(
                                    fontWeight = FontWeight.Bold,
                                    text = if (connectionState == ConnectionState.DISCONNECTED)
                                        "-" else "${readerInfo.buzzerVolume}"
                                )
                                Text(
                                    fontWeight = FontWeight.Bold,
                                    text = if (connectionState == ConnectionState.DISCONNECTED)
                                        "-" else "${readerInfo.radioPower}dbm / ${readerInfo.radioPowerMw}Mw"
                                )
                            }
                        }
                    }
                    LazyColumn {
                        item {
                            // only show search, tire info, setting when screen is home
                            MenuDrawer(
                                menuName = Screen.CsvImport.displayName,
                                icon = R.drawable.file_import,
                                iconColor = brightGreenPrimary,
                                onClick = { onNavigate?.invoke(Screen.CsvImport) }
                            )
                            HorizontalDivider(color = paleSkyBlue)
                            MenuDrawer(
                                menuName = Screen.CsvExport.displayName,
                                icon = R.drawable.file_export,
                                iconColor = brightOrange,
                                onClick = { onNavigate?.invoke(Screen.CsvExport) }
                            )
                            HorizontalDivider(color = paleSkyBlue)
                            MenuDrawer(
                                menuName = Screen.Setting.displayName,
                                icon = R.drawable.setting,
                                iconColor = Color.Black,
                                onClick = { onNavigate?.invoke(Screen.Setting) }
                            )
                            HorizontalDivider(color = paleSkyBlue)
                            MenuDrawer(
                                menuName = Screen.VersionInfo.displayName,
                                icon = R.drawable.app_version_info,
                                iconColor = deepOceanBlue,
                                onClick = { onNavigate?.invoke(Screen.VersionInfo) }
                            )
                            HorizontalDivider(color = paleSkyBlue)
                        }
                    }
                }
            }
        },
        content = {
            Scaffold(
                modifier = modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    fontSize = 20.sp,
                                    color = Color.White,
                                    text = topBarText
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    topBarButton?.invoke()
                                }
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { onBackArrowClick(drawerState) }) {
                                Icon(
                                    tint = Color.White,
                                    imageVector = topBarIcon,
                                    contentDescription = null
                                )
                                focusManager.clearFocus()
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = if (isPerformingInventory) orange else brightAzure
                        ),
                    )
                },
                bottomBar = {
                    if (hasBottomBar) {
                        BottomAppBar(
                            containerColor = Color.Black.copy(alpha = 0.3f),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                bottomButton?.invoke()
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .consumeWindowInsets(innerPadding)
                        .imePadding()
                ) {
                    content(innerPadding)
                }
                AnimatedVisibility(
                    visible = ToastManager.showMessage.collectAsState().value,
                    enter = slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight }, // Start from bottom
                        animationSpec = tween(durationMillis = 400)
                    ) + fadeIn(animationSpec = tween(durationMillis = 400)),
                    exit = slideOutVertically(
                        targetOffsetY = { fullHeight -> fullHeight }, // Slide back down
                        animationSpec = tween(durationMillis = 400)
                    ) + fadeOut(animationSpec = tween(durationMillis = 400))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        ToastMessage(
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(innerPadding)
                                .padding(bottom = 16.dp)
                                .zIndex(100f)
                        )
                    }
                }
            }
        }
    )
}