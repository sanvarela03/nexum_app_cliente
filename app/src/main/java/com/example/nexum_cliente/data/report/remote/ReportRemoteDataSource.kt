package com.example.nexum_cliente.data.report.remote

import com.example.nexum_cliente.common.apiRequestFlow
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.data.report.remote.payload.req.CreateReportReq
import com.example.nexum_cliente.data.report.remote.payload.res.ReportRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/27/2026
 * @version 1.0
 */
@Singleton
class ReportRemoteDataSource @Inject constructor(
    private val api: ReportApi
) {
    fun createReport(req: CreateReportReq): Flow<ApiResponse<ReportRes>> =
        apiRequestFlow { api.createReport(req) }

    fun getReportsByContract(contractId: Long): Flow<ApiResponse<List<ReportRes>>> =
        apiRequestFlow { api.getReportsByContract(contractId) }

    fun getMyReports(): Flow<ApiResponse<List<ReportRes>>> =
        apiRequestFlow { api.getMyReports() }
}
