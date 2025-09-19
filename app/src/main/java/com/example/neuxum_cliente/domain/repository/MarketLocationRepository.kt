package com.example.neuxum_cliente.domain.repository

import com.example.neuxum_cliente.data.global_payload.res.ApiResponse
import com.example.neuxum_cliente.data.market_location.local.MarketLocationEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 09/09/2025
 * @version 1.0
 */
interface MarketLocationRepository {
    fun observeAllLocations(): Flow<List<MarketLocationEntity>>
    fun updateAllLocations(countryCode: String, fetchFromRemote: Boolean): Flow<ApiResponse<Unit>>
}