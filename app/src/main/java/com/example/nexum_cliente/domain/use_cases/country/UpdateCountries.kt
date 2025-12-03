package com.example.nexum_cliente.domain.use_cases.country

import android.util.Log
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 02/12/2025
 * @version 1.0
 */
class UpdateCountries(
    private val countryRepository: CountryRepository
) {
    operator fun invoke(fetchFromRemote: Boolean): Flow<ApiResponse<Unit>> {
        Log.d(
            "UpdateCountries",
            "invoke - fetchFromRemote ${fetchFromRemote}"
        )
        return countryRepository.update(fetchFromRemote)
    }
}