package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.exception.CsvFormatException
import java.math.BigDecimal
import java.math.RoundingMode

class CsvRow(private val data: Map<String, String>) {

    fun string(name: String): String? =
        data[name]?.trim()?.ifEmpty { null }

    fun int(name: String): Int? {
        val raw = string(name) ?: return null
        return raw.toIntOrNull()
            ?: throw CsvFormatException(
                "カラム「$name」に整数として不正なデータ「$raw」が入力されています。"
            )
    }

    fun decimal3(name: String): BigDecimal? {
        val raw = string(name) ?: return null
        return try {
            raw.toBigDecimal().setScale(3, RoundingMode.DOWN)
        } catch (_: NumberFormatException) {
            throw CsvFormatException(
                "カラム「$name」に小数値として不正なデータ「$raw」が入力されています。"
            )
        }
    }


    fun boolean(name: String): Boolean {
        val raw = string(name)
            ?: throw CsvFormatException(
                "カラム「$name」は必須ですが値が空です。"
            )

        return when (raw.lowercase()) {
            "1", "true" -> true
            "0", "false" -> false
            else -> throw CsvFormatException(
                "カラム「$name」に真偽値として不正なデータ「$raw」が入力されています。（true / false / 1 / 0 のみ可）"
            )
        }
    }
}

