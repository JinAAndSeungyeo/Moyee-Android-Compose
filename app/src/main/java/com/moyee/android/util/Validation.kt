package com.moyee.android.util

import android.content.Context
import com.moyee.android.R

/**
 * 유효성 검사를 위한 클래스
 *
 * @param context
 * @param value 유효성 검사를 진행할 값
 * @param valueName 값의 이름
 * @param minSize 값의 최소 길이
 * @param maxSize 값의 최대 길이
 * @param regex
 * */
class Validation(
    private val context: Context,
    private val value: String,
    private val valueName: String,
    private val minSize: Int,
    private val maxSize: Int,
    private val regex: Regex,
) {

    private val length = value.length

    fun checkNotEmpty(): Validation = { value.isNotEmpty() }.checkOrThrows(
        context.getString(R.string.error_input_blank, valueName)
    )

    fun checkInvalidSize(): Validation = { length in minSize..maxSize }.checkOrThrows(
        context.getString(
            R.string.error_input_size,
            valueName,
            minSize,
            maxSize
        )
    )

    fun checkInvalid(errorMessage: String): Validation =
        { regex.matches(value) }.checkOrThrows(errorMessage)

    // 유효성이 올바르지 않으면 파리미터로 넘겨받은 errorMessage를 매핑한 IllegalArgumentException을 던짐
    private fun (() -> Boolean).checkOrThrows(errorMessage: String) =
        if (invoke()) this@Validation else throw IllegalArgumentException(errorMessage)
}

/**
 * 유효성 검사 후 결과 값을 래핑한 sealed class
 * */
sealed class ValidationResult {
    object Success : ValidationResult()
    data class NotAvailable(val errorMessage: String) : ValidationResult()
}

/**
 * 전달받은 ValidationResult가 NotAvailable일 때 errorMessage를 반환함,
 * Success일 때는 null 값을 반환함
 * */
fun ValidationResult.getErrorMessageOrNull(): String? =
    (this as? ValidationResult.NotAvailable)?.errorMessage

/**
 * 파라미터로 전달받은 block을 샐행 후,
 * 예외가 발생하지 않았으면 Success를 반환하고, 발생했다면 NotAvailable를 반환함
 * */
inline fun <T> T.execute(
    crossinline block: T.() -> Validation
) = try {
    block()
    ValidationResult.Success
} catch (e: IllegalArgumentException) {
    ValidationResult.NotAvailable(e.message ?: "No error message.")
}