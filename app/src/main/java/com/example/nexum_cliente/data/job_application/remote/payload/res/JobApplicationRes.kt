package com.example.nexum_cliente.data.job_application.remote.payload.res

import com.google.gson.annotations.SerializedName

data class JobApplicationRes(
    @SerializedName("job_application_id")
    val jobApplicationId: Long? = null,
    @SerializedName("worker_id")
    val workerId: Long? = null,
    @SerializedName("worker_user_id")
    val workerUserId: Long? = null,
    @SerializedName("worker_name")
    val workerName: String? = null,
    @SerializedName("worker_image_url")
    val workerImageUrl: String? = null,
    @SerializedName("worker_bio")
    val workerBio: String? = null,
    @SerializedName("worker_average_rating")
    val workerAverageRating: Double? = null,
    @SerializedName("worker_total_reviews")
    val workerTotalReviews: Int? = null,
    @SerializedName("client_id")
    val clientId: Long? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("labor_cost")
    val laborCost: Double? = null,
    @SerializedName("materials_cost")
    val materialsCost: Double? = null,
    @SerializedName("total_cost")
    val totalCost: Double? = null,
    @SerializedName("proposal_text")
    val proposalText: String? = null,
    @SerializedName("distance_in_meters")
    val distanceInMeters: Double? = null,
    @SerializedName("scheduled_date_time")
    val scheduledDateTime: String? = null,
    @SerializedName("applied_at")
    val appliedAt: String? = null,
    @SerializedName("work_schedules")
    val workSchedules: List<WorkScheduleRes>? = null,
    @SerializedName("materials")
    val materials: List<MaterialRes>? = null
)
