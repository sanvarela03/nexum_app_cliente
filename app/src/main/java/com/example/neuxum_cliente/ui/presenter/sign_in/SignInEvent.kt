package com.example.neuxum_cliente.ui.presenter.sign_in

sealed class SignInEvent {
    data class UsernameChanged(val username: String) : SignInEvent()
    data class PasswordChanged(val password: String) : SignInEvent()
    object LoginButtonClicked : SignInEvent()
    object ForgotPasswordButtonClicked : SignInEvent()
}