# Neuxum Cliente - Aplicación Android

Esta es la aplicación oficial para los clientes de **Neuxum**, diseñada para facilitar la búsqueda y contratación de servicios profesionales, gestión de solicitudes y comunicación en tiempo real.

## 🚀 Características Principales

*   **Autenticación Segura:** Registro e inicio de sesión integrados con gestión de tokens (JWT).
*   **Mensajería en Tiempo Real:** Chat bidireccional mediante WebSockets con soporte para estados de lectura, entrega y notificaciones de escritura.
*   **Gestión de Solicitudes:** Creación y seguimiento de ofertas de trabajo y solicitudes de servicio.
*   **Exploración de Categorías:** Buscador de profesionales por categorías y servicios especializados.
*   **Pagos Integrados:** Integración con Mercado Pago para transacciones seguras dentro de la plataforma.
*   **Perfiles de Usuario:** Gestión de información personal y preferencias del cliente.
*   **Mapas y Geolocalización:** Visualización de servicios y profesionales cercanos.

## 🛠️ Stack Tecnológico

*   **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) - Interfaz moderna y declarativa.
*   **Arquitectura:** [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) con patrón MVVM.
*   **Inyección de Dependencias:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android).
*   **Networking:** [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/) para APIs REST; WebSockets para mensajería.
*   **Persistencia Local:** [Room Database](https://developer.android.com/training/data-storage/room) y [DataStore](https://developer.android.com/topic/libraries/architecture/datastore).
*   **Imágenes:** [Glide](https://github.com/bumptech/glide) con soporte para Compose.
*   **Concurrencia:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html).

## 📂 Estructura del Proyecto

El proyecto sigue una estructura modular basada en capas:

*   `data/`: Repositorios, fuentes de datos (locales y remotas), DTOs y mapeadores.
*   `domain/`: Modelos de negocio, interfaces de repositorios y casos de uso (UseCases).
*   `ui/`: Pantallas (Screens), ViewModels, componentes reutilizables y temas (Compose).
*   `security/`: Gestión de seguridad, cifrado y autenticación.
*   `di/`: Módulos de configuración para Hilt.

## 📝 Notas de Versión Recientes (v4.5+)

*   **Corrección de Errores Críticos:** Se solucionó el problema de `IllegalArgumentException` relacionado con claves duplicadas en `LazyColumn` dentro de las pantallas de Chat y Conversaciones.
*   **Optimización de Mensajería:** Implementación de actualizaciones atómicas de estado y filtrado de duplicados en tiempo real.
*   **Mejoras en UI:** Mejora en el manejo de imágenes de perfil y placeholders dinámicos.

---
**Autor:** Santiago Varela Daza
**Contacto:** svarela03@uan.edu.co
**GitHub:** [sanvarela03](https://github.com/sanvarela03)
