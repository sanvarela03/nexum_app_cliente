package com.example.neuxum_cliente.data.market_location.parser

import com.example.neuxum_cliente.data.market_location.local.MarketLocationEntity
import com.example.neuxum_cliente.data.market_location.remote.payload.res.MarketLocationRes

object MarketLocationParser {
    fun toEntity(res: MarketLocationRes): MarketLocationEntity = MarketLocationEntity(
        id = res.id,
        city = res.city,
        state = res.state,
        country = res.country,
        countryCode = res.countryCode,
    )

    fun toEntity(res: List<MarketLocationRes>): List<MarketLocationEntity> = res.map { MarketLocationParser.toEntity(it) }
}