package com.example.cashincontrol.domain.parsing

import com.example.cashincontrol.domain.UserClass
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class BankParser() {
    companion object {
        const val prefix = "операции²"
        const val suffix = "Продолжение на следующей странице"

        fun parse(inputStream: InputStream) {
            val parsingString = PdfHandler.getTrimmedPage(inputStream, prefix, suffix)

            val transactionParcer =
                """(\d\d\.\d\d\.\d\d\d\d \d\d:\d\d) \d* (\D*) ([0-9,  +]*) ([0-9,  +]*)\n[\d.]* (.*)\.\s\D*\d{4}\n*""".toRegex()
            for (transactionMatch in transactionParcer.findAll(parsingString)) {
                val (datetimeStr, categoryStr, sumStr, balanceStr, commentary) = transactionMatch.destructured

                val datetime = LocalDateTime.parse(
                    datetimeStr,
                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                )
                val isExpenses = sumStr[0] != '+'
                val category = UserClass.getOrCreateCategory(categoryStr, isExpenses)
                val sum = sumStr.replace(',', '.').replace(" ", "").toFloat()

                UserClass.addTransaction(isExpenses, sum, datetime, category, commentary, true)
            }
        }
    }
}