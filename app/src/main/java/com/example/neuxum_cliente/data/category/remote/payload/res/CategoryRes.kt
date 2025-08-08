package com.example.neuxum_cliente.data.category.remote.payload.res


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/5/2025
 * @version 1.0
 */
data class CategoryRes(
    val id: Long,
    val name: String?,
    val description: String?,
    val imageUrl: String?,
    val serviceType: String?,
    val iconName: String?,
)