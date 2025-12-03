package com.example.nexum_cliente.domain.repository

import com.example.nexum_cliente.data.client.local.ClientEntity
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import kotlinx.coroutines.flow.Flow

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/2/2025
 * @version 1.0
 */
interface ClientRepository {
    fun updateClient(fetchFromRemote: Boolean): Flow<ApiResponse<Unit>>
    fun observeClient(userId: Long?): Flow<ClientEntity?>
    fun observeUserId(): Flow<Long?>
}