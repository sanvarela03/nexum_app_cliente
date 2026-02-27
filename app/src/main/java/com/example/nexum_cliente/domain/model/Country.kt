package com.example.nexum_cliente.domain.model

data class Country(
    val id: Long,
    val name: String?,
    val code: String?,
    val phoneCode: String?,
    val flagEmoji: String?,
    val phoneCheckRegex: String?,
)
