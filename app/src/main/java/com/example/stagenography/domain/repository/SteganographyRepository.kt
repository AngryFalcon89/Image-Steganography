package com.example.stagenography.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import com.example.stagenography.domain.model.SteganographyParams


interface SteganographyRepository {
    suspend fun encodeImage(filePath: String, message: String): SteganographyParams
    suspend fun decodeImage(filePath: String): SteganographyParams
    suspend fun loadImage(uri: Uri): Bitmap?
}
