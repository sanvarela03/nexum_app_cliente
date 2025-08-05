package com.example.neuxum_cliente.ui.presenter.sign_in

data class SignInState(
    var email: String = "",
    var password: String = "",

    var emailError: Boolean = false,
    var passwordError: Boolean = false
)
