package com.example.nexum_cliente.data.contract

import com.example.nexum_cliente.data.contract.remote.ContractRemoteDataSource
import com.example.nexum_cliente.data.contract.remote.payload.req.NewContractReq
import com.example.nexum_cliente.data.contract.remote.payload.req.PayLaborReq
import com.example.nexum_cliente.data.contract.remote.payload.res.ContractRes
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.domain.repository.ContractRepository
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
class ContractRepositoryImpl @Inject constructor(
    private val remoteDataSource: ContractRemoteDataSource
) : ContractRepository {

    override fun createContract(req: NewContractReq): Flow<ApiResponse<ContractRes>> =
        remoteDataSource.createContract(req)

    override fun getContractById(id: Long): Flow<ApiResponse<ContractRes>> =
        remoteDataSource.getContractById(id)

    override fun getMyContracts(): Flow<ApiResponse<List<ContractRes>>> =
        remoteDataSource.getMyContracts()

    override fun scheduleMeeting(id: Long, date: String, h3Index: String): Flow<ApiResponse<ContractRes>> =
        remoteDataSource.scheduleMeeting(id, date, h3Index)

    override fun generateVerificationCode(id: Long): Flow<ApiResponse<Map<String, String>>> =
        remoteDataSource.generateVerificationCode(id)

    override fun payLabor(id: Long, req: PayLaborReq): Flow<ApiResponse<ContractRes>> =
        remoteDataSource.payLabor(id, req)
}
