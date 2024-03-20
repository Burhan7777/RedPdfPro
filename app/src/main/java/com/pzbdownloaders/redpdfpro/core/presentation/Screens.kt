package com.pzbdownloaders.redpdfpro.core.presentation

sealed class Screens(var route: String) {
    object homePage : Screens("home_page_screen")
    object splitPdf : Screens("split_pdf_screen")
    object mergePdf : Screens("merge_pdf_screen")
}
