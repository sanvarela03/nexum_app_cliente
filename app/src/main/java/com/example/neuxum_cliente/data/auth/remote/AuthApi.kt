package com.example.neuxum_cliente.data.auth.remote

import com.example.neuxum_cliente.data.auth.remote.payload.req.RefreshReq
import com.example.neuxum_cliente.data.auth.remote.payload.req.SignInReq
import com.example.neuxum_cliente.data.auth.remote.payload.req.SignUpReq
import com.example.neuxum_cliente.data.auth.remote.payload.res.RefreshRes
import com.example.neuxum_cliente.data.auth.remote.payload.res.SignInRes
import com.example.neuxum_cliente.data.global_payload.res.MessageRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 7/30/2025
 * @version 1.0
 */
interface AuthApi {
    @POST("/api/v1/auth/signin")
    suspend fun signIn(@Body signInReq: SignInReq): Response<SignInRes>

    @POST("/api/v1/auth/signup")
    suspend fun signUp(@Body signUpReq: SignUpReq): Response<MessageRes>

    @POST("/api/v1/auth/signout")
    suspend fun signOut(): Response<MessageRes>

    @POST("/api/v1/auth/refreshtoken")
    suspend fun refreshToken(@Body refreshRequest: RefreshReq): Response<RefreshRes>
}