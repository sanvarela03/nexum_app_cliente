package com.example.neuxum_cliente.data.auth

import android.util.Log
import com.example.neuxum_cliente.common.apiRequestFlow
import com.example.neuxum_cliente.data.auth.remote.AuthApi
import com.example.neuxum_cliente.data.auth.remote.payload.req.SignInReq
import com.example.neuxum_cliente.data.auth.remote.payload.req.SignUpReq
import com.example.neuxum_cliente.domain.repository.AuthRepository
import com.example.protapptest.security.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    val TAG = AuthRepositoryImpl::class.simpleName

    override fun signIn(signInRequest: SignInReq) = apiRequestFlow {
        Log.d("login(auth: AuthRequest) -> auth: ", "$signInRequest")
        authApi.signIn(signInRequest)
    }

    override fun signOut() = apiRequestFlow {
        authApi.signOut()
    }

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