package com.example.stagenography.presentation.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stagenography.data.SteganographyUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class EncodeScreenViewModel : ViewModel() {
    val imageBitmap = mutableStateOf<Bitmap?>(null)
    val encodedBitmap = mutableStateOf<Bitmap?>(null)
    val message = mutableStateOf("")

    fun onImageSelected(inputStream: InputStream) {
        val bitmap = BitmapFactory.decodeStream(inputStream)
        imageBitmap.value = bitmap
    }

    fun onMessageChanged(newMessage: String) {
        message.value = newMessage
    }

    fun onEncodeClick() {
        val bitmap = imageBitmap.value ?: return
        val message = message.value

        viewModelScope.launch(Dispatchers.Default) {
            val pixels = IntArray(bitmap.width * bitmap.height)
            bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

            val encodedPixels = SteganographyUtils.encode(pixels, message)
            val encodedBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
            encodedBitmap.setPixels(encodedPixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

            this@EncodeScreenViewModel.encodedBitmap.value = encodedBitmap
        }
    }

    fun saveEncodedImage(file: File) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bitmap = encodedBitmap.value ?: return@launch
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                }
                Log.d("EncodeScreenViewModel", "Image saved successfully")
            } catch (e: Exception) {
                Log.e("EncodeScreenViewModel", "Error saving image", e)
            }
        }
    }
}
