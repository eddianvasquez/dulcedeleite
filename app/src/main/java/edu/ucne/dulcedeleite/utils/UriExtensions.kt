package edu.ucne.dulcedeleite.utils

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

fun Uri.toMultipartBodyPart(context: Context, paramName: String = "file"): MultipartBody.Part? {
    val contentResolver = context.contentResolver
    val tempFile = File(context.cacheDir, "${UUID.randomUUID()}.jpg")
    
    return try {
        val inputStream = contentResolver.openInputStream(this) ?: return null
        val outputStream = FileOutputStream(tempFile)
        inputStream.copyTo(outputStream)
        
        val requestFile = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        MultipartBody.Part.createFormData(paramName, tempFile.name, requestFile)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
