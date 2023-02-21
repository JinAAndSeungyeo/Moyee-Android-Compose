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

enum class MoyeePermission(val permissionName: String, val permissions: List<String>) {
    LOCATION(
        "위치",
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
}