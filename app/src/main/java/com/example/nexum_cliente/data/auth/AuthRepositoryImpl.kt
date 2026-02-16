package com.example.nexum_cliente.data.auth

import android.util.Log
import com.example.nexum_cliente.common.apiRequestFlow
import com.example.nexum_cliente.data.auth.remote.AuthApi
import com.example.nexum_cliente.data.auth.remote.payload.req.SignInReq
import com.example.nexum_cliente.data.auth.remote.payload.req.SignUpReq
import com.example.nexum_cliente.di.modules.IoDispatcher
import com.example.nexum_cliente.domain.repository.AuthRepository
import com.example.nexum_cliente.security.TokenManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun signOut() = apiRequestFlow { authApi.signOut() }

    override fun signUp(signUpReq: SignUpReq) = apiRequestFlow {
        authApi.signUp(signUpReq)
    }

    override fun authenticate(): Flow<Boolean> {
        return tokenManager.getAccessToken()
            .map { it != null }
    }
}