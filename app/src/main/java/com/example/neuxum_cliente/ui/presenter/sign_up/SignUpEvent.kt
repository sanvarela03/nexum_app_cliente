package com.example.neuxum_cliente.ui.presenter.sign_up

import android.net.Uri

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
    data class BirthDateDayChanged(val birthDateDay: Int) : SignUpEvent()
    data class BirthDateMonthChanged(val birthDateMonth: Int) : SignUpEvent()
    data class BirthDateYearChanged(val birthDateYear: Int) : SignUpEvent()
    data class CityChanged(val city: String) : SignUpEvent()
    data class DocumentNumberChanged(val documentNumber: String) : SignUpEvent()
    data class FrontDocumentUriChanged(val frontDocumentUri: Uri) : SignUpEvent()
    data class BackDocumentUriChanged(val backDocumentUri: Uri) : SignUpEvent()
    data class FrontDocumentUrlChanged(val frontDocumentUrl: String) : SignUpEvent()
    data class BackDocumentUrlChanged(val backDocumentUrl: String) : SignUpEvent()
    data class PasswordChanged(val password: String) : SignUpEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : SignUpEvent()
    data class ProfilePictureUriChanged(val profilePictureUri: Uri) : SignUpEvent()
    data class ProfilePictureUrlChanged(val profilePictureUrl: String) : SignUpEvent()


    object ContinueButtonClicked : SignUpEvent()
}