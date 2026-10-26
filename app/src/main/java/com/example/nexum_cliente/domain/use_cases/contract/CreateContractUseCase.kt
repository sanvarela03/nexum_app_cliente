package com.example.nexum_cliente.domain.use_cases.contract

import com.example.nexum_cliente.data.contract.remote.payload.req.NewContractReq
import com.example.nexum_cliente.data.contract.remote.payload.res.ContractRes
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.domain.repository.ContractRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/27/2026
 * @version 1.0
 */
class CreateContractUseCase @Inject constructor(
    private val repository: ContractRepository
) {
    operator fun invoke(req: NewContractReq): Flow<ApiResponse<ContractRes>> =
        repository.createContract(req)
}
