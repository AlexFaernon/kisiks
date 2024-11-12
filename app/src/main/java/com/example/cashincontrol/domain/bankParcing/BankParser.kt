package com.example.cashincontrol.domain.bankParcing

import android.util.Log
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor
import com.itextpdf.kernel.pdf.canvas.parser.listener.FilteredEventListener
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy
import java.io.InputStream


class BankParser() {
    companion object {
        const val prefix = "Дата обработки¹ и код авторизации Описание операции Сумма в валюте\n" +
                " операции²\n"
        const val suffix = "Продолжение на следующей странице"
        fun parse(inputStream: InputStream) {
            val reader = PdfReader(inputStream)
            val pdfDoc = PdfDocument(reader)

            val listener = FilteredEventListener()
            val extractionStrategy: LocationTextExtractionStrategy = listener
                .attachEventListener(LocationTextExtractionStrategy())
            val parser = PdfCanvasProcessor(listener)
            parser.processPageContent(pdfDoc.getPage(2))

            val actualText = extractionStrategy.resultantText
            val transactionsRegex =
                """\d\d\.\d\d\.\d\d\d\d \d\d:\d\d.*\n.*\n.*\*{4}[0-9]{4}""".toRegex()
            val parced = transactionsRegex.findAll(actualText)

            val listOfRawTransactions = mutableListOf<String>()
            for (i in parced){
                listOfRawTransactions.add(i.value)
            }

            val transactionParcer =
                """"(\d\d\.\d\d\.\d\d\d\d \d\d:\d\d) \d* (\D*) ([0-9,]*) ([0-9,  ]*)\n[\d.]* (.*)\.\s\D*\d{4}\n*"""".toRegex()
            for (rawTransaction in listOfRawTransactions){
                val transactionMatch = transactionParcer.find(rawTransaction)
                val (datetime, category, sum, balance, org) = transactionMatch!!.destructured
                val result = "date: $datetime, cat: $category, sum: $sum, balance: $balance, org: $org"
                Log.d("Result", result)
            }

            Log.d("Parsed", actualText)
            pdfDoc.close()
        }
    }
}