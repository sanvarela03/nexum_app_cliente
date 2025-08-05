package com.example.neuxum_cliente.domain.use_cases.auth

import android.util.Log
import com.example.neuxum_cliente.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class Authenticate(
    private val repository: AuthRepository
) {
    val TAG = Authenticate::class.simpleName
    operator fun invoke(): Flow<Boolean> {
        Log.d(TAG, "INSIDE INVOKE")
        return repository.authenticate()
    }
}