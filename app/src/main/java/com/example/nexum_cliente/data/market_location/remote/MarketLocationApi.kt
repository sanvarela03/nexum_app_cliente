package com.example.nexum_cliente.data.market_location.remote

import com.example.nexum_cliente.data.market_location.remote.payload.res.MarketLocationRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 04/09/2025
 * @version 1.0
 */
interface MarketLocationApi {
    @GET("api/v1/market-locations")
    suspend fun getAllLocations(
        @Query("country_code") countryCode: String? = null
    ): Response<List<MarketLocationRes>>
}