package com.example.sol_denka_stockmanagement.helper.csv

class CsvRow(private val data: Map<String, String>) {

    fun string(name: String): String? =
        data[name]?.trim()?.ifEmpty { null }

    fun int(name: String): Int? =
        string(name)?.toIntOrNull()

    fun boolean(name: String): Boolean {
        val raw = data[name]?.trim()
            ?: error("Required column '$name' is missing")

        return when (raw.lowercase()) {
            "1", "true" -> true
            "0", "false" -> false
            else -> error("Invalid boolean value '$raw' for column '$name'")
        }
    }
}

