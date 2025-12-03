package com.example.nexum_cliente.data.country.local
import com.example.nexum_cliente.data.country.mapper.CountryMapper
import com.example.nexum_cliente.data.country.remote.payload.res.CountryRes
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
) {
    suspend fun getAllCountries(): List<CountryEntity> {
        return countryDao.getAll()
    }

    fun observeCountries(): Flow<List<CountryEntity>> {
        return countryDao.observe()
    }

    suspend fun replaceCountries(countries: List<CountryRes>) {
        countryDao.replaceAll(CountryMapper.toEntity(countries))
    }

    suspend fun hasCountries(): Boolean {
        return getAllCountries().isNotEmpty()
    }

    suspend fun clearAll() {
        countryDao.clearAll()
    }

}