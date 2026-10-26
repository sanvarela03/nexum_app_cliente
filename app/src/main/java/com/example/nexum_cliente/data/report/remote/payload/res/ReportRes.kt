package com.example.nexum_cliente.data.report.remote.payload.res

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/27/2026
 * @version 1.0
 */
data class ReportRes(
    val id: Long,
    val contractId: Long,
    val openedById: Long,
    val openedByUsername: String?,
    val reason: String,
    val resolution: String?,
    val createdAt: String?,
    val updatedAt: String?
)
