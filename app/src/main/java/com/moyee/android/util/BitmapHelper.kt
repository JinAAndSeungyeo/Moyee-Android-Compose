package com.moyee.android.util

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

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

/**
 * 인자 값으로 들어온 Drawable Resource ID를 BitmapDescriptor로 변환한다.
 * 반환된 BitmapDescriptor는 Google Map Marker 커스텀시 사용된다.
 *
 * @param id Drawable Resource ID
 * @return [BitmapDescriptor]
 * */
fun Context.getBitmapDescriptor(@DrawableRes id: Int): BitmapDescriptor =
    BitmapDescriptorFactory.fromBitmap(getBitmap(id))

/**
 * Drawable Resource ID를 Bitmap으로 변환한다. 부합하는 Resource ID가 없으면 NotFoundException을 던진다.
 *
 * @param id Drawable Resource ID
 * @return [Bitmap]
 * */
fun Context.getBitmap(@DrawableRes id: Int): Bitmap =
    ContextCompat.getDrawable(this, id)?.run { toBitmap(intrinsicWidth, intrinsicHeight) }
        ?: throw Resources.NotFoundException("유효하지 않은 resource ID 입니다.")

/**
 * Bitmap을 BitmapDescriptor로 변환한다.
 *
 * @return [BitmapDescriptor]
 * */
fun Bitmap.toBitmapDescriptor() = BitmapDescriptorFactory.fromBitmap(this)

/**
 * 비트맵을 원형으로 자르는 함수로, 주로 이미지 Bitmap을 원형으로 자를 때 사용한다.
 * 이미지의 크기가 캔버스의 크기보다 크다면 캔버스의 크기만큼 이미지를 축소하여 자른다.
 *
 * 이미지의 비율을 유지하지 않고 항상 1:1 비율이니 꼭 주의하기!!
 *
 * @return [Bitmap] 원형으로 자른 후 비트맵
 * */
fun Bitmap.clipToCircle() =
    BitmapHelper.init(width.dp)
        .createCircleMask()
        .intoImage(this)
        .bitmap

/**
 * Bitmap의 사이즈를 조정해서 다시 반환
 *
 * @param size 조절할 크기
 * @return [Bitmap] 사이즈 조정 후 다시 반환
 * */
fun Bitmap.resize(size: Dp): Bitmap = with(size.value.toInt()) {
    Bitmap.createScaledBitmap(this@resize, this, this, true)
} // TODO: 프로필 사진 보내줄 때 꼭 1:1 비율로 서버로 보내야 됨!! 위 메서드에서는 비율 조절까지는 구현하지 않음

private fun BitmapHelper.createCircleMask() =
    size.div(2f).run {
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(this, this, this, paint)
        this@createCircleMask
    }

private fun BitmapHelper.intoImage(
    imageBitmap: Bitmap
) = this.apply {
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    val rect = Rect(0, 0, size, size)
    canvas.drawBitmap(imageBitmap, rect, rect, paint)
}