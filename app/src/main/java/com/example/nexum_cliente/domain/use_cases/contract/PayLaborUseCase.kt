package com.example.nexum_cliente.domain.use_cases.contract

import com.example.nexum_cliente.data.contract.remote.payload.req.PayLaborReq
import com.example.nexum_cliente.data.contract.remote.payload.res.ContractRes
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.domain.repository.ContractRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @version 1.0
 */
class PayLaborUseCase @Inject constructor(
    private val repository: ContractRepository
) {
    operator fun invoke(
        contractId: Long,
        req: PayLaborReq
    ): Flow<ApiResponse<ContractRes>> =
        repository.payLabor(contractId, req)
}
