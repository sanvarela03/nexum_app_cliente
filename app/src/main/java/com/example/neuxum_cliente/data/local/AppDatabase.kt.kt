package com.example.neuxum_cliente.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.neuxum_cliente.data.market_location.local.MarketLocationDao
import com.example.neuxum_cliente.data.market_location.local.MarketLocationEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider

/**
 * AppDatabase.kt
 *
 * Central Room database used for mocking / local storage of Market Locations.
 * Created to be used with the provided Hilt DatabaseModule and a Provider<MarketLocationDao>
 * so the pre-population callback can lazily obtain the DAO without circular injection.
 *
 * @author Ernesto Bastidas Pulido
 * @since 18/09/2025
 */

@Database(
    entities = [MarketLocationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun marketLocationDao(): MarketLocationDao

    companion object {
        // Volatile to ensure visibility across threads
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Returns a singleton AppDatabase instance.
         * This variant accepts a Provider<MarketLocationDao> and an applicationScope so the
         * prepopulation callback can insert data after the DB is created without causing
         * injection cycles.
         */
        fun getDatabase(
            context: Context,
            marketLocationDaoProvider: Provider<MarketLocationDao>,
            applicationScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "neuxum_cliente_database.db"
                )
                    .addCallback(AppDatabaseCallback(marketLocationDaoProvider, applicationScope))
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}

/**
 * Callback used to pre-populate the database after creation. Uses a Provider<MarketLocationDao>
 * to avoid a circular dependency when wiring with Hilt.
 */
class AppDatabaseCallback(
    private val marketLocationDaoProvider: Provider<MarketLocationDao>,
    private val applicationScope: CoroutineScope
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // Launching on IO dispatcher to avoid blocking main thread
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
            // add or change mock locations as needed
        )

        // Obtain DAO lazily via provider
        val dao = marketLocationDaoProvider.get()
        dao.saveAll(predefinedLocations)
    }
}
