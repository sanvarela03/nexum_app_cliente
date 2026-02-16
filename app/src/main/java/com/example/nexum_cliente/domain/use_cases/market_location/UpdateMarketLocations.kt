package com.example.nexum_cliente.domain.use_cases.market_location

import android.util.Log
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.domain.repository.MarketLocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 09/09/2025
 * @version 1.0
 */
class UpdateMarketLocations @Inject constructor(
    private val repository: MarketLocationRepository
) {
    operator fun invoke(fetchFromRemote: Boolean) = repository.update(fetchFromRemote)

}