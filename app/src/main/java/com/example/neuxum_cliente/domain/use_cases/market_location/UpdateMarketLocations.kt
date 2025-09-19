package com.example.neuxum_cliente.domain.use_cases.market_location

import com.example.neuxum_cliente.domain.repository.MarketLocationRepository
/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 09/09/2025
 * @version 1.0
 */
class UpdateMarketLocations (
    private val marketLocationRepository: MarketLocationRepository
) {
     operator fun invoke(countryCode: String, fetchFromRemote: Boolean) =
        marketLocationRepository.updateAllLocations(countryCode, fetchFromRemote)

}