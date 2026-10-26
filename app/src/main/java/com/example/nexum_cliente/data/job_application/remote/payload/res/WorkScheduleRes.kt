package com.example.nexum_cliente.data.job_application.remote.payload.res

import com.google.gson.annotations.SerializedName

data class WorkScheduleRes(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("work_date")
    val workDate: String? = null,
    @SerializedName("start_time")
    val startTime: String? = null,
    @SerializedName("end_time")
    val endTime: String? = null
)
