<<<<<<< Updated upstream
package com.example.nexum_trabajador.data.local_storage
=======
package com.example.nexum_cliente.data.local_storage
>>>>>>> Stashed changes

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz genérica para operaciones básicas de base de datos.
 * @param T La clase de la entidad (Entity).
 */
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T)
<<<<<<< Updated upstream
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<T>)
    @Update
    suspend fun update(entity: T)
    @Delete
    suspend fun delete(entity: T)
    suspend fun clearAll()
    fun observe(): Flow<List<T>>
}
=======

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<T>)

    @Update
    suspend fun update(entity: T)

    @Delete
    suspend fun delete(entity: T)
}

interface ReadableDao<T, ID> {
    suspend fun getAll(): List<T>
    suspend fun getById(id: ID): T?
    fun observe(): Flow<List<T>>
    suspend fun clearAll()
    suspend fun replaceAll(entities: List<T>)
}
>>>>>>> Stashed changes
