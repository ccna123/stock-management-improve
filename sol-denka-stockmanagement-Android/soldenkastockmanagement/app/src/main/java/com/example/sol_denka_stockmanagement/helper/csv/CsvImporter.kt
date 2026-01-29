package com.example.sol_denka_stockmanagement.helper.csv

import android.util.Log
import com.example.sol_denka_stockmanagement.exception.CsvImportFailedException
import com.example.sol_denka_stockmanagement.exception.MissingColumnException

abstract class CsvImporter<T> {

    protected val buffer = mutableListOf<T>()
    abstract val requiredHeaders: Set<String>

    protected abstract fun mapRow(row: CsvRow): T
    protected abstract suspend fun replaceAllWithNewData(entities: List<T>)
    fun importAll(headers: List<String>, lines: List<String>) {
        importChunk(headers, lines)
    }

    fun importChunk(headers: List<String>, lines: List<String>) {
        if (lines.isEmpty()) return

        val entities = lines
            .filter { it.isNotBlank() }
            .map { line ->
                val values = line.split(",")

                // 🔥 DATA COLUMN CHECK
                if (values.size < headers.size) {
                    val missingCols = headers.drop(values.size)
                    throw MissingColumnException(missingCols)
                }
                val rowMap = headers.zip(values).toMap()
                mapRow(CsvRow(rowMap))
            }

        buffer.addAll(entities)
    }

    suspend fun finish() {
        Log.e("TSS", "buffer is empty ?: ${buffer.isEmpty()}")
        if (buffer.isEmpty()) {
            throw CsvImportFailedException()
        }
        try {
            replaceAllWithNewData(buffer)
        } catch (e: Exception) {
            Log.e("TSS", "finish: $e")
            throw CsvImportFailedException()
        } finally {
            buffer.clear()
        }
    }
}

