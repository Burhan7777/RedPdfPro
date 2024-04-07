package com.pzbdownloaders.redpdfpro.mergepdffeature.util

import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity

fun getFileName(uri: Uri, activity: MainActivity): String {
    var result = ""
    var cursor: Cursor? = null
    if (uri.scheme == "content") {
        try {
            cursor = activity.contentResolver.query(uri, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                var index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index >= 0) {
                    result = cursor.getString(index)
                }
            }
        } finally {
            cursor?.close()
        }
    }
    return result
}