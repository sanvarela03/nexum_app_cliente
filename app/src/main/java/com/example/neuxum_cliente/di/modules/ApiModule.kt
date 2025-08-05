package com.example.neuxum_cliente.di.modules

import com.example.neuxum_cliente.common.HOST_URL
import com.example.neuxum_cliente.data.auth.remote.AuthApi
import com.example.neuxum_cliente.data.client.remote.ClientApi
import com.example.protapptest.security.AuthAuthenticator
import com.example.protapptest.security.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 7/30/2025
 * @version 1.0
 */
@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthAPI(
        okHttpClient: OkHttpClient, //TODO Arreglar para ingresar y registrarse y para el refresh
        retrofit: Retrofit.Builder
    ): AuthApi =
        retrofit.client(okHttpClient).build().create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideClientAPI(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): ClientApi =
        retrofit.client(okHttpClient).build().create(ClientApi::class.java)


    @Provides
    @Singleton
    fun provideRetrofitBuilder(): Retrofit.Builder =
        Retrofit.Builder().baseUrl(HOST_URL).addConverterFactory(GsonConverterFactory.create())
}