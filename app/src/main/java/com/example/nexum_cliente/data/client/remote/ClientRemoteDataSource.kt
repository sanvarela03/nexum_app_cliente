package com.example.nexum_cliente.data.client.remote

<<<<<<< Updated upstream
=======
import com.example.nexum_cliente.common.apiRequestFlow
import com.example.nexum_cliente.data.client.remote.payload.res.ClientRes
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

>>>>>>> Stashed changes

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 1/6/2026
 * @version 1.0
 */
<<<<<<< Updated upstream
class ClientRemoteDataSource {
=======
@Singleton
class ClientRemoteDataSource @Inject constructor(
    private val clientApi: ClientApi
) {
    fun getClient(): Flow<ApiResponse<ClientRes>> =
        apiRequestFlow { clientApi.getClient() }
>>>>>>> Stashed changes
}