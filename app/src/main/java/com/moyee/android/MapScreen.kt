package com.moyee.android

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.moyee.android.domain.UserLocation
import com.moyee.android.util.*

@Composable
fun UserMarker(
    onClick: (Marker) -> Boolean,
    userLocation: UserLocation,
    size: Dp,
) {
    val context = LocalContext.current
    var userImageBitmap by remember { mutableStateOf(context.getBitmap(R.drawable.ic_user_placeholder)) }

    // url 에서 이미지를 로딩 후 비트맵으로 변환
    Glide.with(context).asBitmap().load(userLocation.imageUrl)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                userImageBitmap = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })

    Marker(
        state = MarkerState(position = userLocation.location),
        onClick = onClick,
        icon = userLocationIcon(
            color = MaterialTheme.colors.primary,
            size = size,
            imageBitmap = userImageBitmap
        )
    )
}

/**
 * Bitmap에 user-location 아이콘(말풍선)을 그려서 반환하는 함수
 *
 * 자세한 아이콘 이미지는 피그마를 참고
 *
 * @param color
 * @param size
 * @param imageBitmap
 * */
private fun userLocationIcon(
    color: Color,
    size: Dp,
    imageBitmap: Bitmap,
): BitmapDescriptor {
    val smallCircleRadius = size.value.div(8)
    val smallCircleCenterPoint = size.value.minus(smallCircleRadius).minus(10)

    val imageSize = (size.value * 0.85f).dp

    // 이미지
    val circleUserImage = imageBitmap.resize(imageSize).clipToCircle()

    return BitmapHelper.init(size)
        .paintColor(color)
        .drawFullCircle()
        .drawCircle(smallCircleCenterPoint, smallCircleRadius)
        .drawBitmapInCenter(circleUserImage)
        .bitmap.toBitmapDescriptor()
}