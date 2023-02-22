package com.moyee.android

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*
import com.moyee.android.component.MoyeeDialog
import com.moyee.android.domain.UserLocation
import com.moyee.android.util.*
import timber.log.Timber

private const val LOCATION_ZOOM = 10f
private val DEFAULT_LOCATION = LatLng(37.0, 126.0)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen() {
    val context = LocalContext.current
    val permissionsState =
        rememberMultiplePermissionsState(permissions = MoyeePermission.FOREGROUND_LOCATION.permissions)
    var isGrantedForegroundLocationPermission by remember { mutableStateOf(true) }

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                isGrantedForegroundLocationPermission =
                    context.checkPermissions(MoyeePermission.FOREGROUND_LOCATION.permissions)
                Timber.d("isGrantedForegroundLocationPermission=$isGrantedForegroundLocationPermission")
            }

            else -> {}
        }
    }

    if (isGrantedForegroundLocationPermission) {
        // TODO: 내 위치 값 조회하기
    } else {
        ForegroundLocationPermissionCheckDialog(
            launchForegroundLocationPermissionRequest = { permissionsState.launchMultiplePermissionRequest() }
        )
    }

    permissionsState.ProcessPermissionRequest(
        onAllPermissionGranted = {
            context.checkPermission(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION).let {isGrantedBackgroundLocationPermission ->
                Timber.d("isGrantedBackgroundLocationPermission=$isGrantedBackgroundLocationPermission")
                if (!isGrantedBackgroundLocationPermission)
                    BackgroundLocationPermissionCheckDialog()
            }
        },
        permissionName = MoyeePermission.FOREGROUND_LOCATION.name
    )

    MapContent(
        onMapLoaded = { },
        myLocation = DEFAULT_LOCATION,
        otherUsers = emptyList()
    )
}

@Composable
fun MapContent(
    onMapLoaded: () -> Unit,
    myLocation: LatLng,
    otherUsers: List<UserLocation>,
) {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(myLocation, LOCATION_ZOOM)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapLoaded = onMapLoaded,
        uiSettings = MapUiSettings(myLocationButtonEnabled = true)
    ) {
        UserMarker(
            onClick = { true },
            userLocation = UserLocation(location = myLocation, imageUrl = "https://w.namu.la/s/a5bc19d3232508b1f141def5e3588dd3521ba3aa33ea7cad6caf79c3dbc3c2c64533fa32e669a7e486a1d2af7aab32d4c7e64e3e3a63894e02eeb1a5c9e2b120d020eb77054ffe49caa5d10e6c07a359306ddd0c9dd8631f36a6dfbe3802f3ec"),
            size = 300.dp
        )

        otherUsers.forEach { userLocation ->
            UserMarker(
                onClick = {
                    // TODO: UserInfo 띄워주기, 새 UI 작성
                    Toast.makeText(context, "I'm user${userLocation.id}!", Toast.LENGTH_SHORT)
                        .show()
                    true
                },
                userLocation = userLocation,
                size = 300.dp
            )
        }
    }
}

@Composable
fun UserMarker(
    onClick: (Marker) -> Boolean,
    userLocation: UserLocation,
    size: Dp,
) {
    val context = LocalContext.current
    var userImageBitmap by remember { mutableStateOf(context.getBitmap(R.drawable.ic_user_placeholder)) }

    context.loadImage(
        onResourceRoad = { resource -> userImageBitmap = resource },
        imageUrl = userLocation.imageUrl
    )

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
 * url에서 이미지를 로딩하고, 그 이미지를 비트맵으로 변환함
 *
 * @param onResourceRoad 반환할 비트맵 이벤트
 * @param imageUrl 로딩할 이미지 url
 * */
fun Context.loadImage(
    onResourceRoad: (resource: Bitmap) -> Unit,
    imageUrl: String,
) {
    Timber.d("imageUrl=$imageUrl")

    Glide.with(this).asBitmap().load(imageUrl).into(object : CustomTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            onResourceRoad.invoke(resource)
        }

        override fun onLoadCleared(placeholder: Drawable?) {}
    })
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

@Composable
private fun ForegroundLocationPermissionCheckDialog(
    launchForegroundLocationPermissionRequest: () -> Unit,
) {
    MoyeeDialog(
        positiveButtonName = stringResource(R.string.confirm),
        onPositiveButtonClick = { launchForegroundLocationPermissionRequest.invoke() },
        message = stringResource(R.string.foreground_location_permission_message)
    )
}

@Composable
private fun BackgroundLocationPermissionCheckDialog() {
    val context = LocalContext.current
    MoyeeDialog(
        positiveButtonName = stringResource(R.string.background_location_permission_button_name),
        onPositiveButtonClick = { context.startSystemSettings() },
        message = stringResource(R.string.background_location_permission_message)
    )
}