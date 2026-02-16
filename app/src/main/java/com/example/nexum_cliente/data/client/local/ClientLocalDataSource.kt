package com.example.nexum_cliente.data.client.local

<<<<<<< Updated upstream
=======
import com.example.nexum_trabajador.data.local_data_source.LocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

>>>>>>> Stashed changes

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 1/6/2026
 * @version 1.0
 */
<<<<<<< Updated upstream
class ClientLocalDataSource {
=======
@Singleton
class ClientLocalDataSource @Inject constructor(
    private val clientDao: ClientDao
) : LocalDataSource<ClientEntity> {
    override suspend fun hasResource(): Boolean {
        return clientDao.getAll().isNotEmpty()
    }

    override suspend fun getAll(): List<ClientEntity> {
        return clientDao.getAll()
    }

    override fun observe(): Flow<List<ClientEntity>> {
        return clientDao.observe()
    }

    override suspend fun replaceAll(resources: List<ClientEntity>) {
        clientDao.replaceAll(resources)
    }

    override suspend fun clearAll() {
        clientDao.clearAll()
    }
>>>>>>> Stashed changes
}