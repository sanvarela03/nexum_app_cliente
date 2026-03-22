package com.example.nexum_cliente.data.category.mapper

import com.example.nexum_cliente.data.category.local.CategoryEntity
import com.example.nexum_cliente.data.category.remote.payload.res.CategoryRes
import com.example.nexum_cliente.data.mapper.EntityMapper


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 3/9/2026
 * @version 1.0
 */
object CategoryMapper : EntityMapper<CategoryRes, CategoryEntity> {
    override fun toEntity(dto: CategoryRes): CategoryEntity {
        return CategoryEntity(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            imageUrl = dto.imageUrl,
            serviceType = dto.serviceType,
            iconName = dto.iconName,
        )
    }

}