package com.example.cashincontrol.domain.parsing

import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor
import com.itextpdf.kernel.pdf.canvas.parser.listener.FilteredEventListener
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy
import java.io.InputStream

class PdfHandler {
    companion object{
        fun getTrimmedPage(inputStream: InputStream, prefix: String = "", suffix: String = ""): String {
            val reader = PdfReader(inputStream)
            val pdfDoc = PdfDocument(reader)

            val listener = FilteredEventListener()
            val stringBuilder = StringBuilder()
            val pageCount = pdfDoc.numberOfPages
            for (pageNumber in 1..pageCount) {
                val extractionStrategy: LocationTextExtractionStrategy = listener
                    .attachEventListener(LocationTextExtractionStrategy())
                val parser = PdfCanvasProcessor(listener)
                parser.processPageContent(pdfDoc.getPage(pageNumber))
                val pageText = extractionStrategy.resultantText
                var result = pageText
                if (prefix.isNotEmpty()){
                    result = result.substringAfter(prefix)
                }
                if (suffix.isNotEmpty()){
                    result = result.substringBefore(suffix)
                }
                stringBuilder.append(result)
            }

            pdfDoc.close()
            return stringBuilder.toString()
        }
    }
}