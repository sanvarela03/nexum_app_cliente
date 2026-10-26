package com.example.nexum_cliente.data.report.remote.payload.req

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/27/2026
 * @version 1.0
 */
data class CreateReportReq(
    val contractId: Long,
    val reason: String
)
