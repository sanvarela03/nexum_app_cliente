package com.example.neuxum_cliente.di.modules

import com.example.neuxum_cliente.domain.use_cases.auth.AuthUseCases
import com.example.neuxum_cliente.domain.use_cases.category.CategoryUseCases
import com.example.neuxum_cliente.ui.global_viewmodels.AuthViewModel
import com.example.neuxum_cliente.ui.presenter.categories.CategoriesViewModel
import com.example.neuxum_cliente.ui.presenter.job_offer.JobOfferViewModel
import com.example.neuxum_cliente.ui.presenter.map.MapViewModel
import com.example.neuxum_cliente.ui.presenter.sign_in.SignInViewModel
import com.example.protapptest.security.TokenManager

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {
    @Provides
    @Singleton
    fun provideSplashViewModel(authUseCases: AuthUseCases): AuthViewModel {
        return AuthViewModel(authUseCases)
    }

    @Provides
    @Singleton
    fun provideSignInViewModel(
        authUseCases: AuthUseCases,
        tokenManager: TokenManager,
        authViewModel: AuthViewModel
    ): SignInViewModel {
        return SignInViewModel(authUseCases, tokenManager, authViewModel)
    }

    @Provides
    @Singleton
    fun providesCategoriesViewModel(
        categoryUseCases: CategoryUseCases,
    ) = CategoriesViewModel(
        categoryUseCases = categoryUseCases
    )

    @Provides
    @Singleton
    fun providesJobOfferViewModel() = JobOfferViewModel()

    @Provides
    @Singleton
    fun providesMapViewModel() = MapViewModel()

//    @Provides
//    @Singleton
//    fun provideTokenViewModel(tokenManager: TokenManager): TokenViewModel {
//        return TokenViewModel(tokenManager)
//    }
}