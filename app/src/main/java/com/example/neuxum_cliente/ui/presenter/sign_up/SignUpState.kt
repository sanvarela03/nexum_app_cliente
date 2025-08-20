package com.example.neuxum_cliente.ui.presenter.sign_up

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
    var city: String = "",
    var documentNumber: String = "",
    var frontDocumentUrl: String = "",
    var backDocumentUrl: String = "",
    var profilePictureUrl: String = "",
    var password: String = "",
    var confirmPassword: String = "",

    var emailError: Boolean = false,
    var nameError: Boolean = false,
    var lastNameError: Boolean = false,
    var phoneCodeError: Boolean = false,
    var phoneError: Boolean = false,
    var birthDateError: Boolean = false,
    var cityError: Boolean = false,
    var documentNumberError: Boolean = false,
    var frontDocumentUrlError: Boolean = false,
    var backDocumentUrlError: Boolean = false,
    var profilePictureUrlError: Boolean = false,
    var passwordError: Boolean = false,
    var confirmPasswordError: Boolean = false,
)