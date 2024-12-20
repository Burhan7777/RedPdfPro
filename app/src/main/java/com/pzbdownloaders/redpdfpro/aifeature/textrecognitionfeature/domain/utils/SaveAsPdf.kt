package com.pzbdownloaders.redpdfpro.aifeature.textrecognitionfeature.domain.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

fun exportPDF(context: Context, content: String, fileName: String) {
    val pdfDocument = PdfDocument()

    // Page description (A4 size page)
    val pageWidth = 595
    val pageHeight = 842
    val contentStartX = 10f
    var contentStartY = 100f
    val maxWidth = pageWidth - 20f // Considering some padding
    val maxHeight = pageHeight - 40f // Leaving room at the bottom for page boundaries

    // Paint for title
    val titlePaint = Paint().apply {
        textSize = 24f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    // Paint for content
    val contentPaint = Paint().apply {
        textSize = 14f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    }

    // Create the first page
    var page =
        pdfDocument.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create())
    var canvas = page.canvas

    // Draw the title
    // canvas.drawText(title, contentStartX, 50f, titlePaint)

    // Function to create a new page
    fun createNewPage() {
        pdfDocument.finishPage(page) // Finish the current page
        page = pdfDocument.startPage(
            PdfDocument.PageInfo.Builder(
                pageWidth,
                pageHeight,
                pdfDocument.pages.size + 1
            ).create()
        ) // Start a new page
        canvas = page.canvas // Update the canvas to the new page
        contentStartY = 40f // Reset Y to start a new page
    }

    // Split content into paragraphs by newline characters
    val paragraphs = content.split("\n")

    // Loop through each paragraph and handle pagination
    for (paragraph in paragraphs) {
        val lines = splitTextIntoLines(paragraph, contentPaint, maxWidth)
        for (line in lines) {
            // Check if the line will fit in the remaining space
            if (contentStartY + (contentPaint.descent() - contentPaint.ascent()) > maxHeight) {
                // If it doesn't fit, create a new page
                createNewPage()
            }
            // Draw the line on the canvas
            canvas.drawText(line, contentStartX, contentStartY, contentPaint)
            contentStartY += contentPaint.descent() - contentPaint.ascent() + 5 // Move to the next line, with extra space
        }
        contentStartY += 10f // Extra space between paragraphs
    }

    // Finish the last page
    pdfDocument.finishPage(page)

    val file = File(context.cacheDir, "$fileName.pdf")
    try {
        val downloadsDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "Pro Scanner/Pdfs"
        )
        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs() // Create the directory if it doesn't exist
        }
        val downloadFile = File(downloadsDir, "$fileName.pdf")
        val downloadOutputStream = FileOutputStream(downloadFile)
        pdfDocument.writeTo(downloadOutputStream)
        downloadOutputStream.close()

        pdfDocument.close()

        // Share the PDF
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

// Function to split text into lines that fit within a specified width
fun splitTextIntoLines(text: String, paint: Paint, maxWidth: Float): List<String> {
    val lines = mutableListOf<String>()
    var currentLine = StringBuilder()

    // Split text by words to wrap the lines
    val words = text.split(" ")
    for (word in words) {
        val potentialLine = if (currentLine.isEmpty()) word else "${currentLine} $word"

        // If the line fits within the maxWidth, keep adding words
        if (paint.measureText(potentialLine) <= maxWidth) {
            currentLine = StringBuilder(potentialLine)
        } else {
            // When the line exceeds maxWidth, add it to the list and start a new line
            lines.add(currentLine.toString())
            currentLine = StringBuilder(word)
        }
    }

    // Add the last line if it's not empty
    if (currentLine.isNotEmpty()) {
        lines.add(currentLine.toString())
    }

    return lines
}