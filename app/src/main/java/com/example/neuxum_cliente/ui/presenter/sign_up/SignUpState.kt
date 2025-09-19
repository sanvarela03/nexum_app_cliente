package com.example.neuxum_cliente.ui.presenter.sign_up

import android.net.Uri

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 05/08/2025
 * @version 1.0
 */
data class SignUpState(
    var email: String = "",
    var name: String = "",
    var lastName: String = "",
    var phoneCode: String = "",
    var phone: String = "",
    var birthDate: String = "",
    var birthDateDay: Int = 8,
    var birthDateMonth: Int = 6,
    var birthDateYear: Int = 1978,
    var city: String = "",
    var documentNumber: String = "",
    var frontDocumentUri: Uri = Uri.EMPTY,
    var backDocumentUri: Uri = Uri.EMPTY,
    var frontDocumentUrl: String = "",
    var backDocumentUrl: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    var profilePictureUri: Uri = Uri.EMPTY,
    var profilePictureUrl: String = "",
    var cities: List<String> = listOf(
        "Bogotá",
        "Barranquilla",
        "Chía",
        "Medellín",
        "Cartagena",
        "Cúcuta",
        "Bucaramanga",
        "Cali"
    ),

    var emailError: Boolean = false,
    var nameError: Boolean = false,
    var lastNameError: Boolean = false,
    var phoneCodeError: Boolean = false,
    var phoneError: Boolean = false,
    var birthDateError: Boolean = false,
    var cityError: Boolean = false,
    var documentNumberError: Boolean = false,
    var frontDocumentUriError: Boolean = false,
    var backDocumentUriError: Boolean = false,
    var frontDocumentUrlError: Boolean = false,
    var backDocumentUrlError: Boolean = false,
    var passwordError: Boolean = false,
    var confirmPasswordError: Boolean = false,
    var profilePictureUriError: Boolean = false,
    var profilePictureUrlError: Boolean = false,
    var errorMessage: String = "",
    var isRefreshing: Boolean = false,

)