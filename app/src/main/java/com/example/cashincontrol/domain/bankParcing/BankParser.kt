package com.example.cashincontrol.domain.bankParcing

import com.example.cashincontrol.domain.UserClass
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor
import com.itextpdf.kernel.pdf.canvas.parser.listener.FilteredEventListener
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class BankParser() {
    companion object {
        const val prefix = "операции²"
        const val suffix = "Продолжение на следующей странице"

        fun parse(inputStream: InputStream) {
            val reader = PdfReader(inputStream)
            val pdfDoc = PdfDocument(reader)

//          val transactionsRegex = """\d\d\.\d\d\.\d\d\d\d \d\d:\d\d.*\n.*\n.*\*{4}[0-9]{4}""".toRegex()
            val listener = FilteredEventListener()
            val stringBuilder = StringBuilder()
            val pageCount = pdfDoc.numberOfPages
            for (pageNumber in 1..pageCount) {
                val extractionStrategy: LocationTextExtractionStrategy = listener
                    .attachEventListener(LocationTextExtractionStrategy())
                val parser = PdfCanvasProcessor(listener)
                parser.processPageContent(pdfDoc.getPage(pageNumber))
                val pageText = extractionStrategy.resultantText
                stringBuilder.append(pageText.substringAfter(prefix).substringBefore(suffix))
            }

            val transactionParcer =
                """(\d\d\.\d\d\.\d\d\d\d \d\d:\d\d) \d* (\D*) ([0-9,  +]*) ([0-9,  +]*)\n[\d.]* (.*)\.\s\D*\d{4}\n*""".toRegex()
            for (transactionMatch in transactionParcer.findAll(stringBuilder.toString())) {
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
            pdfDoc.close()
        }
    }
}