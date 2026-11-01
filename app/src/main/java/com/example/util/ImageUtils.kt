package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

object ImageUtils {

    fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun saveBitmapToFile(context: Context, bitmap: Bitmap, prefix: String = "gold_inspect"): String? {
        return try {
            val dir = File(context.filesDir, "inspections")
            if (!dir.exists()) dir.mkdirs()
            val fileName = "${prefix}_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(6)}.jpg"
            val file = File(dir, fileName)
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun bitmapToBase64(bitmap: Bitmap, quality: Int = 85, maxDimension: Int = 1024): String {
        val scaled = scaleDown(bitmap, maxDimension)
        val stream = ByteArrayOutputStream()
        scaled.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        val bytes = stream.toByteArray()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    private fun scaleDown(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        if (width <= maxDimension && height <= maxDimension) return bitmap

        val ratio = width.toFloat() / height.toFloat()
        val newWidth: Int
        val newHeight: Int
        if (width > height) {
            newWidth = maxDimension
            newHeight = (maxDimension / ratio).toInt()
        } else {
            newHeight = maxDimension
            newWidth = (maxDimension * ratio).toInt()
        }
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    data class ColorMetric(
        val goldWarmthIndex: Float, // 0..1
        val brightnessAvg: Float,
        val isYellowTone: Boolean
    )

    fun analyzeImageColors(bitmap: Bitmap): ColorMetric {
        val sample = Bitmap.createScaledBitmap(bitmap, 64, 64, false)
        var totalR = 0L
        var totalG = 0L
        var totalB = 0L
        val pixelCount = sample.width * sample.height

        for (x in 0 until sample.width) {
            for (y in 0 until sample.height) {
                val pixel = sample.getPixel(x, y)
                totalR += (pixel shr 16) and 0xFF
                totalG += (pixel shr 8) and 0xFF
                totalB += pixel and 0xFF
            }
        }

        val avgR = totalR.toFloat() / pixelCount
        val avgG = totalG.toFloat() / pixelCount
        val avgB = totalB.toFloat() / pixelCount

        val brightness = (avgR * 0.299f + avgG * 0.587f + avgB * 0.114f) / 255f
        // Gold has high red and green, lower blue: R > G > B
        val isYellowTone = avgR > 100 && avgG > 80 && avgR >= avgG && avgG > avgB * 1.2f
        val warmth = if (avgR + avgG + avgB > 0) {
            ((avgR + avgG) / (avgR + avgG + avgB * 2f)).coerceIn(0f, 1f)
        } else 0.5f

        return ColorMetric(
            goldWarmthIndex = warmth,
            brightnessAvg = brightness,
            isYellowTone = isYellowTone
        )
    }
}
