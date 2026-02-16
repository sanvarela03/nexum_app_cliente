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
    fun validateEmail(email: String?): ValidationResult {


        if (email.isNullOrEmpty()) {
            return ValidationResult(false, "Email cannot be empty.")
        }
        if (email.contains("@") && email.contains(".")) {

            val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")


            val isValid = emailRegex.matches(email) && email.length >= 4 && email.length <= 20

            val errorMessage = when {
                !emailRegex.matches(email) -> "Invalid email format."
                email.length < 4 -> "Email is too short (minimum 4 characters)."
                email.length > 20 -> "Email is too long (maximum 19 characters)."
                else -> ""
            }

            return ValidationResult(isValid, errorMessage)
        }

        return ValidationResult(email.length >= 4 && email.length <= 20, "Invalid email format.")
    }

    fun validatePassword(password: String?): ValidationResult = ValidationResult(
        (!password.isNullOrEmpty() && password.length >= 4)
    )
}

