package com.example.cashincontrol.domain.parsing

import android.util.Log
import java.io.InputStream

class CheckParser {
    companion object {
        private const val PREFIX = ""
        private const val SUFFIX = ""

        fun parse(inputStream: InputStream) {
            val parseString = PdfHandler.getTrimmedPage(inputStream, PREFIX, SUFFIX)
                .substringAfter("приход")
                .substringBefore("Итог")

            val checkRegex = """^\d+\s*(.*)(?:\n.*)*?\nЦена\*Кол\s*([\d.]*).*\n""".toRegex(RegexOption.MULTILINE)
            val goodsNameRegex = """^\d+\s*(.*)\n""".toRegex(RegexOption.MULTILINE)
            val priceRegex = """Цена\*Кол\s*([\d.]*).*\n""".toRegex()

            Log.d("Parsing", "Start check parsing")

            val goodsNames = goodsNameRegex.findAll(parseString).toList()
            val prices = priceRegex.findAll(parseString).toList()

            for (match in checkRegex.findAll(parseString)){
                val (name, price) = match.destructured
                Log.d("Check parsing", "${name}\n${price}")
            }
        }
    }
}