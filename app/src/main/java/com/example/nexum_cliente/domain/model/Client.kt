package com.example.nexum_cliente.domain.model


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 1/6/2026
 * @version 1.0
 */
<<<<<<< Updated upstream
class Client {
}
=======
data class Client(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val profilePictureUrl: String,
    val email: String,
    val username: String,
    val phone: String,
    val isEnabled: Boolean,
    val profileVerified: Boolean,
    val verificationCode: String?,
    val verificationCodeTimestamp: String?,
    val firebaseToken: String?,
    val dateJoined: String,
    val lastLogin: String,
)
>>>>>>> Stashed changes
