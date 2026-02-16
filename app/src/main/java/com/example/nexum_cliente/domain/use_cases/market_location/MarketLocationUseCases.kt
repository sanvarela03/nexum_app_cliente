package com.example.nexum_cliente.domain.use_cases.market_location

import javax.inject.Inject

data class MarketLocationUseCases @Inject constructor(
    val updateMarketLocations: UpdateMarketLocations,
    val observeMarketLocations: ObserveMarketLocations
)
