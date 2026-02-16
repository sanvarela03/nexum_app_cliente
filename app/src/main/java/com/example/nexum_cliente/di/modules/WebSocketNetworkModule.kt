package com.example.nexum_cliente.di.modules

<<<<<<< Updated upstream
object WebSocketNetworkModule {
=======
import com.example.nexum_cliente.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WebSocketUrl

@Module
@InstallIn(SingletonComponent::class)
object WebSocketNetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(json: Json): HttpClient { // 1. Inyectamos la instancia de Json
        return HttpClient(CIO) {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(WebSockets)
            install(ContentNegotiation) {
                json(json) // 2. Usamos la instancia de Json personalizada
            }
        }
    }

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = true
            encodeDefaults = true
            coerceInputValues = true
        }
    }

    @Provides
    @Singleton
    @WebSocketUrl
    fun provideWebSocketUrl(): String = BuildConfig.WS_URL

    @Provides
    @Singleton
    @BaseUrl
    fun provideBaseUrl(): String = BuildConfig.BASE_URL
>>>>>>> Stashed changes
}