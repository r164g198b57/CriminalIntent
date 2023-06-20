package com.bignerdranch.android.criminalintent

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager

fun getScaledBitmap(path: String, activity: Activity): Bitmap {
    val size = Point()
    val wm = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = wm.currentWindowMetrics
        val windowInsets: WindowInsets = windowMetrics.windowInsets
        val insets = windowInsets.getInsetsIgnoringVisibility(
            WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout()
        )
        val insetsWidth = insets.right + insets.left
        val insetsHeight = insets.top + insets.bottom
        val b = windowMetrics.bounds
        size.x = b.width() - insetsWidth
        size.y = b.height() - insetsHeight
    } else {
        val display = wm.defaultDisplay
        display?.getSize(size)
    }
    return getScaledBitmap(path, size.x, size.y)
}


fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {

    var options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, options)

    val srcWidth = options.outWidth.toFloat()
    val srcHeight = options.outHeight.toFloat()

    var inSampleSize = 1
    if (srcHeight > destHeight || srcWidth > destWidth) {
        val heightScale = srcHeight / destHeight
        val widthScale = srcWidth / destWidth

        val sampleScale = if (heightScale > widthScale) {
            heightScale
        } else {
            widthScale
        }
        inSampleSize = Math.round(sampleScale)
    }

    options = BitmapFactory.Options()
    options.inSampleSize = inSampleSize

    return BitmapFactory.decodeFile(path, options)
}