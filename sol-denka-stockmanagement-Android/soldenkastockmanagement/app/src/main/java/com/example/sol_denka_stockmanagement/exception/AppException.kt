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
    params = mapOf("missing_columns" to missing)
)

class MissingHeaderException(
    missing: List<String>
) : AppException(
    statusCode = StatusCode.MISSING_HEADER,
    params = mapOf("missing_headers" to missing)
)

class CsvFormatException(message: String) : AppException(
    statusCode = StatusCode.FAILED,
    message = message
)

class CsvFileNotFoundException(): AppException(
    statusCode = StatusCode.FILE_NOT_FOUND
)

class CsvFileEmptyException(
    fileName: String
) : AppException(
    statusCode = StatusCode.FILE_EMPTY,
    params = mapOf("file" to fileName)
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

class CsvSchemaException: AppException(
    statusCode = StatusCode.CSV_SCHEMA_ERROR
)

class CsvImportFailedException: AppException(
    statusCode = StatusCode.IMPORT_FAILED
)

class ReferenceMasterMissingFileException(
    missing: List<String>
) : AppException(
    statusCode = StatusCode.REFERENCE_MASTER_MISSING_FILE,
    params = mapOf("missing_files" to missing)
)