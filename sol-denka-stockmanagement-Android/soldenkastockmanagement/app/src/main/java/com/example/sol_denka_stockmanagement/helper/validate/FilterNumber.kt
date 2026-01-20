package com.example.sol_denka_stockmanagement.helper.validate

object FilterNumber {

    fun filterNumber(
        input: String,
        allowDecimal: Boolean
    ): String {
        if (input.isBlank()) return ""

        var value = input
            .replace("-", "")
            .filter { it.isDigit() || (allowDecimal && it == '.') }

        if (allowDecimal) {
            // allow only 1 dot
            val firstDot = value.indexOf('.')
            if (firstDot != -1) {
                value =
                    value.substring(0, firstDot + 1) +
                            value.substring(firstDot + 1).replace(".", "")
            }

            // cannot start with dot
            if (value.startsWith(".")) return ""
        } else {
            // INTEGER: remove all dots
            value = value.replace(".", "")
        }

        // leading zero handle
        if (value.startsWith("0") && value.length > 1 && !value.startsWith("0.")) {
            value = value.dropWhile { it == '0' }
            if (value.isEmpty()) value = "0"
        }

        return value
    }
}