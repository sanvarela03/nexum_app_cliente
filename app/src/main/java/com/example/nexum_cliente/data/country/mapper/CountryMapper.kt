package com.example.nexum_cliente.data.country.mapper

import com.example.nexum_cliente.data.country.local.CountryEntity
import com.example.nexum_cliente.data.country.remote.payload.res.CountryRes

object CountryMapper {
    fun toEntity(countryRes: CountryRes): CountryEntity {
        return CountryEntity(
            id = countryRes.id,
            name = countryRes.name,
            code = countryRes.code,
            phoneCode = countryRes.phoneCode,
            flagEmoji = countryRes.flagEmoji,
            phoneCheckRegex = countryRes.phoneCheckRegex
        )
    }

    fun toEntity(countriesRes: List<CountryRes>): List<CountryEntity> {
        return countriesRes.map { toEntity(it) }
    }
}