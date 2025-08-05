package com.example.neuxum_cliente.data.client.remote.payload.res


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/2/2025
 * @version 1.0
 */
data class ClientRes(
    val clientId: Long,
    val profileVerified: Boolean,
    val documentUrl: String,
    val user: UserRes
)