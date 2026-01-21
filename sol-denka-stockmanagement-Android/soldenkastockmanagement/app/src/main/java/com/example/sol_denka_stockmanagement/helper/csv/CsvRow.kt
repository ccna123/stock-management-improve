package com.example.sol_denka_stockmanagement.helper.csv

import com.example.sol_denka_stockmanagement.exception.CsvFormatException
import java.math.BigDecimal
import java.math.RoundingMode

class CsvRow(private val data: Map<String, String>) {

    fun string(name: String): String? =
        data[name]?.trim()?.ifEmpty { null }

    fun stringWithLength(
        name: String,
        min: Int,
        max: Int
    ): String? {
        val value = string(name) ?: return null

        val length = value.length
        if (length < min || length > max) {
            throw CsvFormatException(
                "カラム「$name」は $min ～ $max 文字で入力してください。（現在: $length 文字）"
            )
        }
        return value
    }

    fun stringWithExactLength(
        name: String,
        length: Int
    ): String? {
        val value = string(name) ?: return null

        if (value.length != length) {
            throw CsvFormatException(
                "カラム「$name」は $length 文字で入力してください。（現在: ${value.length} 文字）"
            )
        }
        return value
    }

    fun long(name: String): Long? {
        val raw = string(name) ?: return null
        val value = raw.toLongOrNull()
            ?: throw CsvFormatException("カラム「$name」に整数として不正なデータ「$raw」が入力されています。")

        if (value < 0) {
            throw CsvFormatException("カラム「$name」に負の値は入力できません。（$raw）")
        }
        return value
    }

    fun int(name: String): Int? {
        val raw = string(name) ?: return null
        return raw.toIntOrNull()
            ?: throw CsvFormatException(
                "カラム「$name」に整数として不正なデータ「$raw」が入力されています。"
            )
    }

    fun intRange(
        name: String,
        min: Int,
        max: Int
    ): Int? {
        val raw = string(name) ?: return null

        val value = raw.toIntOrNull()
            ?: throw CsvFormatException(
                "カラム「$name」に整数として不正なデータ「$raw」が入力されています。"
            )

        if (value < min || value > max) {
            throw CsvFormatException(
                "カラム「$name」は $min ～ $max の範囲で入力してください。（現在: $value）"
            )
        }
        return value
    }


    fun decimal3(
        name: String,
        min: BigDecimal,
        max: BigDecimal
    ): BigDecimal? {
        val raw = string(name) ?: return null

        val value = try {
            raw.toBigDecimal()
        } catch (_: NumberFormatException) {
            throw CsvFormatException(
                "カラム「$name」に小数値として不正なデータ「$raw」が入力されています。"
            )
        }

        val scaled = value.setScale(3, RoundingMode.DOWN)

        if (scaled < min || scaled > max) {
            throw CsvFormatException(
                "カラム「$name」は $min ～ $max の範囲で入力してください。（現在: $scaled）"
            )
        }

        return scaled
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

