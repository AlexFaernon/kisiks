package com.example.cashincontrol.domain.bankParcing

import android.net.Uri
import android.util.Log
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor
import com.itextpdf.kernel.pdf.canvas.parser.listener.FilteredEventListener
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy


class BankParcer() {
    companion object {
        fun parse(path: Uri) {
            val reader = PdfReader("/res/raw/file.pdf")
            val pdfDoc = PdfDocument(reader)
            Log.d("Parse", "Start")
            val listener = FilteredEventListener()
            val extractionStrategy: LocationTextExtractionStrategy = listener
                .attachEventListener(LocationTextExtractionStrategy())
            val parser = PdfCanvasProcessor(listener)
            parser.processPageContent(pdfDoc.getFirstPage())
            val actualText = extractionStrategy.resultantText

            print(actualText)
            pdfDoc.close()
        }
    }
}