package com.pzbdownloaders.redpdfpro.aifeature.textrecognitionfeature.presentation

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.mlkit.vision.documentscanner.GmsDocumentScanner
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity

@Composable
fun SaveAsDialogBox(saveAsDialogBox: MutableState<Boolean>, onDismiss: () -> Unit) {
    val context = LocalContext.current
    androidx.compose.material3.AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        shape = MaterialTheme.shapes.medium.copy(
            topStart = CornerSize(15.dp),
            topEnd = CornerSize(15.dp),
            bottomStart = CornerSize(15.dp),
            bottomEnd = CornerSize(15.dp),
        ),
        containerColor = MaterialTheme.colorScheme.primary,
        /*      icon = {
                     Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
              }*/

        title = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Select the language of document", fontSize = 12.sp)
                SaveCardView(
                    "Save as PDF",
                    onDismiss

                ) {
                    saveAsDialogBox.value = true
                }
                SaveCardView(
                    "Save as .docx",
                    onDismiss
                ) {

                }
            }
        },
        confirmButton = {

        },
        dismissButton = {

        }
    )
}

@Composable
fun SaveCardView(
    text: String,
    onDismiss: () -> Unit,
    onClick: () -> Unit,

    ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {

                onClick()
                onDismiss()
            },
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
        )
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 15.sp,
            modifier = Modifier.padding(10.dp)
        )
    }
}