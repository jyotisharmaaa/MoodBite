package com.jyoti.moodbitenew.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface

object ImageUtils {

    fun getCorrectlyRotatedBitmap(path: String): Bitmap? {

        val bitmap = BitmapFactory.decodeFile(path) ?: return null

        val exif = ExifInterface(path)

        val rotation = when (
            exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
        ) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }

        if (rotation == 0f) {
            return bitmap
        }

        val matrix = Matrix().apply {
            postRotate(rotation)
        }

        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }
}