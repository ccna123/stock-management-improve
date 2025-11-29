package com.example.sol_denka_stockmanagement.helper

import android.content.Context
import com.example.sol_denka_stockmanagement.model.sftp.SftpConfigModel
import org.json.JSONObject

object JsonFileManager {
    fun loadSftpConfig(context: Context): SftpConfigModel {
        return try {
            val jsonStr = context.assets.open("config.json").bufferedReader().use { it.readText() }
            val json = JSONObject(jsonStr)
            SftpConfigModel(
                host = json.getString("host"),
                port = json.getInt("port"),
                userName = json.getString("userName"),
            )
        }catch (e: Exception){
            throw IllegalStateException("Invalid config file: ${e.message}")
        }
    }
}