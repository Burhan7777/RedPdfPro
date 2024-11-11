package com.pzbdownloaders.redpdfpro.conversionsfeature.core.domain.util

import android.content.Context
import android.widget.Toast
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

suspend fun downloadMultipleFilesWithProgress(
    fileIds: List<Int>,
    outputDirectory: String,
    context: Context,
    viewModel: MyViewModel
    // onProgress: (fileIndex: Int, progress: Int) -> Unit,
    //onFileDownloaded: (fileIndex: Int, filePath: String) -> Unit
): String {
    val apiKey = "5c060a983bb5ab4f480ed985908b6f07d8c5e2ec"
    val client = OkHttpClient()


    for ((index, fileId) in fileIds.withIndex()) {
        val randomUUID = UUID.randomUUID()
        val fileName = "file_$fileId-${randomUUID}.jpg" // or use target_files[index]["name"]
        val filePath = "$outputDirectory/$fileName"

        val url = "https://api.zamzar.com/v1/files/$fileId/content"
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", okhttp3.Credentials.basic(apiKey, ""))
            .build()

        withContext(Dispatchers.IO) {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Download failed for file $fileName. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.listOfImagesFromExtractImages.add(filePath)
                    response.body?.let { body ->
                        writeToFileWithProgress(body, File(filePath)) { progress ->
                            //      onProgress(index, progress) // Update progress for each file
                        }
                        //   onFileDownloaded(index, filePath) // Notify when a file is downloaded
                    }
                }
            }
        }
    }
    return "Success"
}

private fun writeToFileWithProgress(
    body: ResponseBody,
    file: File,
    onProgress: (progress: Int) -> Unit
) {
    val contentLength = body.contentLength()
    val inputStream: InputStream = body.byteStream()
    val outputStream: OutputStream = FileOutputStream(file)
    val buffer = ByteArray(16 * 1024) // 16 KB buffer

    var totalBytesRead = 0L
    var bytesRead: Int

    inputStream.use { input ->
        outputStream.use { output ->
            while (input.read(buffer).also { bytesRead = it } != -1) {
                output.write(buffer, 0, bytesRead)
                totalBytesRead += bytesRead

                // Calculate progress as an integer percentage
                val progress = ((totalBytesRead * 100) / contentLength).toInt()
                onProgress(progress) // Invoke the lambda to update progress
            }
            output.flush()
        }
    }
}
