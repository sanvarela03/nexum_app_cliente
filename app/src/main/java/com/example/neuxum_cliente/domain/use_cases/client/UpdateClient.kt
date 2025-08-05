package com.example.neuxum_cliente.domain.use_cases.client

import com.example.neuxum_cliente.data.global_payload.res.ApiResponse
import com.example.neuxum_cliente.domain.repository.ClientRepository
import kotlinx.coroutines.flow.Flow


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/3/2025
 * @version 1.0
 */
data class UpdateClient(
    private val clientRepository: ClientRepository
) {
    operator fun invoke(fetchFromRemote: Boolean): Flow<ApiResponse<Unit>> {
        return clientRepository.updateClient(fetchFromRemote)
    }
}