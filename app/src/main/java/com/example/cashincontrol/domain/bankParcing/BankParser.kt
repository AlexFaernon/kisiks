package com.example.cashincontrol.domain.bankParcing

import android.net.Uri
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor
import com.itextpdf.kernel.pdf.canvas.parser.listener.FilteredEventListener
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy
import com.itextpdf.styledxmlparser.resolver.resource.ResourceResolver
import java.io.InputStream


class BankParser() {
    companion object {
        fun parse(inputStream: InputStream) {
            val reader = PdfReader(inputStream)
            val pdfDoc = PdfDocument(reader)
            val listener = FilteredEventListener()
            val extractionStrategy: LocationTextExtractionStrategy = listener
                .attachEventListener(LocationTextExtractionStrategy())
            val parser = PdfCanvasProcessor(listener)
            parser.processPageContent(pdfDoc.getFirstPage())
            val actualText = extractionStrategy.resultantText

            Log.d("Parsed", actualText)
            pdfDoc.close()
        }
    }
}