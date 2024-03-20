package com.pzbdownloaders.redpdfpro.splitpdffeature.components

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState

data class modelBitmap(
    var bitmap: Bitmap?,
    var isSelected: MutableState<Boolean>
) {
    fun toggle() {
        isSelected .value= !isSelected.value
    }
}