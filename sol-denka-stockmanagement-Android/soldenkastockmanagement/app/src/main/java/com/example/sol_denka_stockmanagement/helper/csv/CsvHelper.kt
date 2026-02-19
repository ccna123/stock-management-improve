package com.example.sol_denka_stockmanagement.helper.csv

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.app_interface.ICsvExport
import com.example.sol_denka_stockmanagement.constant.CsvType
import com.example.sol_denka_stockmanagement.constant.ProcessResult
import com.example.sol_denka_stockmanagement.constant.StatusCode
import com.example.sol_denka_stockmanagement.constant.formatTimestamp
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.database.repository.csv.CsvTaskTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.field.FieldMasterRepository
import com.example.sol_denka_stockmanagement.database.repository.field.ItemTypeFieldSettingMasterRepository
import com.example.sol_denka_stockmanagement.database.repository.inbound.InboundSessionRepository
import com.example.sol_denka_stockmanagement.database.repository.inventory.InventoryResultTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.inventory.InventorySessionRepository
import com.example.sol_denka_stockmanagement.database.repository.item.ItemCategoryRepository
import com.example.sol_denka_stockmanagement.database.repository.item.ItemTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.item.ItemUnitRepository
import com.example.sol_denka_stockmanagement.database.repository.ledger.LedgerItemRepository
import com.example.sol_denka_stockmanagement.database.repository.location.LocationChangeSessionRepository
import com.example.sol_denka_stockmanagement.database.repository.location.LocationMasterRepository
import com.example.sol_denka_stockmanagement.database.repository.outbound.OutboundSessionRepository
import com.example.sol_denka_stockmanagement.database.repository.process.ProcessTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.database.repository.tag.TagStatusMasterRepository
import com.example.sol_denka_stockmanagement.database.repository.winder.WinderRepository
import com.example.sol_denka_stockmanagement.exception.AppException
import com.example.sol_denka_stockmanagement.exception.CsvFileCreateException
import com.example.sol_denka_stockmanagement.exception.CsvFileEmptyException
import com.example.sol_denka_stockmanagement.exception.CsvFileNotFoundException
import com.example.sol_denka_stockmanagement.exception.CsvImportFailedException
import com.example.sol_denka_stockmanagement.exception.CsvImporterNotFoundException
import com.example.sol_denka_stockmanagement.exception.CsvWriteException
import com.example.sol_denka_stockmanagement.exception.FolderNotFoundException
import com.example.sol_denka_stockmanagement.exception.MissingColumnException
import com.example.sol_denka_stockmanagement.exception.MissingHeaderException
import com.example.sol_denka_stockmanagement.exception.ReferenceMasterMissingFileException
import com.example.sol_denka_stockmanagement.exception.SqliteConstraintAppException
import com.example.sol_denka_stockmanagement.model.csv.CsvFileInfoModel
import com.example.sol_denka_stockmanagement.model.session.SessionModel
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
import java.util.zip.ZipInputStream
import javax.inject.Inject
import kotlin.reflect.KClass

