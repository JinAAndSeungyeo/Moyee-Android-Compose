package com.moyee.android.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat

/**
 * 다중 권한 체크
 *
 * @param permissions 체크할 권한들
 * @return [Boolean] 권한 허용 여부
 * */
fun Context.checkPermissions(permissions: List<String>): Boolean =
    permissions.all { permission -> checkPermission(permission) }

/**
 * 단일 권한 체크
 *
 * @param permission 체크할 권한
 * @return [Boolean] 권한 허용 여부
 * */
fun Context.checkPermission(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED

/**
 * 안드로이드 시스템 설정 앱을 실행함
 * */
fun Context.startSystemSettings() {
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${packageName}")).apply {
        addCategory(Intent.CATEGORY_DEFAULT)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(this)
    }
}

/**
 * 권한 허락 여부에 따른 분기 이벤트 처리를 하는 함수로,
 * 권한을 전부 허용했을 때, 첫 번째 거부, 두 번째 거부시로 나누어져 각각의 이벤트를 처리함
 *
 * @param onAllPermissionGranted 모든 권한을 허용했을 때 실행되는 이벤트
 * @param onFirstDenied 권한을 첫 번째 거부했을 때 실행되는 이벤트, 이벤트를 설정하지 않으면 기본 다이얼로그를 띄움
 * @param onSecondDenied 권한을 두 번째 거부했을 때 실행되는 이벤트, 이벤트를 설정하지 않으면 기본 다이얼로그를 띄움
 * @param permissionName 확인할 권한의 이름
 * */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MultiplePermissionsState.ProcessPermissionRequest(
    onAllPermissionGranted: @Composable () -> Unit,
    onFirstDenied: (@Composable () -> Unit)? = null,
    onSecondDenied: (@Composable () -> Unit)? = null,
    permissionName: String,
) {
    when {
        allPermissionsGranted -> onAllPermissionGranted.invoke()

        shouldShowRationale -> onFirstDenied?.let {
            PermissionDeniedDialog(
                onConfirm = { launchMultiplePermissionRequest() },
                permissionName = permissionName
            )
        }

        !allPermissionsGranted && !shouldShowRationale ->
            onSecondDenied?.let { CheckPermissionInSystemSettingsDialog() }
    }
}

enum class MoyeePermission(val permissionName: String, val permissions: List<String>) {
    FOREGROUND_LOCATION(
        "위치",
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
}