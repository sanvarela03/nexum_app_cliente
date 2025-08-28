package com.example.neuxum_cliente.ui.presenter.sign_up

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.neuxum_cliente.domain.use_cases.auth.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject constructor(
    private val authUseCases: AuthUseCases
//    private val viewModelScope: CoroutineScope
) : ViewModel() {

    var state by mutableStateOf(SignUpState())
    var signUpEmailValidationPassed by mutableStateOf(false)
    var signUpUserDataValidationPassed by mutableStateOf(false)
    var signUpPhoneDataValidationPassed by mutableStateOf(false)
    var signUpBirthDateValidationPassed by mutableStateOf(false)
    var signUpCityValidationPassed by mutableStateOf(false)
    var signUpDocumentNumberValidationPassed by mutableStateOf(false)

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }

            is SignUpEvent.NameChanged -> {
                state = state.copy(name = event.name)
            }

            is SignUpEvent.LastNameChanged -> {
                state = state.copy(lastName = event.lastName)
            }

            is SignUpEvent.PhoneCodeChanged -> {
                state = state.copy(phoneCode = event.phoneCode)
            }

            is SignUpEvent.CellphoneChanged -> {
                state = state.copy(phone = event.phone)
            }

            is SignUpEvent.CityChanged -> {
                state = state.copy(city = event.city)
            }

            is SignUpEvent.DocumentNumberChanged -> {
                state = state.copy(documentNumber = event.documentNumber)
            }

            is SignUpEvent.BirthDateDayChanged -> {
                state = state.copy(birthDateDay = event.birthDateDay)
            }

            is SignUpEvent.BirthDateMonthChanged -> {
                state = state.copy(birthDateMonth = event.birthDateMonth)
            }

            is SignUpEvent.BirthDateYearChanged -> {
                state = state.copy(birthDateYear = event.birthDateYear)
            }

            SignUpEvent.ContinueButtonClicked -> {}
        }
        validateSignUpEmail()
        validateSignUpUserData()
        validateSignUpPhoneData()
        validateSignUpBirthDate()
        validateSignUpCity()
        validateSignUpDocumentNumber()
    }

    private fun validateSignUpEmail() {
        val emailResult = SignUpValidator.validateEmail(
            email = state.email
        )

        state = state.copy(
            emailError = emailResult.status,
        )

        signUpEmailValidationPassed = emailResult.status
    }

    private fun validateSignUpUserData() {
        val nameResult = SignUpValidator.validateName(
            name = state.name
        )

        val lastNameResult = SignUpValidator.validateLastName(
            lastName = state.lastName
        )

        state = state.copy(
            nameError = nameResult.status,
            lastNameError = lastNameResult.status,
        )

        signUpUserDataValidationPassed = nameResult.status && lastNameResult.status
    }

    private fun validateSignUpPhoneData() {
        val phoneCodeResult = SignUpValidator.validatePhoneCode(
            phoneCode = state.phoneCode
        )

        val cellphoneResult = SignUpValidator.validateCellphone(
            phone = state.phone, phoneCode = state.phoneCode
        )

        state = state.copy(
            phoneCodeError = phoneCodeResult.status,
            phoneError = cellphoneResult.status,
        )

        signUpPhoneDataValidationPassed = phoneCodeResult.status && cellphoneResult.status
    }

    private fun validateSignUpBirthDate() {
        val birthDateResult = SignUpValidator.validateBirthDate(
            birthDate = "${state.birthDateDay} - ${state.birthDateMonth} - ${state.birthDateYear}"
        )

        state = state.copy(
            birthDateError = birthDateResult.status,
        )

        signUpBirthDateValidationPassed = birthDateResult.status
    }

    private fun validateSignUpCity() {
        val cityResult = SignUpValidator.validateCity(
            city = state.city
        )

        state = state.copy(
            cityError = cityResult.status,
        )

        signUpCityValidationPassed = cityResult.status

    }

    private fun validateSignUpDocumentNumber() {
        val documentNumberResult = SignUpValidator.validateDocumentNumber(
            documentNumber = state.documentNumber
        )

        state = state.copy(
            documentNumberError = documentNumberResult.status,
        )

        signUpDocumentNumberValidationPassed = documentNumberResult.status

    }

}


