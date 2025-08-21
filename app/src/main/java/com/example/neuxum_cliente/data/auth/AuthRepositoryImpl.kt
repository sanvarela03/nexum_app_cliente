package com.example.neuxum_cliente.data.auth

import android.util.Log
import com.example.neuxum_cliente.common.apiRequestFlow
import com.example.neuxum_cliente.data.auth.remote.AuthApi
import com.example.neuxum_cliente.data.auth.remote.payload.req.SignInReq
import com.example.neuxum_cliente.data.auth.remote.payload.req.SignUpReq
import com.example.neuxum_cliente.data.global_payload.res.ApiResponse
import com.example.neuxum_cliente.di.modules.IoDispatcher
import com.example.neuxum_cliente.domain.repository.AuthRepository
import com.example.protapptest.security.TokenManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : AuthRepository {

    val TAG = AuthRepositoryImpl::class.simpleName

    override fun signIn(signInRequest: SignInReq) = apiRequestFlow {
        Log.d("login(auth: AuthRequest) -> auth: ", "$signInRequest")
        authApi.signIn(signInRequest)
    }

    override fun signOut() = flow {
        emit(ApiResponse.Loading)
        apiRequestFlow {
            authApi.signOut()
        }.collect { response ->
            when (response) {
                is ApiResponse.Error -> TODO()
                is ApiResponse.Failure -> {
                    tokenManager.deleteAccessToken()
                    emit(ApiResponse.Failure(response.errorMessage, response.code))
                }

                ApiResponse.Loading -> {
                    emit(ApiResponse.Loading)
                }

                is ApiResponse.Success -> {
                    tokenManager.deleteAccessToken()
                    emit(ApiResponse.Success(response.data))
                }
            }
        }
    }.flowOn(dispatcher)

    override fun signUp(signUpReq: SignUpReq) = apiRequestFlow {
        authApi.signUp(signUpReq)
    }

    override fun authenticate(): Flow<Boolean> = flow {
        tokenManager.getAccessToken().collect {
            if (it != null) {
                emit(true)
            } else {
                emit(false)
            }
        }
    }
}