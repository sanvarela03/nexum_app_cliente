package com.example.neuxum_cliente.ui.presenter.sign_up

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 06/08/2025
 * @version 1.0
 */
sealed class SignUpEvent {
    data class EmailChanged(val email: String) : SignUpEvent()
    data class NameChanged(val name: String) : SignUpEvent()
    data class LastNameChanged(val lastName: String) : SignUpEvent()
    data class PhoneCodeChanged(val phoneCode: String) : SignUpEvent()
    data class CellphoneChanged(val phone: String) : SignUpEvent()
    data class BirthDateChanged(val birthDate: String) : SignUpEvent()
    data class CityChanged(val city: String) : SignUpEvent()
    data class DocumentNumberChanged(val documentNumber: String) : SignUpEvent()
    object ContinueButtonClicked : SignUpEvent()
}