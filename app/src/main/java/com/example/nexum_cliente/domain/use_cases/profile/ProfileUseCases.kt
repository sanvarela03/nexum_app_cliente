package com.example.nexum_cliente.domain.use_cases.profile

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class ProfileUseCases @Inject constructor(
    val updateProfile: UpdateProfile,
    val observeProfile: ObserveProfile
)