class CsvHelper @Inject constructor(
    private val locationMasterRepository: LocationMasterRepository,
    private val ledgerItemRepository: LedgerItemRepository,
    private val itemTypeRepository: ItemTypeRepository,
    private val tagMasterRepository: TagMasterRepository,
    private val itemTypeFieldSettingMasterRepository: ItemTypeFieldSettingMasterRepository,
    private val processTypeRepository: ProcessTypeRepository,
    private val tagStatusMasterRepository: TagStatusMasterRepository,
    private val winderRepository: WinderRepository,
    private val fieldMasterRepository: FieldMasterRepository,
    private val inventoryResultTypeRepository: InventoryResultTypeRepository,
    private val itemUnitRepository: ItemUnitRepository,
    private val csvTaskTypeRepository: CsvTaskTypeRepository,
    private val itemCategoryRepository: ItemCategoryRepository,
    private val inboundSessionRepository: InboundSessionRepository,
    private val outboundSessionRepository: OutboundSessionRepository,
    private val locationChangeSessionRepository: LocationChangeSessionRepository,
    private val inventorySessionRepository: InventorySessionRepository,
    private val db: AppDatabase
) {
    companion object {
        private const val ROOT_FOLDER = "DenkaStockManagement"
        private const val EXPORT = "Export"
        private const val IMPORT = "Import"

        private const val INVENTORY_RESULT = "Inventory"
        private const val INBOUND = "Inbound"
        private const val OUTBOUND = "Outbound"
        private const val LOCATION_CHANGE_EVENT = "LocationChange"

        private const val LEDGER_MASTER = "LedgerItemMaster"
        private const val LOCATION_MASTER = "LocationMaster"
        private const val ITEM_TYPE_MASTER = "ItemTypeMaster"
        private const val TAG_MASTER = "TagMaster"
        private const val ITEM_TYPE_FIELD_SETTING_MASTER = "ItemTypeFieldSettingMaster"
        private const val REFERENCE_MASTER = "ReferenceMaster"
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
            ensureScopedFolder("$ROOT_FOLDER/$EXPORT/$INBOUND")
            ensureScopedFolder("$ROOT_FOLDER/$EXPORT/$OUTBOUND")

            // Import folders
            ensureScopedFolder("$ROOT_FOLDER/$IMPORT")
            ensureScopedFolder("$ROOT_FOLDER/$IMPORT/$LEDGER_MASTER")
            ensureScopedFolder("$ROOT_FOLDER/$IMPORT/$LOCATION_MASTER")
            ensureScopedFolder("$ROOT_FOLDER/$IMPORT/$ITEM_TYPE_MASTER")
            ensureScopedFolder("$ROOT_FOLDER/$IMPORT/$TAG_MASTER")
            ensureScopedFolder("$ROOT_FOLDER/$IMPORT/$ITEM_TYPE_FIELD_SETTING_MASTER")
            ensureScopedFolder("$ROOT_FOLDER/$IMPORT/$REFERENCE_MASTER")
            Log.i("TSS", "✅ Folder structure ensured")
        } catch (e: Exception) {
            Log.e("TSS", "Error creating folders: ${e.message}", e)
        }
    }

    private fun mapCsvTypeToFolders(csvType: String): Pair<String, String>? {
        return when (csvType) {

            CsvType.LedgerMaster.displayNameJp -> Pair(
                "Import/LedgerItemMaster",
                "$IMPORT/$LEDGER_MASTER"
            )

            CsvType.LocationMaster.displayNameJp -> Pair(
                "Import/LocationMaster",
                "$IMPORT/$LOCATION_MASTER"
            )

            CsvType.ItemTypeMaster.displayNameJp -> Pair(
                "Import/ItemTypeMaster",
                "$IMPORT/$ITEM_TYPE_MASTER"
            )

            CsvType.TagMaster.displayNameJp -> Pair(
                "Import/TagMaster",
                "$IMPORT/$TAG_MASTER"
            )

            CsvType.ItemTypeFieldSettingMaster.displayNameJp -> Pair(
                "Import/ItemTypeFieldSettingMaster",
                "$IMPORT/$ITEM_TYPE_FIELD_SETTING_MASTER"
            )

            CsvType.ReferenceMaster.displayNameJp -> Pair(
                "Import/ReferenceMaster",
                "$IMPORT/$REFERENCE_MASTER"
            )

            CsvType.InventoryResult.displayNameJp -> Pair(
                "Export/Inventory",
                "$EXPORT/$INVENTORY_RESULT"
            )

            CsvType.LocationChangeResult.displayNameJp -> Pair(
                "Export/LocationChange",
                "$EXPORT/$LOCATION_CHANGE_EVENT"
            )

            CsvType.OutboundResult.displayNameJp -> Pair(
                "Export/Outbound",
                "$EXPORT/$OUTBOUND"
            )

            CsvType.InboundResult.displayNameJp -> Pair(
                "Export/Inbound",
                "$EXPORT/$INBOUND"
            )

            else -> null
        }
    }

    suspend fun listExportFileName(csvType: String): List<SessionModel> {
        try {
            val listFileName = mutableListOf<SessionModel>()
            when (csvType) {
                CsvType.InboundResult.displayNameJp -> {
                    inboundSessionRepository.getExecutedAt().map { model ->
                        listFileName.add(
                            SessionModel(
                                sessionId = model.sessionId,
                                timeStamp = "${CsvType.InboundResult.displayNameEng}_${model.timeStamp}"
                            )
                        )
                    }
                }

                CsvType.OutboundResult.displayNameJp -> outboundSessionRepository.getExecutedAt()
                    .map { model ->
                        listFileName.add(
                            SessionModel(
                                sessionId = model.sessionId,
                                timeStamp = "${CsvType.OutboundResult.displayNameEng}_${model.timeStamp}"
                            )
                        )
                    }

                CsvType.LocationChangeResult.displayNameJp -> locationChangeSessionRepository.getExecutedAt()
                    .map { model ->
                        listFileName.add(
                            SessionModel(
                                sessionId = model.sessionId,
                                timeStamp = "${CsvType.LocationChangeResult.displayNameEng}_${model.timeStamp}"
                            )
                        )
                    }

                CsvType.InventoryResult.displayNameJp -> inventorySessionRepository.getExecutedAt()
                    .map { model ->
                        listFileName.add(
                            SessionModel(
                                sessionId = model.sessionId,
                                timeStamp = "${CsvType.InventoryResult.displayNameEng}_${model.timeStamp}"
                            )
                        )
                    }

                else -> {}
            }
            return listFileName
        } catch (e: Exception) {
            throw e
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
                val files = targetDir
                    .listFiles()
                    ?.filter {
                        it.isFile && it.name.lowercase().endsWith(
                            if (csvType == CsvType.ReferenceMaster.displayNameJp) ".zip" else ".csv"
                        )
                    }
                    // ✅ 更新日時降順 (newest first)
                    ?.sortedByDescending { it.lastModified() }
                    ?: emptyList()

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
    ) = withContext(Dispatchers.IO) {

        val (_, localFolder) = mapCsvTypeToFolders(csvType)
            ?: throw FolderNotFoundException()

        val targetDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "$ROOT_FOLDER/$localFolder"
        )

        val fileObj = File(targetDir, fileName)
        if (!fileObj.exists() || !fileObj.isFile) {
            throw CsvFileNotFoundException()
        }

        if (csvType == CsvType.ReferenceMaster.displayNameJp) {
            importReferenceMaster(fileObj, onProgress)
            return@withContext
        }

        val lines = fileObj.readLines(Charsets.UTF_8)
        if (lines.isEmpty()) {
            throw CsvFileEmptyException(
                fileName = fileName
            )
        }

        val importer = getImporter(csvType)
            ?: throw CsvImporterNotFoundException()

        val total = maxOf(1, lines.size)
        var count = 0

        val headerLine = lines.first()
        val headers = headerLine.split(",").map { it.trim() }

        // ===== CHECK HEADER =====
        val missingHeaders = importer.requiredHeaders - headers.toSet()
        if (missingHeaders.isNotEmpty()) {
            throw MissingHeaderException(missingHeaders.toList())
        }

        try {
            db.withTransaction {
                lines.drop(1).chunked(50).forEach { chunk ->
                    importer.importAll(headers = headers, lines = chunk)
                    count += chunk.size
                    onProgress((count.toFloat() / total).coerceIn(0f, 1f))
                }
                importer.finish()
            }
            onProgress(1f)

        } catch (_: SQLiteConstraintException) {
            throw SqliteConstraintAppException()

        } catch (e: AppException) {
            throw e

        } catch (e: Exception) {
            Log.e("TSS", "import error", e)
            throw CsvImportFailedException()
        }
    }

    private fun getImporter(csvType: String): CsvImporter<*>? {
        return when (csvType) {
            CsvType.LocationMaster.displayNameJp -> LocationMasterImporter(repository = locationMasterRepository)

            CsvType.LedgerMaster.displayNameJp -> LedgerItemMasterImporter(repository = ledgerItemRepository)

            CsvType.ItemTypeMaster.displayNameJp -> ItemTypeMasterImporter(repository = itemTypeRepository)

            CsvType.TagMaster.displayNameJp -> TagMasterImporter(repository = tagMasterRepository)

            CsvType.ItemTypeFieldSettingMaster.displayNameJp -> ItemTypeFieldSettingMasterImporter(
                repository = itemTypeFieldSettingMasterRepository
            )

            else -> null
        }
    }

    suspend fun <T : ICsvExport> saveCsv(
        context: Context,
        csvType: String,
        fileName: String,
        rows: List<T>,
        onProgress: (Float) -> Unit,
    ) = withContext(Dispatchers.IO) {
        if (rows.isEmpty()) {
            throw CsvFileEmptyException(
                fileName = fileName
            )
        }

        val resolver = context.contentResolver
        val externalUri = MediaStore.Files.getContentUri("external")

        // map csvType -> local folder (Export/InventoryResult, Export/StockEvent, ...)
        val (_, localFolder) = mapCsvTypeToFolders(csvType)
            ?: throw FolderNotFoundException()

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
        val dataLines = rows.joinToString("\n") { row ->
            row.toRow().joinToString(",") { sanitizeForLineCsv(it) }
        }

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
            ?: throw CsvFileCreateException()

        val bytes = fullContent.toByteArray()
        val totalBytes = bytes.size.takeIf { it > 0 } ?: 1
        var written = 0
        try {
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
            } ?: throw CsvWriteException()

            Log.i("TSS", "✅ CSV saved: $relativePath$fileName")
            onProgress(1f)
        } catch (e: Exception) {
            resolver.delete(uri, null, null)
            Log.e("TSS", "saveCsv error: $e")
            throw e
        }
    }

    private suspend fun importReferenceMaster(
        zipFile: File,
        onProgress: (Float) -> Unit
    ) {
        val tempDir = File(zipFile.parentFile, "tmp_reference_${System.currentTimeMillis()}")
        tempDir.mkdirs()

        try {
            unzip(zipFile, tempDir)

            val csvFiles = tempDir
                .listFiles { f -> f.extension.lowercase() == "csv" }
                ?.sortedBy { it.name }
                ?: emptyList()

            val expectedImporters: Map<KClass<out CsvImporter<*>>, String> = mapOf(
                ProcessTypeMasterImporter::class to "処理種別",
                TagStatusMasterImporter::class to "タグステータス",
                WinderMasterImporter::class to "巻取機",
                ItemCategoryMasterImporter::class to "品目区分",
                FieldMasterImporter::class to "項目プリセット",
//                InventoryResultTypeMasterImporter::class to "棚卸結果種別",
                ItemUnitMasterImporter::class to "品目単位",
                CsvTaskTypeMasterImporter::class to "CSVタスク種別"
            )

            if (csvFiles.isEmpty()) {
                throw ReferenceMasterMissingFileException(
                    expectedImporters.values.toList()
                )
            }

            // ===== PHASE 1: PRE-SCAN (NO THROW) =====
            val detectedImporters = mutableSetOf<KClass<out CsvImporter<*>>>()

            csvFiles.forEach { csv ->
                val lines = csv.readLines(Charsets.UTF_8)
                if (lines.size <= 1) {
                    // header only → file empty
                    throw CsvFileEmptyException(
                        fileName = csv.name
                    )
                }

                val headers = lines.first()
                    .split(",")
                    .map { it.trim() }
                    .toSet()

                val cls = detectReferenceImporterClass(headers)
                if (cls != null) {
                    detectedImporters.add(cls)
                }
            }

            // ===== CHECK MISSING MASTER =====
            val missing = expectedImporters.keys - detectedImporters
            if (missing.isNotEmpty()) {
                throw ReferenceMasterMissingFileException(
                    missing.map { expectedImporters[it] ?: "不明なマスタ" }
                )
            }

            // ===== PHASE 2: IMPORT =====
            val total = csvFiles.size
            var done = 0

            db.withTransaction {
                csvFiles.forEach { csv ->
                    val lines = csv.readLines(Charsets.UTF_8)
                    val headers = lines.first().split(",").map { it.trim() }.toSet()

                    val importerClass =
                        detectReferenceImporterClass(headers)
                            ?: throw CsvImporterNotFoundException()

                    val importer = createReferenceImporter(importerClass)

                    // ===== CHECK HEADER =====
                    val headerList = headers.toList()
                    val dataLines = lines.drop(1)

                    val missingHeaders = mutableListOf<String>()
                    val missingColumns = mutableListOf<String>()

                    importer.requiredHeaders.forEach { required ->
                        val headerIndex = headerList.indexOf(required)

                        if (headerIndex == -1) {
                            // header not exists → check data
                            val hasAnyData = dataLines.any { line ->
                                val values = line.split(",")
                                values.size > headerList.size && values.last().isNotBlank()
                            }

                            if (hasAnyData) {
                                missingHeaders.add(required)
                            } else {
                                missingColumns.add(required)
                            }
                        }
                    }

                    when {
                        missingHeaders.isNotEmpty() ->
                            throw MissingHeaderException(missingHeaders)

                        missingColumns.isNotEmpty() ->
                            throw MissingColumnException(missingColumns)
                    }

                    dataLines.chunked(50).forEach { chunk ->
                        importer.importAll(headers.toList(), chunk)
                    }
                    importer.finish()

                    done++
                    onProgress(done.toFloat() / total)
                }
            }

            onProgress(1f)

        } finally {
            tempDir.deleteRecursively()
        }
    }

    private fun detectReferenceImporterClass(
        headers: Set<String>
    ): KClass<out CsvImporter<*>>? {

        val importers = listOf(
            ProcessTypeMasterImporter(processTypeRepository),
            TagStatusMasterImporter(tagStatusMasterRepository),
            WinderMasterImporter(winderRepository),
            ItemCategoryMasterImporter(itemCategoryRepository),
            FieldMasterImporter(fieldMasterRepository),
//            InventoryResultTypeMasterImporter(inventoryResultTypeRepository),
            ItemUnitMasterImporter(itemUnitRepository),
            CsvTaskTypeMasterImporter(csvTaskTypeRepository)
        )

        val matched = importers.filter {
            it.requiredHeaders.any { h -> h in headers }
        }

        return if (matched.size == 1) matched.first()::class else null
    }

    private fun createReferenceImporter(
        cls: KClass<out CsvImporter<*>>
    ): CsvImporter<*> =
        when (cls) {
            ProcessTypeMasterImporter::class ->
                ProcessTypeMasterImporter(processTypeRepository)

            TagStatusMasterImporter::class ->
                TagStatusMasterImporter(tagStatusMasterRepository)

            WinderMasterImporter::class ->
                WinderMasterImporter(winderRepository)

            ItemCategoryMasterImporter::class ->
                ItemCategoryMasterImporter(itemCategoryRepository)

            FieldMasterImporter::class ->
                FieldMasterImporter(fieldMasterRepository)

//            InventoryResultTypeMasterImporter::class ->
//                InventoryResultTypeMasterImporter(inventoryResultTypeRepository)

            ItemUnitMasterImporter::class ->
                ItemUnitMasterImporter(itemUnitRepository)

            CsvTaskTypeMasterImporter::class ->
                CsvTaskTypeMasterImporter(csvTaskTypeRepository)

            else ->
                throw CsvImporterNotFoundException()
        }

    private fun unzip(zipFile: File, targetDir: File) {
        ZipInputStream(zipFile.inputStream()).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                val outFile = File(targetDir, entry.name)
                if (!entry.isDirectory) {
                    outFile.parentFile?.mkdirs()
                    outFile.outputStream().use { zis.copyTo(it) }
                }
                zis.closeEntry()
                entry = zis.nextEntry
            }
        }
    }

    private fun sanitizeForLineCsv(value: String): String {
        return value
            .replace("\r\n", "\\n")
            .replace("\n", "\\n")
            .replace("\r", "\\n")
    }

}