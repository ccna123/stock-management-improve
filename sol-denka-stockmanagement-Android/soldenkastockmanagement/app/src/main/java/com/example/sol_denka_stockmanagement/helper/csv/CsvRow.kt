package com.example.sol_denka_stockmanagement.helper.csv

import java.math.BigDecimal
import java.math.RoundingMode

class CsvRow(private val data: Map<String, String>) {

    fun string(name: String): String? =
        data[name]?.trim()?.ifEmpty { null }

    fun int(name: String): Int? =
        string(name)?.toIntOrNull()

    fun decimal3(name: String): BigDecimal? {
        val raw = string(name) ?: return null

        return try {
            raw.toBigDecimal()
                .setScale(3, RoundingMode.DOWN)
        } catch (_: NumberFormatException) {
            error("カラム「$name」に小数値として不正なデータ「$raw」が入力されています。")
        }
    }


    fun boolean(name: String): Boolean {
        val raw = data[name]?.trim()
            ?: error("Required column '$name' is missing")

        return when (raw.lowercase()) {
            "1", "true" -> true
            "0", "false" -> false
            else -> error("カラム「$name」に真偽値として不正なデータ「$raw」が入力されています。（true / false / 1 / 0 のみ可）")
        }
    }
}

