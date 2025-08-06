package com.example.neuxum_cliente.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/2/2025
 * @version 1.0
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher // Para operaciones de entrada/salida (I/O)

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher // Para operaciones de procesamiento de datos de larga duración (CPU-intensive)

@Module
@InstallIn(SingletonComponent::class)
class DispatcherModule {
    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}