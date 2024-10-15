package com.pzbdownloaders.redpdfpro.core.presentation.Component

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chaquo.python.Python
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DisplayMessageDialogBox(
    message: String,
    confirmTextButtonText: String,
    cancelTextButtonText: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    androidx.compose.material3.AlertDialog(
        onDismissRequest = { onDismiss() },
        shape = androidx.compose.material.MaterialTheme.shapes.medium.copy(
            topStart = CornerSize(15.dp),
            topEnd = CornerSize(15.dp),
            bottomStart = CornerSize(15.dp),
            bottomEnd = CornerSize(15.dp),
        ),
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.secondary,
        title = {
            Text(
                text = message,
                fontSize = 15.sp,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
            )
        }, confirmButton = {
            androidx.compose.material.Button(
                onClick = {

                    //showEmailWillBeSendAlertBox.value = false
                    onDismiss()


                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                    contentColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
                ),
                shape = androidx.compose.material.MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                )
            ) {
                androidx.compose.material.Text(
                    text = confirmTextButtonText,
                )
            }
        },
        dismissButton = {
            androidx.compose.material.OutlinedButton(
                onClick = {
                    onDismiss()
                },
                shape = androidx.compose.material.MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                ),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colors.onPrimary
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.secondary,
                    contentColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
                ),
            ) {
                androidx.compose.material.Text(
                    text = cancelTextButtonText,
                )
            }
        })
}
