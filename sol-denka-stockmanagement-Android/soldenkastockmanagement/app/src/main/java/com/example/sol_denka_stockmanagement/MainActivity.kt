package com.example.sol_denka_stockmanagement

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.example.sol_denka_stockmanagement.navigation.AppNavigation
import com.example.sol_denka_stockmanagement.screen.setting.SettingViewModel
import com.example.sol_denka_stockmanagement.ui.theme.SoldenkastockmanagementTheme
import com.example.sol_denka_stockmanagement.viewmodel.AppViewModel
import com.example.sol_denka_stockmanagement.viewmodel.ScanViewModel
import dagger.hilt.android.AndroidEntryPoint

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val appViewModel: AppViewModel by viewModels()
    private val scanViewModel: ScanViewModel by viewModels()
    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (!hasAllFilesPermission()) {
            showAllFilesPermissionDialog()
            return
        }

        launchAppUI()
    }


    private fun launchAppUI() {
        setContent {
            SoldenkastockmanagementTheme {
                AppNavigation(
                    appViewModel = appViewModel,
                    scanViewModel = scanViewModel,
                    settingViewModel = settingViewModel
                )
            }
        }
    }

    private fun hasAllFilesPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else true
    }

    private fun showAllFilesPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.permission_title))
            .setMessage(getString(R.string.permission_message))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.permission_accept)) { _, _ ->
                openAllFilesSettings()
            }
            .setNegativeButton(getString(R.string.permission_cancel)) { _, _ ->
                // Có thể đóng app hoặc cho vào app nhưng không đọc file được.
                finish()
            }
            .show()
    }

    private fun openAllFilesSettings() {
        try {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = "package:$packageName".toUri()
            startActivity(intent)
        } catch (e: Exception) {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Khi user cấp quyền xong quay lại app
        if (hasAllFilesPermission()) {
            launchAppUI()
        }
    }
}
