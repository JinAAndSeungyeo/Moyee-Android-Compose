package com.moyee.android.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat

/**
 * 권한 체크
 *
 * @param moyeePermission 체크할 권한
 * @return [String] 허용되지 않은 권한의 이름을 반환, 모두 허용되었다면 null
 * */
fun Context.checkPermissions(moyeePermission: MoyeePermission): String? =
    moyeePermission.run {
        permissions.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    this@checkPermissions,
                    permission
                ) == PackageManager.PERMISSION_DENIED
            ) permissionName
        }
        null
    }

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