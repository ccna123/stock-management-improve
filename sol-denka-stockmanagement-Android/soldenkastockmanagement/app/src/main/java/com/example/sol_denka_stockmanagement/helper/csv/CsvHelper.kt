package com.example.sol_denka_stockmanagement.helper.csv

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.example.sol_denka_stockmanagement.app_interface.ICsvExport
import com.example.sol_denka_stockmanagement.constant.CsvType
import com.example.sol_denka_stockmanagement.constant.StatusCode
import com.example.sol_denka_stockmanagement.database.repository.item.ItemTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.ledger.LedgerItemRepository
import com.example.sol_denka_stockmanagement.database.repository.location.LocationMasterRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.database.repository.field.ItemTypeFieldSettingMasterRepository
import com.example.sol_denka_stockmanagement.constant.ProcessResult
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.model.csv.CsvFileInfoModel
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import com.jcraft.jsch.SftpProgressMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale
import java.util.Vector
import javax.inject.Inject

class CsvHelper @Inject constructor(
    private val locationMasterRepository: LocationMasterRepository,
    private val ledgerItemRepository: LedgerItemRepository,
    private val itemTypeRepository: ItemTypeRepository,
    private val tagMasterRepository: TagMasterRepository,
    private val itemTypeFieldSettingMasterRepository: ItemTypeFieldSettingMasterRepository,
    private val db: AppDatabase
) {
    companion object {
        private const val ROOT_FOLDER = "DenkaStockManagement"
        private const val EXPORT = "Export"
        private const val IMPORT = "Import"

        private const val INVENTORY_RESULT = "InventoryResult"
        private const val INBOUND_EVENT = "InboundEvent"
        private const val OUTBOUND_EVENT = "OutboundEvent"
        private const val LOCATION_CHANGE_EVENT = "LocationChangeEvent"

        private const val LEDGER_MASTER = "LedgerMaster"
        private const val LOCATION_MASTER = "LocationMaster"
        private const val ITEM_TYPE_MASTER = "ItemTypeMaster"
        private const val TAG_MASTER = "TagMaster"
        private const val ITEM_TYPE_FIELD_SETTING_MASTER = "ItemTypeFieldSettingMaster"
    }

    suspend fun createAppFolders(context: Context) = withContext(Dispatchers.IO) {
        try {
            // ✅ Scoped storage: create using MediaStore under Documents/
            val resolver = context.contentResolver
            val externalUri = MediaStore.Files.getContentUri("external")

            fun ensureScopedFolder(relative: String) {
                val dummy = ContentValues().apply {
                    put(
                        MediaStore.MediaColumns.DISPLAY_NAME,
                        ".folder_marker_${System.currentTimeMillis()}"
                    )
                    put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
                    // use a *known* base directory
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_DOWNLOADS + "/" + relative
                    )
                }
                try {
                    val uri = resolver.insert(externalUri, dummy)
                    if (uri != null) {
                        resolver.delete(uri, null, null)
                    } else {
                        Log.e("TSS", "Insert returned null for $relative")
                    }
                } catch (e: Exception) {
                    Log.e("TSS", "Error ensuring folder $relative: ${e.message}")
                }
            }
            // Root
            ensureScopedFolder(ROOT_FOLDER)

            // Export folders
            ensureScopedFolder("$ROOT_FOLDER/$EXPORT")
            ensureScopedFolder("$ROOT_FOLDER/$EXPORT/$INVENTORY_RESULT")
            ensureScopedFolder("$ROOT_FOLDER/$EXPORT/$LOCATION_CHANGE_EVENT")
            ensureScopedFolder("$ROOT_FOLDER/$EXPORT/$INBOUND_EVENT")
            ensureScopedFolder("$ROOT_FOLDER/$EXPORT/$OUTBOUND_EVENT")

            // Import folders
            ensureScopedFolder("$ROOT_FOLDER/$IMPORT")
            ensureScopedFolder("$ROOT_FOLDER/$IMPORT/$LEDGER_MASTER")
            ensureScopedFolder("$ROOT_FOLDER/$IMPORT/$LOCATION_MASTER")
            ensureScopedFolder("$ROOT_FOLDER/$IMPORT/$ITEM_TYPE_MASTER")
            ensureScopedFolder("$ROOT_FOLDER/$IMPORT/$TAG_MASTER")
            ensureScopedFolder("$ROOT_FOLDER/$IMPORT/$ITEM_TYPE_FIELD_SETTING_MASTER")
            Log.i("TSS", "✅ Folder structure ensured")
        } catch (e: Exception) {
            Log.e("TSS", "Error creating folders: ${e.message}", e)
        }
    }

    private fun mapCsvTypeToFolders(csvType: String): Pair<String, String>? {
        return when (csvType) {

            CsvType.LedgerMaster.displayName -> Pair(
                "Import/LedgerMaster",
                "$IMPORT/$LEDGER_MASTER"
            )

            CsvType.LocationMaster.displayName -> Pair(
                "Import/LocationMaster",
                "$IMPORT/$LOCATION_MASTER"
            )

            CsvType.ItemTypeMaster.displayName -> Pair(
                "Import/ItemTypeMaster",
                "$IMPORT/$ITEM_TYPE_MASTER"
            )

            CsvType.TagMaster.displayName -> Pair(
                "Import/TagMaster",
                "$IMPORT/$TAG_MASTER"
            )

            CsvType.ItemTypeFieldSettingMaster.displayName -> Pair(
                "Import/ItemTypeFieldSettingMaster",
                "$IMPORT/$ITEM_TYPE_FIELD_SETTING_MASTER"
            )

            CsvType.InventoryResult.displayName -> Pair(
                "Export/InventoryResult",
                "$EXPORT/$INVENTORY_RESULT"
            )

            CsvType.LocationChangeResult.displayName -> Pair(
                "Export/LocationChange",
                "$EXPORT/$LOCATION_CHANGE_EVENT"
            )

            CsvType.OutboundResult.displayName -> Pair(
                "Export/OutboundEvent",
                "$EXPORT/$OUTBOUND_EVENT"
            )

            CsvType.InboundResult.displayName -> Pair(
                "Export/InboundEvent",
                "$EXPORT/$INBOUND_EVENT"
            )

            else -> null
        }
    }

    suspend fun listCsvFiles(csvType: String): List<CsvFileInfoModel> =
        withContext(Dispatchers.IO) {

            // 1) CHECK PERMISSION
            if (!hasAllFilesAccess()) {
                Log.e("TSS", "❌ MANAGE_EXTERNAL_STORAGE missing! Cannot list files.")
                return@withContext emptyList()
            }

            val result = mutableListOf<CsvFileInfoModel>()

            try {
                val (_, localFolder) = mapCsvTypeToFolders(csvType)
                    ?: return@withContext emptyList()

                val targetDir = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "$ROOT_FOLDER/$localFolder"
                )

                Log.w("TSS", "📁 Reading folder (File API FULL ACCESS): ${targetDir.absolutePath}")

                if (!targetDir.exists()) {
                    Log.w("TSS", "📁 Folder NOT exists → creating...")
                    targetDir.mkdirs()
                }

                // 2) LIST FILES DIRECTLY FROM DISK
                val files = targetDir.listFiles()?.filter {
                    it.isFile && it.name.lowercase().endsWith(".csv")
                } ?: emptyList()

                Log.w(
                    "TSS",
                    "📄 FOUND ${files.size} file(s): ${files.joinToString { it.name }}"
                )

                // 3) FORMAT RESULT
                result.addAll(
                    files.map { file ->
                        CsvFileInfoModel(
                            fileName = file.name,
                            fileSize = formatSize(file.length()),
                            filePath = targetDir.absolutePath
                        )
                    }
                )

            } catch (e: Exception) {
                Log.e("TSS", "❌ listCsvFiles ERROR: ${e.message}")
            }

            result
        }

    // ---------------------------------------------
    // FORMAT SIZE (Readable)
    // ---------------------------------------------
    private fun formatSize(sizeBytes: Long): String {
        val size = sizeBytes.toDouble()
        return when {
            size >= 1000 * 1000 -> String.format(Locale.US, "%.1f MB", size / (1024 * 1024))
            size >= 1000 -> String.format(Locale.US, "%.1f KB", size / 1024)
            else -> "$sizeBytes B"
        }
    }

    suspend fun exportAllFilesIndividually(
        context: Context,
        files: List<CsvFileInfoModel>,
        isInventoryResult: Boolean,
        onProgressUpdate: (Int, Float) -> Unit,
        onFileComplete: (Int) -> Unit,
        onFileError: (Int) -> Unit,
    ) = withContext(Dispatchers.IO) {
        if (files.isEmpty()) return@withContext
        supervisorScope {
            files.mapIndexed { index, file ->
                async {
                    try {

                        val sourceFile = File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                            "StockManagementApp/Export/${if (isInventoryResult) "InventoryResultLocalRepository" else "StockEvent"}/${file.fileName}"
                        )

                        if (!sourceFile.exists()) {
                            Log.e("TSS", "❌ File not found: ${sourceFile.absolutePath}")
                            onFileError(index)
                            return@async
                        }

                        val totalBytes = sourceFile.length().takeIf { it > 0 } ?: 1L
                        var copiedBytes = 0L

                        val exportDir = File(context.filesDir, "exported_csv")
                        if (!exportDir.exists()) exportDir.mkdirs()
                        val targetFile = File(exportDir, file.fileName)

                        sourceFile.inputStream().use { input ->
                            targetFile.outputStream().use { output ->
                                val buffer = ByteArray(4096)
                                var bytesRead: Int
                                while (input.read(buffer).also { bytesRead = it } != -1) {
                                    output.write(buffer, 0, bytesRead)
                                    copiedBytes += bytesRead

                                    val progress =
                                        (copiedBytes.toFloat() / totalBytes).coerceIn(0f, 1f)
                                    onProgressUpdate(index, progress)
                                }
                            }
                        }

                        Log.i("TSS", "✅ Exported successfully: ${file.fileName}")
                        onFileComplete(index)

                    } catch (e: Exception) {
                        Log.e("TSS", "❌ Failed: ${file.fileName} (${e.message})")

                        val failedFile = File(context.filesDir, "exported_csv/${file.fileName}")
                        if (failedFile.exists()) {
                            failedFile.delete()
                            Log.w("TSS", "🗑️ Deleted incomplete file: ${failedFile.name}")
                        }

                        onFileError(index)
                    }
                }
            }.awaitAll()
        }
    }

    suspend fun downloadCsvFromSftp(
        context: Context,
        host: String,
        port: Int = 22,
        username: String,
        csvType: String,
        onProgress: (Float) -> Unit,
        onComplete: () -> Unit,
    ): ProcessResult = withContext(Dispatchers.IO) {
        try {
            val (remoteFolder, localFolder) = mapCsvTypeToFolders(csvType)
                ?: return@withContext ProcessResult.Failure(
                    statusCode = StatusCode.FOLDER_NOT_FOUND,
                )

            // Copy from assets to internal storage so JSch can read it
            val privateKeyFile = getPrivateKeyAndKnownHosts(context).first
            val knownHosts = getPrivateKeyAndKnownHosts(context).second

            val jsch = JSch()
            jsch.apply {
                addIdentity(privateKeyFile.absolutePath, "")
                setKnownHosts(knownHosts.absolutePath)
            }

            val session: Session = jsch.getSession(username, host, port)
            session.connect()

            val channel = session.openChannel("sftp") as ChannelSftp
            channel.connect()

            // ✅ Use forward-slash SFTP-style path
            val remoteDir = "/C:/$ROOT_FOLDER/$remoteFolder"
            val files = channel.ls(remoteDir) as Vector<ChannelSftp.LsEntry>
            val csvFiles = files.filter { it.filename.endsWith(".csv", true) }

            val localBaseDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "$ROOT_FOLDER/$localFolder"
            )
            if (!localBaseDir.exists()) localBaseDir.mkdirs()

            csvFiles.forEach { entry ->
                val remoteFile = "$remoteDir/${entry.filename}"
                val localFile = File(localBaseDir, entry.filename)

                val attrs = channel.lstat(remoteFile)
                val totalBytes = attrs.size.takeIf { it > 0 } ?: 1L

                val monitor = object : SftpProgressMonitor {
                    private var transferredBytes: Long = 0L
                    private var lastProgress = 0.0

                    override fun init(op: Int, src: String?, dest: String?, max: Long) {
                        transferredBytes = 0L
                        lastProgress = 0.0
                    }

                    override fun count(count: Long): Boolean {
                        transferredBytes += count
                        val progress = (transferredBytes.toDouble() / totalBytes).coerceIn(0.0, 1.0)

                        if (progress > lastProgress) {
                            lastProgress = progress
                            onProgress(progress.toFloat())
                        }
                        return true
                    }

                    override fun end() {
                        onProgress(1f)
                        Log.i("TSS", "✅ Download completed: ${entry.filename}")
                    }
                }
                channel.get(remoteFile, localFile.outputStream(), monitor)
                Log.i("TSS", "✅ Downloaded ${entry.filename} to ${localFile.absolutePath}")
            }
            channel.disconnect()
            session.disconnect()
            onComplete()
            ProcessResult.Success(statusCode = StatusCode.DOWNLOAD_SFTP_OK)
        } catch (e: Exception) {
            Log.e("TSS", "downloadCsvFromSftp: $e")
            ProcessResult.Failure(
                statusCode = StatusCode.FAILED,
            )
        }
    }

    private fun getPrivateKeyAndKnownHosts(context: Context): Pair<File, File> {
        val privateKeyFile = File(context.filesDir, "android_sftp")
        if (!privateKeyFile.exists()) {
            context.assets.open("android_sftp").use { input ->
                privateKeyFile.outputStream().use { output -> input.copyTo(output) }
            }
        }
        val knownHosts = File(context.filesDir, "known_hosts")
        if (!knownHosts.exists()) {
            context.assets.open("known_hosts").use { input ->
                knownHosts.outputStream().use { output -> input.copyTo(output) }
            }
        }
        return Pair(privateKeyFile, knownHosts)
    }

    private fun hasAllFilesAccess(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else true
    }

    suspend fun import(
        csvType: String,
        fileName: String,
        onProgress: (Float) -> Unit
    ): ProcessResult = withContext(Dispatchers.IO) {
        try {
            val files = listCsvFiles(csvType)
            if (files.isEmpty()) {
                return@withContext ProcessResult.Failure(
                    statusCode = StatusCode.FILE_NOT_FOUND,
                )
            }

            val fileInfo = files.find { it.fileName == fileName }
                ?: return@withContext ProcessResult.Failure(
                    statusCode = StatusCode.FILE_NOT_FOUND,
                )

            val fileObj = File(fileInfo.filePath, fileInfo.fileName)
            if (!fileObj.exists()) {
                return@withContext ProcessResult.Failure(
                    statusCode = StatusCode.FILE_NOT_FOUND,
                )
            }

            val lines = fileObj.readLines(Charsets.UTF_8)
            if (lines.isEmpty()) {
                return@withContext ProcessResult.Failure(
                    statusCode = StatusCode.FILE_EMPTY,
                )
            }

            val importer = getImporter(csvType)
                ?: return@withContext ProcessResult.Failure(
                    statusCode = StatusCode.CSV_IMPORTER_NOT_FOUND,
                )

            val total = maxOf(1, lines.size)
            var count = 0

            val headerLine = lines.first()
            val headers = headerLine.split(",").map { it.trim() }

            val missing = importer.requiredHeaders - headers.toSet()
            if (missing.isNotEmpty()) {
                return@withContext ProcessResult.Failure(
                    statusCode = StatusCode.MISSING_COLUMN,
                    params = mapOf(
                        "missing_headers" to missing.toList()
                    )
                )
            }

            lines.drop(1).chunked(1).forEach { chunk ->
                importer.importAll(headers = headers, lines = chunk)
                count += chunk.size

                val progress = (count.toFloat() / total).coerceIn(0f, 1f)
                onProgress(progress)
            }
            importer.finish()
            onProgress(1f)
            ProcessResult.Success(statusCode = StatusCode.IMPORT_OK)

        } catch (e: Exception) {
            ProcessResult.Failure(
                statusCode = StatusCode.FAILED,
                rawMessage = e.message ?: "Unknown error"
            )
        }
    }


    private fun getImporter(csvType: String): CsvImporter<*>? {
        return when (csvType) {
            CsvType.LocationMaster.displayName -> LocationMasterImporter(repository = locationMasterRepository, db = db)
            CsvType.LedgerMaster.displayName -> LedgerItemMasterImporter(repository = ledgerItemRepository, db = db)
            CsvType.ItemTypeMaster.displayName -> ItemTypeMasterImporter(repository = itemTypeRepository, db = db)
            CsvType.TagMaster.displayName -> TagMasterImporter(repository = tagMasterRepository, db = db)
            CsvType.ItemTypeFieldSettingMaster.displayName -> ItemTypeFieldSettingMasterImporter(repository = itemTypeFieldSettingMasterRepository, db = db)
            else -> null
        }
    }


    suspend fun <T : ICsvExport> saveCsv(
        context: Context,
        csvType: String,
        fileName: String,
        rows: List<T>,
        onProgress: (Float) -> Unit,
    ): ProcessResult = withContext(Dispatchers.IO) {
        try {
            if (rows.isEmpty()) {
                return@withContext ProcessResult.Failure(
                    statusCode = StatusCode.EMPTY_DATA,
                )
            }

            val resolver = context.contentResolver
            val externalUri = MediaStore.Files.getContentUri("external")

            // map csvType -> local folder (Export/InventoryResult, Export/StockEvent, ...)
            val (_, localFolder) = mapCsvTypeToFolders(csvType)
                ?: return@withContext ProcessResult.Failure(
                    statusCode = StatusCode.FOLDER_NOT_FOUND,
                )

            val relativePath =
                Environment.DIRECTORY_DOWNLOADS + "/$ROOT_FOLDER/$localFolder/"

            // Remove file that has same name
            resolver.delete(
                externalUri,
                "${MediaStore.MediaColumns.RELATIVE_PATH}=? AND ${MediaStore.MediaColumns.DISPLAY_NAME}=?",
                arrayOf(relativePath, fileName)
            )

            // Get header from first row
            val headerLine = rows.first().toHeader().joinToString(",")

            // Build data
            val dataLines = rows.joinToString("\n") { it.toRow().joinToString(",") }

            // Content assemble
            val fullContent = buildString {
                append(headerLine)
                append("\r\n")
                append(dataLines)
                append("\r\n")
            }

            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
            }

            val uri = resolver.insert(externalUri, values)
                ?: return@withContext ProcessResult.Failure(
                    statusCode = StatusCode.FILE_CREATED_FAILED,
                )

            val bytes = fullContent.toByteArray()
            val totalBytes = bytes.size.takeIf { it > 0 } ?: 1
            var written = 0

            resolver.openOutputStream(uri)?.use { output ->
                val bufferSize = 4096
                var offset = 0

                while (offset < bytes.size) {
                    val count = minOf(bufferSize, bytes.size - offset)
                    output.write(bytes, offset, count)
                    offset += count
                    written += count

                    val progress = (written.toFloat() / totalBytes).coerceIn(0f, 1f)
                    onProgress(progress)
                }
                output.flush()
            } ?: return@withContext ProcessResult.Failure(
                statusCode = StatusCode.WRITE_ERROR,
            )

            Log.i("TSS", "✅ CSV saved: $relativePath$fileName")
            onProgress(1f)

            ProcessResult.Success(statusCode = StatusCode.OK)

        } catch (e: Exception) {
            Log.e("TSS", "❌ saveCsv error: ${e.message}", e)
            ProcessResult.Failure(
                statusCode = StatusCode.FAILED,
                rawMessage = e.message ?: "Unknown"
            )
        }
    }
}