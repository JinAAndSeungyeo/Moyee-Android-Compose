package com.moyee.android.util

import androidx.compose.runtime.Composable

/**
 * T 값이 null이 아니면 파라미터로 받은 composable function을 실행하고, null이면 null을 반환함
 * */
@Composable
inline fun <T> T.executeOrNull(
    crossinline e: @Composable (T) -> Unit
): (@Composable () -> Unit)? = this?.let { data -> { e.invoke(data) } }