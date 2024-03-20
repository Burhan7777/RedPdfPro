package com.pzbdownloaders.redpdfpro.core.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.pzbdownloaders.redpdfpro.splitpdf.components.modelBitmap

class MyViewModel : ViewModel() {
    var modelList = mutableStateListOf<modelBitmap>()
}