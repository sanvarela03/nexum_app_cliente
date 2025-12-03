package com.example.nexum_cliente.data.market_location.local

import androidx.room.RoomDatabase


/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 18/09/2025
 * @version 1.0
 */

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.nexum_cliente.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Database is usually a singleton
object DatabaseModule {

    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {
        // SupervisorJob makes sure that if one coroutine fails, others are not cancelled.
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        // Use Provider for lazy injection of DAO into the callback to avoid circular dependency
        marketLocationDaoProvider: Provider<MarketLocationDao>,
        applicationScope: CoroutineScope
    ): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "neuxum_cliente_database.db" // Choose a suitable name for your DB file
        )
            .addCallback(
                AppDatabaseCallback( // Renamed for clarity, or keep PrepopulateCallback
                    marketLocationDaoProvider,
                    applicationScope
                )
            )
            .build()
    }
}

// You can define the callback class here or in its own file (e.g., PrepopulateCallback.kt)
private class AppDatabaseCallback(
    private val marketLocationDaoProvider: Provider<MarketLocationDao>,
    private val applicationScope: CoroutineScope
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch(Dispatchers.IO) {
            populateDatabase()
        }
    }

    private suspend fun populateDatabase() {
        val predefinedLocations = listOf(
            MarketLocationEntity(1, "Bogotá", "Cundinamarca", "Colombia", "CO"),
            MarketLocationEntity(2, "Barranquilla", "Atlántico", "Colombia", "CO"),
            MarketLocationEntity(3, "Chía", "Cundinamarca", "Colombia", "CO"),
            MarketLocationEntity(4, "Medellín", "Antioquia", "Colombia", "CO")
            // ... add all your locations
        )
        // Get DAO instance via provider inside the coroutine, after DB is created
        marketLocationDaoProvider.get().saveAll(predefinedLocations)
    }
}