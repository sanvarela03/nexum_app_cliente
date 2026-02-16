package com.example.nexum_cliente.domain.repository

import com.example.nexum_cliente.data.country.local.CountryEntity
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.domain.model.Country
import kotlinx.coroutines.flow.Flow

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/2/2025
 * @version 1.0
 */
interface CountryRepository {
    fun update(fetchFromRemote: Boolean): Flow<ApiResponse<Unit>>
    fun observe(): Flow<List<Country>>
}