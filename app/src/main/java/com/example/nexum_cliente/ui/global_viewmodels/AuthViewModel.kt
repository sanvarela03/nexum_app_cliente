package com.example.nexum_cliente.ui.global_viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_cliente.common.UserAuthState
import com.example.nexum_cliente.domain.use_cases.auth.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {
    private val _isAuthenticated = MutableStateFlow(UserAuthState.UNKNOWN)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    init {
        observeAuthenticationState()
    }

    private fun observeAuthenticationState() {
        Log.d("AuthViewModel", "observeAuthenticationState")
        viewModelScope.launch {
            authUseCases.authenticate().collect { hasToken ->
                Log.d("AuthViewModel", "hasToken: $hasToken")
                _isAuthenticated.value = if (hasToken) {
                    UserAuthState.AUTHENTICATED
                } else {
                    UserAuthState.UNAUTHENTICATED
                }
                Log.d("AuthViewModel", "is_Authenticated: ${_isAuthenticated.value}")
                Log.d("AuthViewModel", "isAuthenticated: ${isAuthenticated.value}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("AuthViewModel", "onCleared")
    }
}