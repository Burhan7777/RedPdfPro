package com.pzbdownloaders.redpdfpro.core.presentation

import android.net.Uri

const val TOOLS_GRAPH = "tools_graph_route"
const val SCANNER_GRAPH = "scanner_graph_route"
const val DOCUMENTS_GRAPH = "documents_graph_route"

sealed class Screens(var route: String) {
    object HomePage : Screens("home_page_screen")
    object SplitPdf : Screens("split_pdf_screen?filePath={filePath}") {
        fun splitPdfPassFilePath(filePath: String): String {
            return "split_pdf_screen?filePath=$filePath"
        }
    }

    object MergePdf : Screens("merge_pdf_screen?fileName={fileName}?filePath={filePath}") {
        fun mergePDfWithArguments(fileName: String, filePath: String): String {
            return "merge_pdf_screen?fileName=$fileName?filePath=$filePath"
        }
    }

    object CompressPDf : Screens("compress_pdf_screen")
    object ExtractText : Screens("extract_text_screen")
    object RotatePdf : Screens("rotate_pdf_screen")
    object LockPdf : Screens("lock_pdf_screen")
    object UnlockPdf : Screens("unlock_pdf_screen")
    object ExtractImageFromPdf : Screens("extract_image_screen")
    object ImageToPdf : Screens("image_to_pdf_screen")
    object DocsToPdf : Screens("docs_to_pdf_screen")
    object ScanToDocx : Screens("scan_to_docx_screen")
    object ScanToTxt : Screens("scan_to_txt_screen")
    object Documents : Screens("document_screen")
    object PdfReader : Screens("pdf_reader_screen/?uri={uri}") {
        fun pdfReaderWithUri(uri: String): String {
            return "pdf_reader_screen/?uri=$uri"
        }
    }

}
