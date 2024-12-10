package com.example.cashincontrol.domain.parsing

import android.util.Log
import com.example.cashincontrol.domain.UserClass
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
//            val goodsNameRegex = """^\d+\s*(.*)\n""".toRegex(RegexOption.MULTILINE)
//            val priceRegex = """Цена\*Кол\s*([\d.]*).*\n""".toRegex()

            Log.d("Parsing", "Start check parsing")

            for (match in checkRegex.findAll(parseString)){
                val (nameRaw, priceStr) = match.destructured
                val namesForCategory = nameRaw.split(' ', '.').toMutableList().filter { it.all { it.isLetter() } }.map { it.lowercase() }
                val price = priceStr.replace(',', '.').toFloat()
                val checkCategory = UserClass.findCheckCategory(namesForCategory)
                if (checkCategory != null){
                    Log.d("checkCategory", "Found ${checkCategory.name} for $nameRaw; price: $price")
                }
            }
        }
    }
}