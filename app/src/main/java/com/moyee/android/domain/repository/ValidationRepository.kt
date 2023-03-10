package com.moyee.android.domain.repository

import com.moyee.android.util.ValidationResult

interface ValidationRepository {

    fun isIdAvailable(id: String): ValidationResult

    fun isPasswordAvailable(password: String): ValidationResult

    fun isNicknameAvailable(nickname: String): ValidationResult

    fun isNameAvailable(name: String): ValidationResult
}