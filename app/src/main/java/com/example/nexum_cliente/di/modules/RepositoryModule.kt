package com.example.nexum_cliente.di.modules

import com.example.nexum_cliente.data.auth.AuthRepositoryImpl
import com.example.nexum_cliente.data.category.CategoryRepositoryImpl
import com.example.nexum_cliente.data.client.ClientRepositoryImpl
import com.example.nexum_cliente.data.country.CountryRepositoryImpl
import com.example.nexum_cliente.data.job_offer.JobOfferRepositoryImpl
import com.example.nexum_cliente.data.market_location.MarketLocationRepositoryImpl
import com.example.nexum_cliente.data.mercado_pago.MercadoPagoRepositoryImpl
import com.example.nexum_cliente.data.message.MessagingRepositoryImpl
import com.example.nexum_cliente.data.profile.ProfileRepositoryImpl
import com.example.nexum_cliente.domain.repository.AuthRepository
import com.example.nexum_cliente.domain.repository.CategoryRepository
import com.example.nexum_cliente.domain.repository.ClientRepository
import com.example.nexum_cliente.domain.repository.CountryRepository
import com.example.nexum_cliente.domain.repository.JobOfferRepository
import com.example.nexum_cliente.domain.repository.MarketLocationRepository
import com.example.nexum_cliente.domain.repository.MercadoPagoRepository
import com.example.nexum_cliente.domain.repository.MessagingRepository
import com.example.nexum_cliente.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindClientRepository(clientRepositoryImpl: ClientRepositoryImpl): ClientRepository

    @Binds
    @Singleton
    abstract fun bindMarketLocationRepository(marketLocationRepositoryImpl: MarketLocationRepositoryImpl): MarketLocationRepository

    @Binds
    @Singleton
    abstract fun bindCountryRepository(countryRepositoryImpl: CountryRepositoryImpl): CountryRepository

    @Binds
    @Singleton
    abstract fun bindJobOfferRepository(jobOfferRepositoryImpl: JobOfferRepositoryImpl): JobOfferRepository

    @Binds
    @Singleton
    abstract fun bindMessagingRepository(messagingRepositoryImpl: MessagingRepositoryImpl): MessagingRepository

    @Binds
    @Singleton
    abstract fun bindMercadoPagoRepository(mercadoPagoRepositoryImpl: MercadoPagoRepositoryImpl): MercadoPagoRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository
}