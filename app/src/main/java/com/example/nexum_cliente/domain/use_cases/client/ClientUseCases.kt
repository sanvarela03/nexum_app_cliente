package com.example.nexum_cliente.domain.use_cases.client

import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/3/2025
 * @version 1.0
 */
@Singleton
data class ClientUseCases @Inject constructor(
    val updateClient: UpdateClient,
    val observeClient: ObserveClient,
)
