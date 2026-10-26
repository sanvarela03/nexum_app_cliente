package com.example.nexum_cliente.domain.use_cases.report

import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.data.report.remote.payload.res.ReportRes
import com.example.nexum_cliente.domain.repository.ReportRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/27/2026
 * @version 1.0
 */
class GetReportsByContractUseCase @Inject constructor(
    private val repository: ReportRepository
) {
    operator fun invoke(contractId: Long): Flow<ApiResponse<List<ReportRes>>> =
        repository.getReportsByContract(contractId)
}
