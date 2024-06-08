package com.example.stagenography.domain.model

import android.net.Uri

data class SteganographyParams(
    val filePath: String,
    val resultUri: Uri?,
    val message: String,
    val type: Type
) {
    enum class Type {
        IMAGE_LOADED,
        ENCODE_SUCCESS,
        DECODE_SUCCESS,
        FAILURE
    }
}