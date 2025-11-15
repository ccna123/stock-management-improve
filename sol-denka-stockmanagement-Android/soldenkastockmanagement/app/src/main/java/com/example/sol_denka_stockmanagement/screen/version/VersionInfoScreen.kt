package com.example.sol_denka_stockmanagement.screen.version

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.ConnectionState
import com.example.sol_denka_stockmanagement.navigation.Screen
import com.example.sol_denka_stockmanagement.model.ReaderInfoModel
import com.example.sol_denka_stockmanagement.screen.layout.Layout
import com.example.sol_denka_stockmanagement.share.ButtonContainer
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun VersionInfoScreen(
    appViewModel: AppViewModel,
    onNavigate: (Screen) -> Unit
) {
    val readerInfo by appViewModel.readerInfo.collectAsStateWithLifecycle()
    Layout(
        topBarText = Screen.VersionInfo.displayName,
        topBarIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigate = onNavigate,
        currentScreenNameId = Screen.VersionInfo.routeId,
        hasBottomBar = false,
        onBackArrowClick = { onNavigate((Screen.Home)) }
    ) { paddingValues ->
        VersionInfoScreenContent(
            modifier = Modifier.padding(paddingValues),
            onNavigate = onNavigate,
            readerInfo = readerInfo
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun VersionInfoScreenContent(
    modifier: Modifier = Modifier,
    readerInfo: ReaderInfoModel,
    onNavigate: (Screen) -> Unit
) {

    val context = LocalContext.current
    val versionName = try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName ?: "Unknown"
    } catch (_: PackageManager.NameNotFoundException) {
        "Unknown"
    }

    LazyColumn(
        modifier = modifier.padding(PaddingValues(horizontal = 16.dp))
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues = PaddingValues(top = 20.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painterResource(R.drawable.app_info), contentDescription = null,
                    modifier = Modifier.size(90.dp)
                )
                Column {
                    Text(
                        fontSize = 15.sp,
                        text = stringResource(R.string.version_app_name)
                    )
                    Text(
                        fontSize = 15.sp,
                        text = stringResource(R.string.version_verinfo, versionName)
                    )
                    Text(
                        fontSize = 15.sp,
                        text = stringResource(R.string.version_copyright)
                    )
                }

            }
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider()
            Text(text = stringResource(R.string.version_device_title))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(30.dp),
            ) {
                Column {
                    Text(text = stringResource(R.string.version_device_name))
                    Text(text = stringResource(R.string.version_device_android))
                    Text(text = stringResource(R.string.version_device_id))
                }
                Column {
                    Text(text = Build.MODEL)
                    Text(text = Build.VERSION.RELEASE)
                    Text(text = Build.ID)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider()
            if (readerInfo.connectionState == ConnectionState.CONNECTED) {
                Text(text = stringResource(R.string.version_rfid_title))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = stringResource(R.string.version_rfid_name))
                    Text(text = readerInfo.readerName)
                }
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider()
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = stringResource(R.string.version_lib_title))
                ButtonContainer(
                    modifier = Modifier.fillMaxWidth(),
                    buttonHeight = 35.dp,
                    shape = RoundedCornerShape(10.dp),
                    buttonText = stringResource(R.string.version_license_info),
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.license),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    onClick = {
                        onNavigate(Screen.LicenseInfo)
                    }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(10.dp))
            Column {
                Text(
                    color = brightAzure,
                    text = stringResource(R.string.version_company_title)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    fontWeight = FontWeight.Bold,
                    text = stringResource(R.string.version_company_name)
                )
                Text(
                    fontWeight = FontWeight.Bold,
                    text = stringResource(R.string.version_division_name)
                )
                Spacer(modifier = Modifier.height(10.dp))
                ClickableUrlText()
            }
        }
    }
}

@Composable
fun ClickableUrlText(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val url = stringResource(R.string.version_company_url) // e.g., "https://example.com"

    // Create an AnnotatedString with the URL text
    val annotatedString = buildAnnotatedString {
        append(url)
        // Apply styling to the entire string
        addStyle(
            style = SpanStyle(
                color = brightAzure,
                textDecoration = TextDecoration.Underline
            ),
            start = 0,
            end = url.length
        )
        // Add a tag to make it clickable
        addStringAnnotation(
            tag = "URL",
            annotation = url,
            start = 0,
            end = url.length
        )
    }

    ClickableText(
        text = annotatedString,
        style = TextStyle(fontSize = 16.sp), // Adjust font size as needed
        modifier = modifier,
        onClick = { offset ->
            // Find the clicked annotation
            annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    // Launch the URL in a browser
                    val intent = Intent(Intent.ACTION_VIEW, annotation.item.toUri())
                    context.startActivity(intent)
                }
        }
    )
}