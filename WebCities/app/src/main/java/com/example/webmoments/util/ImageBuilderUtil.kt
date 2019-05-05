package com.example.webmoments.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory

object ImageBuilderUtil {
    fun prepare(urlpath: String, width: Int = 200, height: Int = 200): Bitmap {
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(urlpath, bmOptions)
        val bitmapWidth = bmOptions.outWidth
        val bitoutHeight = bmOptions.outHeight
        val scaleFactor = Math.min(bitmapWidth / width, bitoutHeight / height)

        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor

        return BitmapFactory.decodeFile(urlpath, bmOptions)
    }
}