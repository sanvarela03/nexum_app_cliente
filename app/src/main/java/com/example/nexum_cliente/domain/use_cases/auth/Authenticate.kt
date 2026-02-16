package com.example.nexum_cliente.domain.use_cases.auth

import com.example.nexum_cliente.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Authenticate @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.authenticate()
    }
}