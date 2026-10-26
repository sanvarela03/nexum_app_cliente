package com.example.nexum_cliente.domain.use_cases.job_application

import com.example.nexum_cliente.data.notification.mapper.NotificationMapper
import com.example.nexum_cliente.domain.use_cases.notification.NotificationUseCases
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/6/2026
 * @version 1.0
 */
@Singleton
class ObserveJobApplications @Inject constructor(
    private val notificationsUseCase: NotificationUseCases
) {
    operator fun invoke(jobOfferId: String) = notificationsUseCase.getNotifications()
        .map { notifications ->
            val filtered = notifications.filter { it.type == "NEW_JOB_APPLICATION" && it.jobOfferId == jobOfferId }
            NotificationMapper.toJobApplication(filtered)
        }
}