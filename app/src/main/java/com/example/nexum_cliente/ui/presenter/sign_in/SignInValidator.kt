package com.example.nexum_cliente.ui.presenter.sign_in

import com.example.nexum_cliente.ui.common.ValidationResult


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 7/31/2025
 * @version 1.0
 */
object SignInValidator {
    private const val MIN_EMAIL_LENGTH = 3
    private const val MAX_EMAIL_LENGTH = 254
    private const val MIN_PASSWORD_LENGTH = 5
    private const val MIN_USER_LENGTH = 4
    private const val MAX_USER_LENGTH = 20
    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val usernameRegex = Regex("^[A-Za-z0-9._]+$")

    fun validateEmailOrUsername(input: String?): ValidationResult {
        if (input.isNullOrBlank()) {
            return ValidationResult(false, "Email cannot be empty.")
        }

        val isEmail = input.contains("@") && input.substringAfter("@").contains(".")

        return if (isEmail) {
            when {
                input.length < MIN_EMAIL_LENGTH -> ValidationResult(false, "Email is too short.")
                input.length > MAX_EMAIL_LENGTH -> ValidationResult(false, "Email is too long.")
                !emailRegex.matches(input) -> ValidationResult(false, "Invalid email format")
                else -> ValidationResult(true)
            }
        } else {
            when {
                !usernameRegex.matches(input) -> ValidationResult(false, "Invalid username format")
                input.length < MIN_USER_LENGTH -> ValidationResult(
                    false,
                    "Username too short (min $MIN_USER_LENGTH)."
                )

                input.length > MAX_USER_LENGTH -> ValidationResult(
                    false,
                    "Username too long (max $MAX_USER_LENGTH)."
                )

                else -> ValidationResult(true)
            }
        }
    }

    fun validatePassword(password: String?): ValidationResult {
        if (password.isNullOrBlank()) {
            return ValidationResult(false, "Password cannot be empty.")
        }
        return if (password.length >= MIN_PASSWORD_LENGTH) {
            ValidationResult(true)
        } else {
            ValidationResult(false, "Password must be at least $MIN_PASSWORD_LENGTH characters.")
        }
    }
}

