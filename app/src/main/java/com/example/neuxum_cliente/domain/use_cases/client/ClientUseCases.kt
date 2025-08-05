package com.example.neuxum_cliente.domain.use_cases.client


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/3/2025
 * @version 1.0
 */
data class ClientUseCases(
    val updateClient: UpdateClient,
    val observeClient: ObserveClient,
    val observeUserId: ObserveUserId
)
