package com.example.nexum_cliente.domain.repository

import com.example.nexum_cliente.data.contract.remote.payload.req.NewContractReq
import com.example.nexum_cliente.data.contract.remote.payload.req.PayLaborReq
import com.example.nexum_cliente.data.contract.remote.payload.res.ContractRes
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import kotlinx.coroutines.flow.Flow

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/27/2026
 * @version 1.0
 */
interface ContractRepository {
    fun createContract(req: NewContractReq): Flow<ApiResponse<ContractRes>>
    fun getContractById(id: Long): Flow<ApiResponse<ContractRes>>
    fun getMyContracts(): Flow<ApiResponse<List<ContractRes>>>
    fun scheduleMeeting(id: Long, date: String, h3Index: String): Flow<ApiResponse<ContractRes>>
    fun generateVerificationCode(id: Long): Flow<ApiResponse<Map<String, String>>>
    fun payLabor(id: Long, req: PayLaborReq): Flow<ApiResponse<ContractRes>>
}
