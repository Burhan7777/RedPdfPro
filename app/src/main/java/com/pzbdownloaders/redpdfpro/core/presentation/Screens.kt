package com.pzbdownloaders.redpdfpro.core.presentation

const val TOOLS_GRAPH = "tools_graph_route"
const val SCANNER_GRAPH = "scanner_graph_route"

sealed class Screens(var route: String) {
    object HomePage : Screens("home_page_screen")
    object SplitPdf : Screens("split_pdf_screen")
    object MergePdf : Screens("merge_pdf_screen")
    object CompressPDf : Screens("compress_pdf_screen")
    object ExtractText : Screens("extract_text_screen")
    object RotatePdf : Screens("rotate_pdf_screen")
    object LockPdf : Screens("lock_pdf_screen")
    object UnlockPdf : Screens("unlock_pdf_screen")
    object ExtractImageFromPdf : Screens("extract_image_screen")
    object ImageToPdf : Screens("image_to_pdf_screen")
    object DocsToPdf : Screens("docs_to_pdf_screen")

}
