package com.example.sol_denka_stockmanagement.exception

import com.example.sol_denka_stockmanagement.constant.StatusCode

sealed class AppException(
    val statusCode: StatusCode,
    val params: Map<String, Any>? = null,
    message: String? = null
) : RuntimeException(message)

class MissingColumnException(
    missing: List<String>
) : AppException(
    statusCode = StatusCode.MISSING_COLUMN,
    params = mapOf("missing_headers" to missing)
)

class CsvFormatException(message: String) : AppException(
    statusCode = StatusCode.FAILED,
    message = message
)

class FileEmptyException : AppException(
    statusCode = StatusCode.FILE_EMPTY
)

class FileNotFoundException : AppException(
    statusCode = StatusCode.FILE_EMPTY
)

class CsvImporterNotFoundException : AppException(
    statusCode = StatusCode.CSV_IMPORTER_NOT_FOUND
)

class SqliteConstraintAppException: AppException(
    statusCode = StatusCode.SQLITE_CONSTRAINT_ERROR
)

class FolderNotFoundException: AppException(
    statusCode = StatusCode.FOLDER_NOT_FOUND
)

class CsvFileCreateException: AppException(
    statusCode = StatusCode.FILE_CREATED_FAILED
)

class CsvWriteException: AppException(
    statusCode = StatusCode.WRITE_ERROR
)