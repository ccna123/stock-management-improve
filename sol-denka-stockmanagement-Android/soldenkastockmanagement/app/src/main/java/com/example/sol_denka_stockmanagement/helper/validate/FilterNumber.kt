package com.example.sol_denka_stockmanagement.helper.validate

object FilterNumber {
    fun filterNumber(input: String): String{
        if (input.isBlank()) return ""

        // minus and not digit symbol
        var value = input
            .replace("-", "")
            .filter { it.isDigit() || it == '.' }

        // allow only 1 dot for decimal number
        val firstDot = value.indexOf('.')
        if (firstDot != -1){
            value =
                value.substring(0, firstDot +1) +
                value.substring(firstDot +1).replace(".", "")
        }

        // do not allow start with dot -> force to 0
        if (value.startsWith(".")) return ""

        // continuous leading zero handle
        if (value.startsWith("0") && value.length > 1 && !value.startsWith("0.")){
            value  = value.dropWhile { it == '0' }
            if (value.isEmpty()) value = "0"
        }
        return value
    }
}