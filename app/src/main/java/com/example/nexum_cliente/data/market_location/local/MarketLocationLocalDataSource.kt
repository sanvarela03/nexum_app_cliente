<<<<<<< Updated upstream
package com.example.nexum_trabajador.data.market_location.local
=======
package com.example.nexum_cliente.data.market_location.local
>>>>>>> Stashed changes

import com.example.nexum_trabajador.data.local_data_source.LocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
<<<<<<< Updated upstream
=======
import kotlin.collections.isNotEmpty
>>>>>>> Stashed changes


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/9/2025
 * @version 1.0
 */
@Singleton
class MarketLocationLocalDataSource @Inject constructor(
<<<<<<< Updated upstream
    private val marketLocationDao: MarketLocationDao
=======
    private val dao: MarketLocationDao
>>>>>>> Stashed changes
) : LocalDataSource<MarketLocationEntity> {

    override suspend fun hasResource(): Boolean {
        return getAll().isNotEmpty()
    }

    override suspend fun getAll(): List<MarketLocationEntity> {
<<<<<<< Updated upstream
        return marketLocationDao.getAll()
    }

    override fun observe(): Flow<List<MarketLocationEntity>> {
        return marketLocationDao.observe()
    }

    override suspend fun replaceAll(resources: List<MarketLocationEntity>) {
        marketLocationDao.replaceAll(resources)
    }

    override suspend fun clearAll() {
        marketLocationDao.clearAll()
=======
        return dao.getAll()
    }

    override fun observe(): Flow<List<MarketLocationEntity>> {
        return dao.observe()
    }

    override suspend fun replaceAll(resources: List<MarketLocationEntity>) {
        dao.replaceAll(resources)
    }

    override suspend fun clearAll() {
        dao.clearAll()
>>>>>>> Stashed changes
    }
}