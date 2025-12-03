package com.example.nexum_cliente.data.market_location.remote

import android.util.Log
import com.example.nexum_cliente.data.market_location.local.MarketLocationDao
import com.example.nexum_cliente.data.market_location.local.MarketLocationEntity
import com.example.nexum_cliente.data.market_location.remote.payload.res.MarketLocationRes
import retrofit2.Response
import javax.inject.Inject

/**
 * Returns DB-backed data instead of hitting the network.
 * Use Hilt to provide this in place of the real Retrofit API for debugging/mock.
 */
class FakeMarketLocationApi @Inject constructor(
    private val dao: MarketLocationDao
) : MarketLocationApi {

    override suspend fun getAllLocations(countryCode: String?): Response<List<MarketLocationRes>> {
        // Read from DB (suspend)
        Log.d("FakeMarketLocationApi", "getAllLocations: $countryCode")
        val entities: List<MarketLocationEntity?> = dao.getAllLocations()

        Log.d("FakeMarketLocationApi", "entities: $entities")

        val resList: List<MarketLocationRes> = entities
            .filterNotNull()
            .map { it.toRes() }

        return Response.success(resList)

    }

    // Extension mapper (you can move to a parser file)
    private fun MarketLocationEntity.toRes(): MarketLocationRes {
        // Map fields - change names to match your actual MarketLocationRes constructor
        return MarketLocationRes(
            id = this.id,
            city = this.city,
            state = this.state,
            country = this.country,
            countryCode = this.countryCode,
        )
    }
}
