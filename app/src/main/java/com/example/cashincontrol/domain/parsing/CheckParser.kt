package com.example.cashincontrol.domain.parsing

import android.util.Log
import com.example.cashincontrol.domain.UserClass
import com.example.cashincontrol.domain.transaction.CheckCategory
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CheckParser {
    companion object {
        private const val PREFIX = ""
        private const val SUFFIX = ""

        fun parse(inputStream: InputStream) {
            val parseString = PdfHandler.getTrimmedPage(inputStream, PREFIX, SUFFIX)
                .substringAfter("Признак агента")
                .substringBefore("Итог")

            val datetimeRegex = """(\d\d.\d\d.\d\d\d\d) \| (\d\d:\d\d)""".toRegex()
            val (date, time) = datetimeRegex.find(parseString)!!.destructured
            val checkDatetime = LocalDateTime.parse(
                "$date $time",
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))

            val checkRegex = """^\d+\s*(.*)(?:\n.*)*?\nЦена\*Кол\s*([\d.]*).*\n""".toRegex(RegexOption.MULTILINE)
//            val goodsNameRegex = """^\d+\s*(.*)\n""".toRegex(RegexOption.MULTILINE)
//            val priceRegex = """Цена\*Кол\s*([\d.]*).*\n""".toRegex()

            Log.d("Parsing", "Start check parsing")

            val categories: MutableMap<CheckCategory, Float> = mutableMapOf()
            for (match in checkRegex.findAll(parseString)){
                val (nameRaw, priceStr) = match.destructured
                val namesForCategory = nameRaw.split(' ', '.').toMutableList().filter { it.all { it.isLetter() } }.map { it.lowercase() }
                val price = priceStr.replace(',', '.').toFloat()
                val checkCategory = UserClass.findCheckCategory(namesForCategory)
                if (checkCategory != null){
                    Log.d("checkCategory", "Found ${checkCategory.name} for $nameRaw; price: $price")

                    if (categories.containsKey(checkCategory)){
                        categories[checkCategory] = categories[checkCategory]!! + price
                    }
                    else{
                        categories[checkCategory] = price
                    }
                }
            }

            UserClass.addCheckTransaction(checkDatetime, categories)
        }
    }
}