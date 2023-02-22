package com.moyee.android.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.moyee.android.R
import com.moyee.android.ui.theme.MoyeeTheme
import com.moyee.android.ui.theme.Purple700
import com.moyee.android.util.startSystemSettings

// 직접 접근하지 말고, BaseDialog를 구현한 MoyeeDialog를 사용할 것
@Composable
private fun MoyeeBaseDialog(
    onCloseRequest: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Dialog(onDismissRequest = { onCloseRequest?.invoke() }) {
        Surface(
            color = Color.Transparent,
            modifier = Modifier.width(500.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = Purple700,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp, 24.dp)
            ) {
                content.invoke()
            }
        }
    }
}

/**
 * 앱 다이얼로그
 *
 * @param onCloseRequest 다이얼로그 이외 배경을 클릭 시에 처리하는 이벤트 (ex: 다이얼로그 숨기기)
 * @param positiveButtonName 예/완료 등 메세지에 대한 긍정적인 버튼의 text 값
 * @param onPositiveButtonClick positiveButton의 클릭 이벤트
 * @param negativeButtonName 아니오/취소 등 메세지에 대한 부정적인 버튼의 text 값, 사용시에 이벤트 값도 함께 전달해야 함
 * @param onNegativeButtonClick negativeButton의 클릭 이벤트
 * @param title 다이얼로그 제목
 * @param message 다이얼로그 메세지
 * @param content 일반 메세지 이외 커스텀시 사용
 * */
@Composable
fun MoyeeDialog(
    onCloseRequest: (() -> Unit)? = null,
    positiveButtonName: String,
    onPositiveButtonClick: () -> Unit,
    negativeButtonName: String? = null,
    onNegativeButtonClick: (() -> Unit)? = null,
    title: String? = null,
    message: String? = null,
    content: (@Composable () -> Unit)? = null,
) {
    MoyeeBaseDialog(onCloseRequest = onCloseRequest) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            title?.let { Text(text = it) }

            message?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colors.onSecondary,
                    style = MaterialTheme.typography.body1
                )
            } ?: run { content?.invoke() }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                if (negativeButtonName != null && onNegativeButtonClick != null) {
                    MoyeeTextButton(onClick = onNegativeButtonClick, text = negativeButtonName)
                }

                Spacer(modifier = Modifier.weight(1f))
                MoyeeSubButton(onClick = onPositiveButtonClick, text = positiveButtonName)
            }
        }
    }
}

/**
 * 필수 권한 거부시 띄우는 다이얼로그
 *
 * @param permissionName 거부 당한 권한 이름
 * */
@Composable
fun PermissionDeniedDialog(
    onConfirm: () -> Unit,
    permissionName: String,
) {
    MoyeeDialog(
        positiveButtonName = stringResource(id = R.string.confirm),
        onPositiveButtonClick = onConfirm,
        message = stringResource(R.string.permission_denied_message, permissionName)
    )
}

/**
 * 권한 2번 거부/다시 묻지 않기 체크 후 거부시 띄우는 다이얼로그, 확인 버튼 클릭시 직접 안드로이드 설정창을 띄워 줌
 * */
@Composable
fun CheckPermissionInSystemSettingsDialog() {
    val context = LocalContext.current

    MoyeeDialog(
        positiveButtonName = stringResource(id = R.string.confirm),
        onPositiveButtonClick = { context.startSystemSettings() }
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.permission_system_settings_message),
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMoyeeDialog() {
    MoyeeTheme {
        MoyeeDialog(
            positiveButtonName = "화긴",
            onPositiveButtonClick = {},
            message = "대충 이런저런 메세지예요.dsfsdfdsfsadffsfdfs",
            negativeButtonName = "ㄴㄴ",
            onNegativeButtonClick = {}
        )
    }
}