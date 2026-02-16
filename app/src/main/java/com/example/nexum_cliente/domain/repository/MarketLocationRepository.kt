package com.example.nexum_cliente.domain.repository

import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.data.market_location.local.MarketLocationEntity
import com.example.nexum_cliente.domain.model.MarketLocation
import kotlinx.coroutines.flow.Flow

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 09/09/2025
 * @version 1.0
 */
interface MarketLocationRepository {
    fun update(fetchFromRemote: Boolean): Flow<ApiResponse<Unit>>
    fun observe() : Flow<List<MarketLocation>>
}