package com.example.neuxum_cliente.ui.presenter.sign_in

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neuxum_cliente.data.auth.remote.payload.res.SignInRes
import com.example.neuxum_cliente.data.global_payload.res.ApiResponse
import com.example.neuxum_cliente.domain.use_cases.auth.AuthUseCases
import com.example.neuxum_cliente.ui.global_viewmodels.AuthViewModel
import com.example.neuxum_cliente.ui.presenter.splash.SplashEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.protapptest.security.TokenManager
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.tasks.await

@HiltViewModel
class SignInViewModel
@Inject constructor(
    private val authUseCases: AuthUseCases,
    private val tokenManager: TokenManager,
    private val authViewModel: AuthViewModel,
//    private val viewModelScope: CoroutineScope
) : ViewModel() {

    private val _signInResponse: MutableState<ApiResponse<SignInRes>> =
        mutableStateOf(ApiResponse.Loading)
    val signInResponse: State<ApiResponse<SignInRes>> = _signInResponse

    private var signInJob: Job? = null

    var response by mutableStateOf<SignInRes?>(null)

    private val _signInResponseTest: MutableState<ApiResponse<SignInRes>> =
        mutableStateOf(ApiResponse.Loading)
    val signInResponseTest: State<ApiResponse<SignInRes>> = _signInResponseTest


    var state by mutableStateOf(SignInState())
    var allValidationsPassed by mutableStateOf(false)

    fun onEvent(event: SignInEvent) {

        Log.d("SignInViewModel", "onEvent")
        when (event) {
            is SignInEvent.UsernameChanged -> {
                state = state.copy(email = event.username)
            }

            is SignInEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }

            SignInEvent.LoginButtonClicked -> {
                signInTest()
            }

            SignInEvent.ForgotPasswordButtonClicked -> {

            }
        }
        validateSignInUIDataWithRules()
    }

    private fun validateSignInUIDataWithRules() {
        val usernameResult = SignInValidator.validateEmail(
            email = state.email
        )

        val passwordResult = SignInValidator.validatePassword(
            password = state.password
        )

        state = state.copy(
            emailError = usernameResult.status,
            passwordError = passwordResult.status
        )

        allValidationsPassed = usernameResult.status && passwordResult.status
    }

    private fun signInTest() {
        Log.d("SignInViewModel", "onEvent | LoginButtonClicked() | signInTest()")

        signInJob?.cancel()
        signInJob = CoroutineScope(Dispatchers.Main + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                Log.d("SignInViewModel", "Ocurrió un error: ${error.localizedMessage}")
            }
        }
        ).launch {
            val tkn = FirebaseMessaging.getInstance().token.await()

            Log.d(
                "SignInViewModel",
                "onEvent | LoginButtonClicked() | signInTest() | viewModelScope.launch  "
            )
            authUseCases.signIn(state.email, state.password, tkn)
                .collect {
                    when (it) {
                        is ApiResponse.Success -> {
                            it.data?.let {
                                tokenManager.saveAccessToken(it.token)
                                response = it
                                tokenManager.saveRefreshToken(it.refreshToken)
                                tokenManager.saveUserId(it.id)
                                authViewModel.onEvent(SplashEvent.CheckAuthentication)
                            }
                        }

                        is ApiResponse.Failure -> {}
                        is ApiResponse.Loading -> {}
                        is ApiResponse.Error -> TODO()
                    }


                }
        }
    }

    override fun onCleared() {
        Log.d("SignInViewModel", "onCleared")
        super.onCleared()

        signInJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
    }
}