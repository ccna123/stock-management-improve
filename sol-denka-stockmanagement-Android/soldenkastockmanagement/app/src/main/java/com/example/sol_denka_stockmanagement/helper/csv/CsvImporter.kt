package com.example.sol_denka_stockmanagement.helper.csv

sealed class CsvImporter<T> {

    protected val buffer = mutableListOf<T>()

    protected abstract fun parse(lines: List<String>): List<T>

    protected abstract suspend fun persist(entities: List<T>)

    suspend fun importChunk(lines: List<String>) {
        if (lines.isEmpty()) return
        val entities = parse(lines)
        buffer.addAll(entities)
    }

    suspend fun finish(){
        if (buffer.isEmpty()) return
        persist(buffer)
        buffer.clear()
    }
}