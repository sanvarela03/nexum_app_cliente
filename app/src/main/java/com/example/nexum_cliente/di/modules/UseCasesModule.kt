package com.example.nexum_cliente.di.modules

import com.example.nexum_cliente.domain.repository.AuthRepository
import com.example.nexum_cliente.domain.repository.CategoryRepository
import com.example.nexum_cliente.domain.repository.ClientRepository
import com.example.nexum_cliente.domain.repository.CountryRepository
import com.example.nexum_cliente.domain.repository.MarketLocationRepository
import com.example.nexum_cliente.domain.use_cases.auth.AuthUseCases
import com.example.nexum_cliente.domain.use_cases.auth.Authenticate
import com.example.nexum_cliente.domain.use_cases.auth.SignIn
import com.example.nexum_cliente.domain.use_cases.auth.SignOut
import com.example.nexum_cliente.domain.use_cases.auth.SignUp
import com.example.nexum_cliente.domain.use_cases.category.CategoryUseCases
import com.example.nexum_cliente.domain.use_cases.category.ObserveCategories
import com.example.nexum_cliente.domain.use_cases.category.UpdateCategories
import com.example.nexum_cliente.domain.use_cases.client.ClientUseCases
import com.example.nexum_cliente.domain.use_cases.client.ObserveClient
import com.example.nexum_cliente.domain.use_cases.client.ObserveUserId
import com.example.nexum_cliente.domain.use_cases.client.UpdateClient
import com.example.nexum_cliente.domain.use_cases.country.CountryUseCases
import com.example.nexum_cliente.domain.use_cases.country.ObserveCountries
import com.example.nexum_cliente.domain.use_cases.country.UpdateCountries
import com.example.nexum_cliente.domain.use_cases.market_location.MarketLocationUseCases
import com.example.nexum_cliente.domain.use_cases.market_location.ObserveMarketLocations
import com.example.nexum_cliente.domain.use_cases.market_location.UpdateMarketLocations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
object UseCasesModule {
    @Provides
    @Singleton
    fun provideAuthUseCases(repository: AuthRepository): AuthUseCases {
        return AuthUseCases(
            signIn = SignIn(repository),
            signOut = SignOut(repository),
            signUp = SignUp(repository),
            authenticate = Authenticate(repository)
        )
    }

    @Provides
    @Singleton
    fun provideClientUseCases(repository: ClientRepository): ClientUseCases {
        return ClientUseCases(
            updateClient = UpdateClient(repository),
            observeClient = ObserveClient(repository),
            observeUserId = ObserveUserId(repository)
        )
    }

    @Provides
    @Singleton
    fun provideCategoryUseCases(repository: CategoryRepository): CategoryUseCases {
        return CategoryUseCases(
            updateCategories = UpdateCategories(repository),
            observeCategories = ObserveCategories(repository)
        )
    }

    @Provides
    @Singleton
    fun provideMarketLocationUseCases(repository: MarketLocationRepository): MarketLocationUseCases {
        return MarketLocationUseCases(
            updateMarketLocations = UpdateMarketLocations(repository),
            observeMarketLocations = ObserveMarketLocations(repository)
        )

    }

    @Provides
    @Singleton
    fun provideCountryUseCases(repository: CountryRepository): CountryUseCases {
        return CountryUseCases(
            updateCountries = UpdateCountries(repository),
            observeCountries = ObserveCountries(repository)
        )

    }
}