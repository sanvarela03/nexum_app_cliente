package com.example.nexum_cliente.data.category.local

import android.util.Log
import com.example.nexum_trabajador.data.local_data_source.LocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 3/9/2026
 * @version 1.0
 */
@Singleton
class CategoryLocalDataSource @Inject constructor(
    private val categoryDao: CategoryDao
) : LocalDataSource<CategoryEntity> {
    override suspend fun hasResource(): Boolean {
        return categoryDao.getAllCategories().isNotEmpty()
    }

    override suspend fun getAll(): List<CategoryEntity> {
        return categoryDao.getAllCategories()
    }

    override fun observe(): Flow<List<CategoryEntity>> {
        return categoryDao.observeCategories()
    }

    override suspend fun replaceAll(resources: List<CategoryEntity>) {
        Log.d("CategoryLocalDataSource", "replaceAll: $resources")
        categoryDao.replaceCategories(resources)
    }

    override suspend fun clearAll() {
        categoryDao.clearAll()
    }
}