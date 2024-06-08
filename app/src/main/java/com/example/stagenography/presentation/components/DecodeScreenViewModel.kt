package com.example.stagenography.presentation.components

import com.example.stagenography.data.SteganographyUtils
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream

class DecodeScreenViewModel : ViewModel() {
    val imageBitmap = mutableStateOf<Bitmap?>(null)
    val decodedMessage = mutableStateOf("")

    fun onImageSelected(inputStream: InputStream) {
        val bitmap = BitmapFactory.decodeStream(inputStream)
        imageBitmap.value = bitmap
    }

    fun onDecodeClick() {
        val bitmap = imageBitmap.value ?: return

        viewModelScope.launch(Dispatchers.Default) {
            val pixels = IntArray(bitmap.width * bitmap.height)
            bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

            val message = SteganographyUtils.decode(pixels)
            if (message != null) {
                decodedMessage.value = message
            }else{
                decodedMessage.value = "No message found";
            }
        }
    }
}
