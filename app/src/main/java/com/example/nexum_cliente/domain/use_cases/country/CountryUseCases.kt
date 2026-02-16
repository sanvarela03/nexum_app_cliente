package com.example.nexum_cliente.domain.use_cases.country

import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 02/12/2025
 * @version 1.0
 */
@Singleton
data class CountryUseCases @Inject constructor(
    val updateCountry: UpdateCountries,
    val observeCountry: ObserveCountries
)
