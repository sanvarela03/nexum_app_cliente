package com.example.nexum_cliente.data.contract.remote

import com.example.nexum_cliente.data.contract.remote.payload.req.NewContractReq
import com.example.nexum_cliente.data.contract.remote.payload.req.PayLaborReq
import com.example.nexum_cliente.data.contract.remote.payload.res.ContractRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/27/2026
 * @version 1.0
 */
interface ContractApi {

    @POST("/api/v1/contracts")
    suspend fun createContract(@Body req: NewContractReq): Response<ContractRes>

    @GET("/api/v1/contracts/{id}")
    suspend fun getContractById(@Path("id") id: Long): Response<ContractRes>

    @GET("/api/v1/contracts/my-contracts")
    suspend fun getMyContracts(): Response<List<ContractRes>>

    @POST("/api/v1/contracts/{id}/schedule-meeting")
    suspend fun scheduleMeeting(
        @Path("id") id: Long,
        @Query("date") date: String,
        @Query("h3Index") h3Index: String
    ): Response<ContractRes>

    @POST("/api/v1/contracts/{id}/generate-verification-code")
    suspend fun generateVerificationCode(@Path("id") id: Long): Response<Map<String, String>>

    @POST("/api/v1/contracts/{id}/pay-labor")
    suspend fun payLabor(
        @Path("id") id: Long,
        @Body req: PayLaborReq
    ): Response<ContractRes>
}
