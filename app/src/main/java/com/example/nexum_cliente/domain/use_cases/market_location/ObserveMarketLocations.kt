package com.example.nexum_cliente.domain.use_cases.market_location

import com.example.nexum_cliente.data.market_location.local.MarketLocationEntity
import com.example.nexum_cliente.domain.repository.MarketLocationRepository
import kotlinx.coroutines.flow.Flow

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 09/09/2025
 * @version 1.0
 */
class ObserveMarketLocations (
    private val marketLocationRepository: MarketLocationRepository
) {
    operator fun invoke(): Flow<List<MarketLocationEntity>> {
        return marketLocationRepository.observeAllLocations()
    }

}