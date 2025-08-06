package com.example.neuxum_cliente.di.modules

import android.app.Application
import androidx.room.Room
import com.example.neuxum_cliente.data.client.local.db.ClientDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/2/2025
 * @version 1.0
 */
@Module
@InstallIn(SingletonComponent::class)
object DbModule {
    @Provides
    @Singleton
    fun provideClientDb(app: Application): ClientDb {
        return Room.databaseBuilder(
            app,
            ClientDb::class.java,
            "client.db"
        ).build()
    }
}