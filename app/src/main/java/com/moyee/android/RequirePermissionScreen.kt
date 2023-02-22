package com.moyee.android

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.moyee.android.component.MoyeeMainButton
import com.moyee.android.ui.theme.MoyeeTheme
import com.moyee.android.util.ProcessPermissionRequest
import com.moyee.android.util.MoyeePermission

/**
 * 필수 권한 고지 화면
 * */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequirePermissionScreen(
    moyeePermission: MoyeePermission,
    onAllPermissionsGranted: () -> Unit,
) {
    val permissionsState = rememberMultiplePermissionsState(permissions = moyeePermission.permissions)

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(48.dp)
            .fillMaxHeight()
            .fillMaxWidth(),
    ) {
        Column {
            PermissionHeader()
            HorizontalBreakLine()
            PermissionContent()
        }

        MoyeeMainButton(
            onClick = { permissionsState.launchMultiplePermissionRequest() },
            text = stringResource(R.string.okay)
        )
    }

    permissionsState.ProcessPermissionRequest(
        onAllPermissionGranted = { onAllPermissionsGranted.invoke() },
        permissionName = moyeePermission.permissionName
    )
}

// 중단선
@Composable
fun HorizontalBreakLine(
    border: Dp = 1.dp,
    padding: Dp = 16.dp,
    color: Color = MaterialTheme.colors.onSurface,
) {
    Spacer(
        modifier = Modifier
            .height(border)
            .padding(0.dp, padding)
            .background(color)
    )
}

@Composable
private fun PermissionHeader() {
    Text(
        text = stringResource(R.string.app_permission_access),
        style = MaterialTheme.typography.h5,
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = stringResource(R.string.app_permission_access_description),
        style = MaterialTheme.typography.body1,
        modifier = Modifier.padding(start = 16.dp)
    )
}

@Composable
private fun PermissionContent() {
    Column {
        Text(
            text = stringResource(R.string.require_permission),
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.body1,
        )
        PermissionList()
    }
}

@Composable
private fun PermissionList() {
    listOf(
        Pair(R.string.location_info, R.string.location_info_permission_description),
        Pair(R.string.notification, R.string.notification_permission_description),
        Pair(R.string.photo, R.string.photo_permission_description)
    ).forEach { (permissionName, description) ->
        PermissionItem(
            permissionNameResourceId = permissionName,
            descriptionResourceId = description
        )
    }
}

@Composable
private fun PermissionItem(
    @StringRes permissionNameResourceId: Int,
    @StringRes descriptionResourceId: Int,
) {
    Column {
        Text(
            text = "· ${stringResource(id = permissionNameResourceId)}",
            style = MaterialTheme.typography.body1
        )
        Text(
            text = stringResource(id = descriptionResourceId),
            style = MaterialTheme.typography.caption,
        )
    }
}

@Composable
@Preview
private fun PreviewPermissionList() {
    MoyeeTheme {
        PreviewPermissionList()
    }
}

@Composable
@Preview
private fun PreviewPermissionItem() {
    MoyeeTheme {
        PermissionItem(
            permissionNameResourceId = R.string.photo,
            descriptionResourceId = R.string.photo_permission_description
        )
    }
}