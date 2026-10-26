package com.example.nexum_cliente.domain.use_cases.report

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
data class ReportUseCases @Inject constructor(
    val createReport: CreateReportUseCase,
    val getReportsByContract: GetReportsByContractUseCase,
    val getMyReports: GetMyReportsUseCase
)
