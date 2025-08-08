package com.example.neuxum_cliente.data.category.parser

import com.example.neuxum_cliente.data.category.local.CategoryEntity
import com.example.neuxum_cliente.data.category.remote.payload.res.CategoryRes

object CategoryParser {
    fun toEntity(res: CategoryRes): CategoryEntity = CategoryEntity(
        id = res.id,
        name = res.name,
        description = res.description,
        imageUrl = res.imageUrl,
        serviceType = res.serviceType,
        iconName = res.iconName,
    )

    fun toEntity(res: List<CategoryRes>): List<CategoryEntity> = res.map { toEntity(it) }
}