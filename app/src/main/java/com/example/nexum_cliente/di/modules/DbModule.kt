package com.example.nexum_cliente.di.modules

import android.app.Application
import androidx.room.Room
import com.example.nexum_cliente.data.client.local.db.ClientDb
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
        )

//                    db.execSQL("INSERT INTO MarketLocationEntity (id, city, country, state, countryCode) VALUES (1, 'Bogotá', 'Colombia', 'Cundinamarca', 'CO')")
//                    db.execSQL("INSERT INTO MarketLocationEntity (id, city, country, state, countryCode) VALUES (2, 'Barranquilla', 'Colombia', 'Atlántico', 'CO')")
//                    db.execSQL("INSERT INTO MarketLocationEntity (id, city, country, state, countryCode) VALUES (3, 'Chía', 'Colombia', 'Cundinamarca', 'CO')")
//                    db.execSQL("INSERT INTO MarketLocationEntity (id, city, country, state, countryCode) VALUES (4, 'Medellín', 'Colombia', 'Antioquia', 'CO')")
//                    db.execSQL("INSERT INTO MarketLocationEntity (id, city, country, state, countryCode) VALUES (5, 'Ciudad de México', 'México', 'Ciudad de México', 'MX')")
               .build()
    }
}

//.addCallback(
//object : RoomDatabase.Callback() {
//    override fun onCreate(db: SupportSQLiteDatabase) {
//        super.onCreate(db)
//        // Aquí puedes ejecutar SQL crudo
//
//        Log.d("DbModule", "onCreate:")
//    }
//}
//)