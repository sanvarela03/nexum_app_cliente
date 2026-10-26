package com.example.nexum_cliente.domain.repository

import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.data.report.remote.payload.req.CreateReportReq
import com.example.nexum_cliente.data.report.remote.payload.res.ReportRes
import kotlinx.coroutines.flow.Flow

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/27/2026
 * @version 1.0
 */
interface ReportRepository {
    fun createReport(req: CreateReportReq): Flow<ApiResponse<ReportRes>>
    fun getReportsByContract(contractId: Long): Flow<ApiResponse<List<ReportRes>>>
    fun getMyReports(): Flow<ApiResponse<List<ReportRes>>>
}
