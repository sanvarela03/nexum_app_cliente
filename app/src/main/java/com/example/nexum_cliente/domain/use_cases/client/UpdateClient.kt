package com.example.nexum_cliente.domain.use_cases.client

import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.domain.repository.ClientRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/3/2025
 * @version 1.0
 */
data class UpdateClient @Inject constructor(
    private val repository: ClientRepository
) {
    operator fun invoke(fetchFromRemote: Boolean): Flow<ApiResponse<Unit>> {
        return repository.update(fetchFromRemote)
    }
}