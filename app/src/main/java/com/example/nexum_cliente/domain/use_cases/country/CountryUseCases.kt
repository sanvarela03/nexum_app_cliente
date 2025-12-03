package com.example.nexum_cliente.domain.use_cases.country

import com.example.nexum_cliente.domain.use_cases.country.ObserveCountries
import com.example.nexum_cliente.domain.use_cases.country.UpdateCountries

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 02/12/2025
 * @version 1.0
 */
data class CountryUseCases(
    val updateCountries: UpdateCountries,
    val observeCountries: ObserveCountries
)
