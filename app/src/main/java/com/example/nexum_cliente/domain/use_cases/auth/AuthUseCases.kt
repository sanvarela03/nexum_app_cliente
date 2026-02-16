package com.example.nexum_cliente.domain.use_cases.auth

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class AuthUseCases @Inject constructor(
    val signIn: SignIn,
    val signOut: SignOut,
    val signUp: SignUp,
    val authenticate: Authenticate
)