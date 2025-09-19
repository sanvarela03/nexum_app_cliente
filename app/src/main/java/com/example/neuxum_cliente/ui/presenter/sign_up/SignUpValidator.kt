package com.example.neuxum_cliente.ui.presenter.sign_up

import android.net.Uri
import com.example.neuxum_cliente.ui.common.ValidationResult

object SignUpValidator {
    fun validateEmail(email: String?): ValidationResult {
        if (email.isNullOrEmpty()) {
            return ValidationResult(false, "Email cannot be empty.")
        }

        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

        val isValid = emailRegex.matches(email) && email.length >= 4 && email.length < 20

        val errorMessage = when {
            !emailRegex.matches(email) -> "Invalid email format."
            email.length < 4 -> "Email is too short (minimum 4 characters)."
            email.length >= 20 -> "Email is too long (maximum 19 characters)."
            else -> ""
        }

        return ValidationResult(isValid, errorMessage)
    }

    fun validateName(name: String?): ValidationResult = ValidationResult(
        !name.isNullOrEmpty() && name.length >= 4 && name.length < 20
    )

    fun validateLastName(lastName: String?): ValidationResult = ValidationResult(
        !lastName.isNullOrEmpty() && lastName.length >= 4 && lastName.length < 20
    )

    fun validatePhoneCode(phoneCode: String?): ValidationResult = ValidationResult(
        !phoneCode.isNullOrEmpty()
    )

    private val rules: Map<String, Regex> = mapOf(
        // Colombia: 10 digits, mobiles start with 3
        "🇨🇴 +57" to Regex("""^3\d{9}$"""),
        // México: 10 digits (since 2019, nationwide)
        "🇲🇽 +52" to Regex("""^\d{10}$"""),
        // Chile: 9 digits, mobiles start with 9
        "🇨🇱 +56" to Regex("""^9\d{8}$""")
    )

    fun validateCellphone(phone: String?, phoneCode: String): ValidationResult {
        if (phone.isNullOrBlank()) return ValidationResult(false, "El número no puede estar vacío")
        val digits = phone.filter { it.isDigit() }

        val rule = rules[phoneCode] ?: return ValidationResult(false, "Código de país no permitido")
        if (!rule.matches(digits)) {
            val msg = when (phoneCode) {
                "🇨🇴 +57" -> "Colombia: debe ser móvil que empiece en 3 y tenga 10 dígitos"
                "🇲🇽 +52" -> "México: debe tener 10 dígitos"
                "🇨🇱 +56" -> "Chile: debe ser móvil que empiece en 9 y tenga 9 dígitos"
                else  -> "Formato no válido"
            }
            return ValidationResult(false, msg)
        }
        return ValidationResult(true)
    }

    fun validateBirthDate(birthDate: String?): ValidationResult = ValidationResult(
        !birthDate.isNullOrEmpty()
    )

    fun validateCity(city: String?): ValidationResult = ValidationResult(
        !city.isNullOrEmpty()
    )

    fun validateDocumentNumber(documentNumber: String?): ValidationResult {
        val isValid = !documentNumber.isNullOrEmpty() &&
                documentNumber.all { it.isDigit() } &&
                documentNumber.length in 6..10

        return ValidationResult(isValid)
    }

    fun validateFrontDocumentUri(frontDocumentUri: Uri?): ValidationResult = ValidationResult(
        !frontDocumentUri.toString().isEmpty()
    )

    fun validateBackDocumentUri(backDocumentUri: Uri?): ValidationResult = ValidationResult(
        !backDocumentUri.toString().isEmpty()
    )

    fun validateFrontDocumentUrl(frontDocumentUrl: String?): ValidationResult = ValidationResult(
        !frontDocumentUrl.isNullOrEmpty()
    )

    fun validateBackDocumentUrl(backDocumentUrl: String?): ValidationResult = ValidationResult(
        !backDocumentUrl.isNullOrEmpty()
    )

    fun validatePassword(password: String?): ValidationResult = ValidationResult(
        (!password.isNullOrEmpty() && password.length >= 4)
    )

    fun validateConfirmPassword(confirmPassword: String?, password: String?): ValidationResult = ValidationResult(
        confirmPassword == password
    )

    fun validateProfilePictureUri(profilePictureUri: Uri?): ValidationResult = ValidationResult(
        !profilePictureUri.toString().isEmpty()
    )

    fun validateProfilePictureUrl(profilePictureUrl: String?): ValidationResult = ValidationResult(
        !profilePictureUrl.isNullOrEmpty()
    )


}