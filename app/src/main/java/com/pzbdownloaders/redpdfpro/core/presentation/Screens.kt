package com.pzbdownloaders.redpdfpro.core.presentation

import android.net.Uri
import java.io.File

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
    object PdfReader : Screens("pdf_reader_screen/?uri={uri}/?file={file}") {
        fun pdfReaderWithUri(uri: String, file: String): String {
            return "pdf_reader_screen/?uri=$uri/?file=$file"
        }
    }

    object ViewSplitPdfScreen : Screens("view_split_pdf_screen/?path={path}/?uri={uri}") {
        fun viewSplitPdfScreen(path: String, uri: String): String {
            return "view_split_pdf_screen/?path=$path/?uri=$uri"
        }
    }

    object FinalScreenOfPdfOperations :
        Screens("final_screen_of_pdf_operations/?path={path}/?uri={uri}?pathOfUnlockedFile={pathOfUnlockedFile}") {
        fun finalScreen(path: String, uri: String, pathOfUnlockedFile: String): String {
            return "final_screen_of_pdf_operations/?path=$path/?uri=$uri?pathOfUnlockedFile=$pathOfUnlockedFile"
        }
    }


    object PdfViewer : Screens("pdf_viewer_screen/?uri={uri}/?file={file}") {
        fun pdfViewerWIthUri(uri: String, file: String): String {
            return "pdf_viewer_screen/?uri=$uri/?file=$file"
        }
    }

    object ViewPdfRotateScreen : Screens("view_rotate_pdf_screen/?path={path}") {
        fun viewPdfRotateScreen(path: String): String {
            return "view_rotate_pdf_screen/?path=$path"
        }
    }

    object FinalScreenOFImageExtraction :
        Screens("final_screen_of_image_extraction?pathOfTempFIle={pathOfTempFile}") {
        fun withParameters(pathOfTempFile: String): String {
            return "final_screen_of_image_extraction?pathOfTempFIle=$pathOfTempFile"
        }
    }

    object FinalScreenOfTextExtraction :
        Screens("final_screen_of_text_extraction/?pathOfFile={pathOfFile}") {
        fun withParameters(pathOfFile: String): String {
            return "final_screen_of_text_extraction/?pathOfFile=$pathOfFile"
        }
    }

    object AIScreen : Screens("ai_screen")

    object FinalScreenDocx : Screens("final_screen_docx/?pathOfFile={pathOfFile}") {
        fun withParameters(pathOfFile: String): String {
            return "final_screen_docx/?pathOfFile=$pathOfFile"
        }

    }

    object PptxToPdf : Screens("pptx_to_pdf_screen")

    object PdfToDocxScreen : Screens("pdf_to_docx_screen")

    object XlsToPdf : Screens("xls_to_pdf_screen")

    object XlsxToPdf : Screens("xlsx_to_pdf_screen")

    object DocToPdf : Screens("doc_to_pdf_screen")

    object PptToPdf : Screens("ppt_to_pdf_screen")

    object CsvToPdf : Screens("csv_to_pdf_screen")

    object EpubToPdf : Screens("epub_to_pdf_screen")

    object FinalScreen :
        Screens("final_screen/?pathOfFile={pathOfFile}/?pathOfDir={pathOfDir}/?mimeType={mimeType}/?stringResource={stringResource}") {
        fun withParameters(
            pathOfFile: String,
            pathOfDir: String,
            mimeType: String,
            stringResource: Int
        ): String {
            return "final_screen/?pathOfFile=$pathOfFile/?pathOfDir=$pathOfDir/?mimeType=$mimeType/?stringResource=$stringResource"
        }
    }

    object PdfToJpgScreen : Screens("pdf_to_jpg_screen")

    object PdfToDocScreen : Screens("pdf_to_doc_screen")

    object PdfToPptxScreen : Screens("pdf_to_pptx_screen")

    object PdfToPptScreen : Screens("pdf_to_ppt_screen")

    object PdfToEpubScreen : Screens("pdf_to_epub_screen")

    object PdfToCsvScreen : Screens("pdf_to_csv_screen")

    object PdfToXls : Screens("pdf_to_xls_screen")

    object PdfToXlsx:Screens("pdf_to_xlsx_screen")

    object PdfToHtml5:Screens("pdf_to_html5_screen")

}
