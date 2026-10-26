package com.example.nexum_cliente.data.report

import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.data.report.remote.ReportRemoteDataSource
import com.example.nexum_cliente.data.report.remote.payload.req.CreateReportReq
import com.example.nexum_cliente.data.report.remote.payload.res.ReportRes
import com.example.nexum_cliente.domain.repository.ReportRepository
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
class ReportRepositoryImpl @Inject constructor(
    private val remoteDataSource: ReportRemoteDataSource
) : ReportRepository {

    override fun createReport(req: CreateReportReq): Flow<ApiResponse<ReportRes>> =
        remoteDataSource.createReport(req)

    override fun getReportsByContract(contractId: Long): Flow<ApiResponse<List<ReportRes>>> =
        remoteDataSource.getReportsByContract(contractId)

    override fun getMyReports(): Flow<ApiResponse<List<ReportRes>>> =
        remoteDataSource.getMyReports()
}
