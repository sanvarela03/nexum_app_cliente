package com.example.neuxum_cliente.ui.presenter.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neuxum_cliente.data.global_payload.res.ApiResponse
import com.example.neuxum_cliente.data.global_payload.res.MessageRes
import com.example.neuxum_cliente.domain.use_cases.auth.AuthUseCases
import com.example.neuxum_cliente.domain.use_cases.client.ClientUseCases
import com.example.neuxum_cliente.ui.global_viewmodels.AuthViewModel
import com.example.neuxum_cliente.ui.presenter.sign_in.SignInViewModel
import com.example.neuxum_cliente.ui.presenter.splash.SplashEvent
import com.example.protapptest.security.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val clientUseCases: ClientUseCases,
    private val tokenManager: TokenManager,
    private val authViewModel: AuthViewModel,
    private val signInViewModel: SignInViewModel
) : ViewModel() {

    var state by mutableStateOf(HomeState())

    private val _signOutResponse: MutableState<ApiResponse<MessageRes>> =
        mutableStateOf(ApiResponse.Loading)
    val signOutResponse = _signOutResponse


    var signOutJob: Job? = null
    var getProducerJob: Job? = null

    init {
        Log.d("HomeViewModel", " init ")
        observeClient()
        Log.d("HomeViewModel", " init 2")
        clientUseCases.updateClient(fetchFromRemote = true)
    }

    private fun observeClient() {
        viewModelScope.launch {
            clientUseCases.observeUserId()
                .filterNotNull()
                .distinctUntilChanged()
                .flatMapLatest { userId ->
                    clientUseCases.observeClient(userId)
                }.catch { e ->
                    Log.d("HomeViewModel", "Error observando cliente: ${e.message}")
                    state = state.copy(errorMessage = e.message ?: "Unknown error")
                }
                .collect { client ->
                    state = state.copy(clientEntity = client)
                }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.SignOutBtnClicked -> {
                signOut()
            }

            HomeEvent.Refresh -> {
                updateClient(fetchFromRemote = true)
            }
        }
    }

    fun updateClient(fetchFromRemote: Boolean) {
        viewModelScope.launch {
            clientUseCases.updateClient(fetchFromRemote).catch {
                Log.d("HomeViewModel", "Error actualizando cliente: ${it.message}")
                state = state.copy(errorMessage = it.message ?: "Unknown error")
            }.collect {
                when (it) {
                    ApiResponse.Loading -> {
                        state = state.copy(isRefreshing = true)
                    }

                    is ApiResponse.Error -> {
                        state = state.copy(isRefreshing = false)
                    }

                    is ApiResponse.Failure -> {
                        state = state.copy(isRefreshing = false)
                    }

                    is ApiResponse.Success -> {
                        state = state.copy(isRefreshing = false)
                    }
                }
            }
        }
    }


    fun signOut() {
        signOutJob = viewModelScope.launch {
            authUseCases.signOut().collect {
                when (it) {
                    ApiResponse.Loading -> {}
                    is ApiResponse.Failure -> {
                        val data = it.errorMessage
                        Log.d("ApiResponse", "errorMessage = $data")
                        state = state.copy(errorMessage = data)

                    }

                    is ApiResponse.Success -> {
                        it.data.let {
                            Log.d("ApiResponse", "message = $it")
                            tokenManager.deleteAccessToken()
                            authViewModel.onEvent(SplashEvent.CheckAuthentication)
                        }
                    }

                    is ApiResponse.Error -> TODO()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        signOutJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }

        getProducerJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
    }
}
