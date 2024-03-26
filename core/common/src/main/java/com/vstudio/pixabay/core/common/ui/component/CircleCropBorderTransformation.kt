package com.vstudio.pixabay.core.common.ui.component

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import coil.size.Size
import coil.transform.Transformation

class CircleCropBorderTransformation(
    private val borderWidthPx: Float,
    private val borderColor: Color = Color.Transparent,
) : Transformation {

    override val cacheKey = "${javaClass.name}-borderWidth:$borderWidthPx-borderColor:$borderColor"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val diameter = input.width.coerceAtMost(input.height)
        val output = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = borderColor.toArgb()
            style = Paint.Style.STROKE
            strokeWidth = borderWidthPx
        }
        val radius = diameter / 2f
        val center = diameter / 2f
        val halfBorderWidth = borderWidthPx / 2f

        val path = Path().apply {
            addCircle(center, center, radius - halfBorderWidth, Path.Direction.CCW)
        }
        canvas.drawPath(path, paint)

        val bitmapShader = BitmapShader(input, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = bitmapShader
        }
        canvas.drawCircle(center, center, radius - halfBorderWidth, imagePaint)

        return output
    }
}