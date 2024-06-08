package com.example.stagenography.data

import android.util.Log

object SteganographyUtils {

    private const val MAX_DECODABLE_LENGTH = 0x00FFFFFF
    private const val LSB = 1

    fun encode(pixels: IntArray, message: String): IntArray {
        Log.d("Steganography.Encode", "Encode Begin")
        val data = message.toByteArray()

        val dataWithLength = ByteArray(4 + data.size)
        var dataLength = data.size
        for (i in 0 until 4) {
            dataWithLength[i] = (dataLength and 0xff).toByte()
            dataLength = dataLength ushr 8
        }

        System.arraycopy(data, 0, dataWithLength, 4, data.size)

        var pixelIndex = 0

        for (b in dataWithLength) {
            var temp = b.toInt()
            for (i in 0 until 8) {
                pixels[pixelIndex] = pixels[pixelIndex] and (LSB.inv())
                pixels[pixelIndex] = pixels[pixelIndex] or (temp and LSB)
                temp = temp ushr 1
                pixelIndex++
            }
        }

        Log.d("Steganography.Encode", "Encode End")

        return pixels
    }

    fun decode(pixels: IntArray): String? {
        Log.d("Steganography.Decode", "Decode Begin")

        return try {
            var pixelIndex = 0

            val length = decodeBitsFromPixels(pixels, 32, pixelIndex)
            pixelIndex += 32

            if (length < 0 || length > MAX_DECODABLE_LENGTH) {
                throw IllegalArgumentException("Failed to decode. Are you sure the image is encoded?")
            }

            val data = ByteArray(length)

            var byteIndex = 0
            while (byteIndex < length) {
                data[byteIndex] = decodeBitsFromPixels(pixels, 8, pixelIndex).toByte()
                byteIndex++
                pixelIndex += 8
            }

            Log.d("Steganography.Decode", "Decode End")
            String(data)
        } catch (e: Exception) {
            Log.e("Steganography.Decode", "Decoding failed", e)
            null
        }
    }
    private fun decodeBitsFromPixels(pixels: IntArray, numberOfBits: Int, pixelIndex: Int): Int {
        var decodedValue = 0

        for (i in 0 until numberOfBits) {
            val bit = pixels[pixelIndex + i] and LSB
            decodedValue = decodedValue or (bit shl i)
        }

        return decodedValue
    }
}
