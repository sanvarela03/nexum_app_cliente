package com.example.neuxum_cliente.ui.presenter.sign_up

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.neuxum_cliente.domain.use_cases.auth.AuthUseCases
import com.example.neuxum_cliente.ui.global_viewmodels.AuthViewModel
import com.example.neuxum_cliente.ui.presenter.sign_in.SignInState
import com.example.neuxum_cliente.ui.presenter.sign_in.SignInValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Singleton

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

            SignUpEvent.ContinueButtonClicked -> {}
        }
        validateSignUpEmail()
        validateSignUpUserData()
        validateSignUpPhoneData()
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
}


