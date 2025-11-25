package com.example.sol_denka_stockmanagement.screen.version.sub_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun LicenseInfoScreen(
    onNavigate: (Screen) -> Unit
) {
    Layout(
        topBarText = Screen.LicenseInfo.displayName,
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigate = onNavigate,
        currentScreenNameId = Screen.LicenseInfo.routeId,
        hasBottomBar = false,
        onBackArrowClick = { onNavigate(Screen.VersionInfo) }
    ) { paddingValues ->
        LibrariesContainer(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
        )
    }
}