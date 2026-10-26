package com.example.nexum_cliente.data.report.remote

import com.example.nexum_cliente.data.report.remote.payload.req.CreateReportReq
import com.example.nexum_cliente.data.report.remote.payload.res.ReportRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/27/2026
 * @version 1.0
 */
interface ReportApi {

    @POST("/api/v1/reports")
    suspend fun createReport(@Body req: CreateReportReq): Response<ReportRes>

    @GET("/api/v1/reports/contract/{contractId}")
    suspend fun getReportsByContract(@Path("contractId") contractId: Long): Response<List<ReportRes>>

    @GET("/api/v1/reports/my-reports")
    suspend fun getMyReports(): Response<List<ReportRes>>
}
