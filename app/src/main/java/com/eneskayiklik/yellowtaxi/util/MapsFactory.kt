package com.eneskayiklik.yellowtaxi.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PixelFormat
import androidx.core.content.ContextCompat
import com.eneskayiklik.yellowtaxi.R

fun drawMarker(context: Context, text: String): Bitmap {
    val drawable = ContextCompat.getDrawable(context, R.drawable.ic_black_marker)!!
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    drawable.draw(canvas)
    val paint = Paint()
    paint.textSize = 50 * context.resources.displayMetrics.density / 2
    paint.style = Paint.Style.FILL
    val textCanvas = Canvas(bitmap)
    textCanvas.drawText(text, ((bitmap.width * 7) / 20).toFloat(), (bitmap.height / 2).toFloat(), paint)

    return bitmap
}