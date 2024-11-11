import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

suspend fun downloadFileWithProgress(
    fileId: Int,
    filePath: String,
    context: Context,
    onProgress: (progress: Int) -> Unit
): String {
    val apiKey = "5c060a983bb5ab4f480ed985908b6f07d8c5e2ec"
    val client = OkHttpClient()
    val url = "https://api.zamzar.com/v1/files/$fileId/content"
    val request = Request.Builder()
        .url(url)
        .addHeader("Authorization", okhttp3.Credentials.basic(apiKey, ""))
        .build()

    return withContext(Dispatchers.IO) {
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) Toast.makeText(
                context,
                "Downloaded Failed. Please try again.",
                Toast.LENGTH_SHORT
            ).show()

            val file = File(filePath)
            response.body?.let { body ->
                writeToFileWithProgress(body, file, onProgress)
            }
        }
        "Success"
    }
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
                onProgress(progress)  // Invoke the lambda to update progress
            }
            output.flush()
        }
    }
}
