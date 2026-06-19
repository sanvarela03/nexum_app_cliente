package com.example.nexum_cliente.domain.use_cases.nearby_workers

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class NearbyWorkersUseCases @Inject constructor(
    val updateNearbyWorkers: UpdateNearbyWorkers,
    val observeNearbyWorkers: ObserveNearbyWorkers
)
