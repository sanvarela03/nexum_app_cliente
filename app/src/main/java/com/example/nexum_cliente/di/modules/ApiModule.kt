package com.example.nexum_cliente.di.modules

import com.example.nexum_cliente.common.HOST_URL
import com.example.nexum_cliente.data.auth.remote.AuthApi
import com.example.nexum_cliente.data.category.remote.CategoryApi
import com.example.nexum_cliente.data.client.remote.ClientApi
import com.example.nexum_cliente.data.country.remote.CountryApi
import com.example.nexum_cliente.data.job_offer.remote.JobOfferApi
import com.example.nexum_cliente.data.market_location.remote.MarketLocationApi
import com.example.nexum_cliente.data.mercado_pago.remote.MercadoPagoApi
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
import java.util.concurrent.TimeUnit
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
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(): Retrofit.Builder =
        Retrofit.Builder().baseUrl(HOST_URL).addConverterFactory(GsonConverterFactory.create())

    @Provides
    @Singleton
    fun provideAuthApi(
        retrofit: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): AuthApi =
        retrofit.client(okHttpClient).build().create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideClientApi(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): ClientApi =
        retrofit.client(okHttpClient).build().create(ClientApi::class.java)

    @Provides
    @Singleton
    fun provideCategoryApi(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): CategoryApi =
        retrofit.client(okHttpClient).build().create(CategoryApi::class.java)

    @Provides
    @Singleton
    fun provideMarketLocationApi(retrofit: Retrofit.Builder): MarketLocationApi =
        retrofit.build().create(MarketLocationApi::class.java)

    @Provides
    @Singleton
    fun provideCountryApi(retrofit: Retrofit.Builder): CountryApi {
        return retrofit.build().create(CountryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideJobOfferApi(retrofit: Retrofit.Builder, okHttpClient: OkHttpClient): JobOfferApi =
        retrofit.client(okHttpClient).build().create(JobOfferApi::class.java)


    @Provides
    @Singleton
    fun provideMercadoPagoApi(
        retrofit: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): MercadoPagoApi =
        retrofit.client(okHttpClient).build().create(MercadoPagoApi::class.java)


}