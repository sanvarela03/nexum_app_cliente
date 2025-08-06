package com.example.neuxum_cliente.data.client.parser

import com.example.neuxum_cliente.data.client.local.ClientEntity
import com.example.neuxum_cliente.data.client.remote.payload.res.ClientRes


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/2/2025
 * @version 1.0
 */
object ClientParser {
    fun toEntity(res: ClientRes): ClientEntity = ClientEntity(
        clientId = res.clientId,
        userId = res.user.userId,
        firstName = res.user.firstName,
        lastName = res.user.lastName,
        username = res.user.username,
        email = res.user.email,
        verificationCode = res.user.verificationCode,
        verificationCodeTimestamp = res.user.verificationCodeTimestamp,
        firebaseToken = res.user.firebaseToken,
        phone = res.user.phone,
        isEnabled = res.user.isEnabled,
        dateJoined = res.user.dateJoined,
        lastLogin = res.user.lastLogin,
        imgUrl = res.user.imgUrl,
    )
}