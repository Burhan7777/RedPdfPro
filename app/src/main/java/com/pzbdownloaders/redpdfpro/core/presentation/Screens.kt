package com.pzbdownloaders.redpdfpro.core.presentation

sealed class Screens(var route: String) {
    object HomePage : Screens("home_page_screen")
    object SplitPdf : Screens("split_pdf_screen")
    object MergePdf : Screens("merge_pdf_screen")
    object CompressPDf : Screens("compress_pdf_screen")
    object ExtractText : Screens("extract_text_screen")
}
