package com.example.nexum_cliente.domain.use_cases.country

import com.example.nexum_cliente.data.country.local.CountryEntity
import com.example.nexum_cliente.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 02/12/2025
 * @version 1.0
 */
class ObserveCountries @Inject constructor (
    private val repository: CountryRepository
) {
    operator fun invoke() = repository.observe()
}