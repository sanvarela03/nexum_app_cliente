package com.example.nexum_cliente.domain.use_cases.country

import com.example.nexum_cliente.domain.repository.CountryRepository
import javax.inject.Inject

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 02/12/2025
 * @version 1.0
 */
class UpdateCountries @Inject constructor(
    private val repository: CountryRepository
) {
    operator fun invoke(fetchFromRemote: Boolean) = repository.update(fetchFromRemote)
}