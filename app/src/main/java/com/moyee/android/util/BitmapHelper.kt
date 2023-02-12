package com.moyee.android.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp

// TODO: 각 과정에 로그 추가하기
/**
 * Canvas를 사용하여 Bitmap에 그림을 그릴 때 사용하는 인터페이스다. init 메서드를 통해 해당 구현체에 접근할 수 있다.
 * */
interface BitmapHelper {
    val size: Int
    val bitmap: Bitmap
    val canvas: Canvas
    val paint: Paint

    companion object {
        fun init(size: Dp) = createBitmapHelper(size)

        private fun createBitmapHelper(size: Dp) = object : BitmapHelper {
            override val size = size.value.toInt()
            override val bitmap = this.size.run {
                Bitmap.createBitmap(this, this, Bitmap.Config.ARGB_8888)
            }
            override val canvas = Canvas(bitmap)
            override val paint = Paint()

            init {
                paint.isAntiAlias = true
            }
        }
    }
}

/**
 * paint 변수의 color 값을 지정한다.
 *
 * @param color paint의 color 값
 * */
fun BitmapHelper.paintColor(color: Color) = this.apply {
    paint.color = color.toArgb()
}

/**
 * 캔버스 사이즈에 딱 맞는 원을 그린다.
 * */
fun BitmapHelper.drawFullCircle() = this.apply {
    val circleSize = canvas.width.div(2f)
    canvas.drawCircle(circleSize, circleSize, circleSize, paint)
}

/**
 * 중심점을 기준으로 원을 그린다.
 *
 * @param pointPosition 원의 중심점의 위치(좌표값)
 * @param radius 반지름
 * */
fun BitmapHelper.drawCircle(pointPosition: Float, radius: Float) =
    this.apply { canvas.drawCircle(pointPosition, pointPosition, radius, paint) }

/**
 * 캔버스 중심에 인자로 전달된 비트맵을 그린다.
 *
 * @param bitmap 그릴 비트맵
 * */
fun BitmapHelper.drawBitmapInCenter(bitmap: Bitmap) = this.apply {
    val start = canvas.width.minus(bitmap.width).div(2f)
    canvas.drawBitmap(bitmap, start, start, null)
}