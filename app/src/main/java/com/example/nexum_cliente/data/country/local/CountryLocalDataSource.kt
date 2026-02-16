package com.example.nexum_cliente.data.country.local

import com.example.nexum_trabajador.data.local_data_source.LocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/2/2025
 * @version 1.0
 */
class CountryLocalDataSource @Inject constructor(
    private val countryDao: CountryDao
) : LocalDataSource<CountryEntity> {
    override suspend fun clearAll() { countryDao.clearAll() }
    override suspend fun hasResource(): Boolean = getAll().isNotEmpty()
    override suspend fun getAll(): List<CountryEntity> = countryDao.getAll()

    override fun observe(): Flow<List<CountryEntity>> = countryDao.observe()

    override suspend fun replaceAll(resources: List<CountryEntity>) {
        countryDao.replaceAll(resources)
    }
}