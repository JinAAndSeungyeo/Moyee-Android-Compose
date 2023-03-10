package com.moyee.android.domain.repository

import android.content.Context
import com.moyee.android.R
import com.moyee.android.util.Validation
import com.moyee.android.util.ValidationResult
import com.moyee.android.util.execute

class ValidationRepositoryImpl(private val context: Context) : ValidationRepository {

    override fun isIdAvailable(id: String): ValidationResult =
        Validation(
            context = context,
            value = id,
            valueName = context.getString(R.string.id),
            minSize = 5,
            maxSize = 15,
            regex = "^[A-Za-z\\d]{5,15}\$".toRegex()
        ).execute {
            checkNotEmpty()
            checkInvalidSize()
            checkInvalid("")
        }

    override fun isPasswordAvailable(password: String): ValidationResult =
        Validation(
            context = context,
            value = password,
            valueName = context.getString(R.string.password),
            minSize = 5,
            maxSize = 15,
            regex = "^[A-Za-z\\d]{5,15}\$".toRegex()
        ).execute {
            checkNotEmpty()
            checkInvalidSize()
            checkInvalid("비밀번호는 영문과 숫자, 특수 문자를 모두 포함해야 합니다.")
        }

    override fun isNicknameAvailable(nickname: String): ValidationResult =
        Validation(
            context = context,
            value = nickname,
            valueName = context.getString(R.string.nickname),
            minSize = 1,
            maxSize = 10,
            regex = "^[A-Za-zㄱ-ㅎ가-힣\\d]{1,10}\$".toRegex()
        ).execute {
            checkNotEmpty()
            checkInvalidSize()
            checkInvalid("닉네임은 한글과 영어, 숫자만 입력 가능합니다.")
        }

    override fun isNameAvailable(name: String): ValidationResult =
        Validation(
            context = context,
            value = name,
            valueName = context.getString(R.string.name),
            minSize = 2,
            maxSize = 5,
            regex = "^[ㄱ-ㅎ가-힣]{2,5}\$".toRegex()
        ).execute {
            checkNotEmpty()
            checkInvalidSize()
            checkInvalid("이름은 한글만 입력 가능합니다.")
        }
}