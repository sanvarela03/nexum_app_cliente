package com.example.nexum_cliente.domain.use_cases.market_location

import com.example.nexum_cliente.domain.repository.MarketLocationRepository
import javax.inject.Inject

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 09/09/2025
 * @version 1.0
 */
class ObserveMarketLocations @Inject constructor(
    private val repository: MarketLocationRepository
) {
    operator fun invoke() = repository.observe()
}