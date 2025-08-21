package com.example.neuxum_cliente.data.category.local

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/5/2025
 * @version 1.0
 */
@Entity
data class CategoryEntity(
    @PrimaryKey
    val id: Long,
    val name: String?,
    val description: String?,
    val imageUrl: String?,
    val serviceType: String?,
    val iconName: String?,
)