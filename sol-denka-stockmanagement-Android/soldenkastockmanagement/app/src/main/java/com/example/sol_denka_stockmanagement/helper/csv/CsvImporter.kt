package com.example.sol_denka_stockmanagement.helper.csv

abstract class CsvImporter<T> {

    protected val buffer = mutableListOf<T>()
    abstract val requiredHeaders: Set<String>

    protected abstract fun mapRow(row: CsvRow): T
    protected abstract suspend fun persist(entities: List<T>)

    fun importChunk(headers: List<String>, lines: List<String>) {
        if (lines.isEmpty()) return

        val entities = lines
            .filter { it.isNotBlank() }
            .map { line ->
                val values = line.split(",")
                val rowMap = headers.zip(values).toMap()
                mapRow(CsvRow(rowMap))
            }

        buffer.addAll(entities)
    }

    suspend fun finish() {
        if (buffer.isEmpty()) return
        persist(buffer)
        buffer.clear()
    }
}

