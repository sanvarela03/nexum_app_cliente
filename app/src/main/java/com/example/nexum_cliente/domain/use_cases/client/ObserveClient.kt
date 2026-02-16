package com.example.nexum_cliente.domain.use_cases.client

import com.example.nexum_cliente.domain.repository.ClientRepository
import javax.inject.Inject


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/3/2025
 * @version 1.0
 */
class ObserveClient @Inject constructor(
    private val repository: ClientRepository
) {
    operator fun invoke() = repository.observe()
}