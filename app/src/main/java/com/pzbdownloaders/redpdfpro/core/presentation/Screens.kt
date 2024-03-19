package com.pzbdownloaders.redpdfpro.core.presentation

sealed class Screens(var route: String) {
    object homePage : Screens("home_page_screen")
}
