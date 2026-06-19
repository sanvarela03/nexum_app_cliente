package com.example.nexum_cliente.domain.use_cases.nearby_workers

import com.example.nexum_cliente.domain.repository.NearbyWorkersRepository
import javax.inject.Inject


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 5/5/2026
 * @version 1.0
 */
class UpdateNearbyWorkers @Inject constructor(
    private val nearbyWorkersRepository: NearbyWorkersRepository
) {
    suspend operator fun invoke(
        jobOfferId: Long,
        radiusKm: Int,
        fetchFromRemote: Boolean
    ) = nearbyWorkersRepository.update(
        jobOfferId = jobOfferId,
        radiusKm = radiusKm,
        fetchFromRemote = fetchFromRemote
    )
}