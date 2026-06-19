# Auditoría de Seguridad - Nexum Cliente

## Fase 1 - Reconocimiento
**Stack Tecnológico:**
* **Lenguaje:** Kotlin (Android)
* **Framework:** Jetpack Compose, Hilt (Inyección de Dependencias), MVVM.
* **Red/API:** Retrofit, Ktor (WebSockets), OkHttp.
* **Almacenamiento Local:** Room (Base de datos local), Preferences DataStore (Preferencias).
* **Servicios de Terceros:** Firebase (Messaging, Storage), Google Maps SDK, Mercado Pago.

**Puntos de Entrada:**
* **API Endpoints:** Definidos en interfaces de Retrofit como `AuthApi`, `NearbyWorkersApi`, etc.
* **WebSockets:** Implementado con Ktor en `WebSocketManager.kt` utilizando protocolo STOMP para chat y tiempo real.
* **Intents / Receivers:** Broadcast Receivers para notificaciones push (`AcceptBroadcastReceiver`, `RejectBroadcastReceiver`, `MyFirebaseMessagingService`).

**Autenticación y Autorización:**
* **Autenticación:** Basada en tokens JWT (Access Token y Refresh Token).
* **Gestión de Sesiones:** Uso de Interceptors (`AuthInterceptor`) y Authenticators (`AuthAuthenticator`) de OkHttp para inyectar y rotar tokens.

**Manejo de Archivos:**
* Carga de imágenes hacia Firebase Storage (`FirebaseStorageService.kt`) con compresión forzada a `.webp`.

---

## Fase 2 y 3 - Análisis y Generación de Hallazgos

### 1. Mass Assignment / Escalada de Privilegios
* **Severidad:** Crítica
* **Condición:** El payload de registro en `SignUpReq.kt` contiene un atributo `private val roles: List<String> = listOf("ROLE_USER", "ROLE_CLIENT")`. Debido a que Gson serializa este campo por defecto, la petición enviada al backend incluye el array de roles dictado por el cliente, lo que podría permitir a un atacante inyectar roles adicionales.
* **Criterio:** OWASP Top 10 A08:2021 - Software and Data Integrity Failures / Broken Access Control.
* **Prueba de Concepto (PoC):**
  Interceptar la petición `POST /api/v1/auth/sign-up` con una herramienta como Burp Suite o ProxyMan. Modificar el JSON enviado:
  ```json
  {
    "username": "attacker",
    "email": "attacker@email.com",
    "roles": ["ROLE_ADMIN"],
    "password": "Password123!",
    "market_location_id": 1
  }
  ```
  Si el backend no sanitiza este input, el usuario será creado con permisos de administrador.

### 2. Almacenamiento Inseguro de Tokens (Insecure Data Storage)
* **Severidad:** Alta
* **Condición:** Los tokens de acceso (JWT) y de refresco son almacenados en texto plano utilizando `Preferences DataStore` (`TokenManager.kt` y `DataStore.kt`). No se utiliza encriptación en reposo.
* **Criterio:** OWASP Mobile Top 10 M2: Insecure Data Storage.
* **Prueba de Concepto (PoC):**
  En un dispositivo rooteado o mediante extracción de datos de aplicación, extraer el archivo:
  `/data/data/com.example.neuxum_cliente/files/datastore/data_store.preferences_pb`
  Al abrir el archivo, los tokens JWT se encuentran expuestos en texto plano.

### 3. Exposición de Información Sensible en Logs (Sensitive Data Exposure)
* **Severidad:** Alta
* **Condición:** La aplicación tiene habilitados múltiples niveles de logging profundo que imprimen información confidencial en el Logcat. `AuthAuthenticator.kt` incluye un `HttpLoggingInterceptor` configurado en `Level.BODY`. `WebSocketNetworkModule.kt` usa `LogLevel.ALL`. Además, se imprimen explícitamente los tokens en `TokenManager.kt` (`Log.d("TokenManager", "Saving Access Token: $token")`), `AuthAuthenticator.kt` y `AuthInterceptor.kt` (`Log.d("INTERCEP THOR !!!", "$accessToken")`).
* **Criterio:** OWASP Mobile Top 10 M9: Reverse Engineering (Exposure via logging).
* **Prueba de Concepto (PoC):**
  Conectar el dispositivo a Android Studio o usar `adb logcat`. Filtrar por `TokenManager` o `INTERCEP THOR`. Las credenciales y payloads de chat de los WebSockets aparecerán en texto plano.

### 4. Secretos Hardcodeados (Hardcoded Credentials)
* **Severidad:** Media
* **Condición:** Se ha encontrado un `APP_KEY` hardcodeado en `Constants.kt` y la API Key de Google Maps expuesta en texto plano en el `AndroidManifest.xml`.
* **Criterio:** OWASP Top 10 A07:2021 - Identification and Authentication Failures.
* **Prueba de Concepto (PoC):**
  Extraer el APK y descompilarlo usando `jadx-gui`.
  Revisar `com/example/nexum_cliente/common/Constants.kt` para visualizar `const val APP_KEY = "a3f2c1b1-8e4b-4a6d-8b9e-0c1f2a3b4d5e"`.

### 5. Configuración de Red Insegura (Cleartext Traffic)
* **Severidad:** Media
* **Condición:** En el archivo `AndroidManifest.xml`, el atributo `android:usesCleartextTraffic="true"` está habilitado globalmente para la aplicación. Esto permite que la app realice conexiones HTTP sin encriptar en entornos ajenos a local (si no se controla), dejándola susceptible a ataques de Intermediario (Man-in-the-Middle).
* **Criterio:** OWASP Mobile Top 10 M3: Insecure Communication.
* **Prueba de Concepto (PoC):**
  Inspeccionar `AndroidManifest.xml`:
  ```xml
  <application ... android:usesCleartextTraffic="true">
  ```

### 6. Vulnerabilidad IDOR en Gestión de Archivos y Falta de Validación
* **Severidad:** Media
* **Condición:** En `FirebaseStorageService.kt`, el método `deleteImage` acepta cualquier `imageUrl` arbitraria proporcionada desde la app para eliminar un archivo del Storage de manera directa. Además, `uploadImage` concatena ciegamente la extensión `.webp` ignorando el tipo MIME real del archivo y expone el nombre de usuario de forma cruda al nombre del archivo, pudiendo generar problemas de codificación o colisión si no hay validación de caracteres.
* **Criterio:** OWASP Top 10 A01:2021 - Broken Access Control (IDOR).
* **Prueba de Concepto (PoC):**
  Un atacante intercepta la función o utiliza un cliente modificado para invocar `FirebaseStorageService.deleteImage("URL_DE_IMAGEN_DE_OTRO_USUARIO")`, logrando eliminar archivos ajenos del bucket de Firebase (si las Reglas de Storage confían en la llamada del cliente).

---

## Fase 4 - Plan de Remediación

### 1. Mass Assignment / Escalada de Privilegios
* **Recomendación Técnica:**
  Elimina el campo `roles` del DTO `SignUpReq.kt`.
  ```kotlin
  // Eliminar o ignorar serialización de: private val roles: List<String> = listOf("ROLE_USER", "ROLE_CLIENT")
  ```
  Si se declara, hazlo con la etiqueta `@Transient` para que Gson lo evada, pero es mejor sacarlo del `data class` por completo.
* **Buenas Prácticas:** La asignación de roles base (`ROLE_USER`) debe ser impuesta siempre del lado del servidor durante la lógica de registro; el modelo del cliente solo debe reflejar la capa visual.

### 2. Almacenamiento Inseguro de Tokens
* **Recomendación Técnica:**
  Migra de `Preferences DataStore` a **EncryptedSharedPreferences** de la biblioteca Jetpack Security, o cifra individualmente las preferencias en el DataStore (usando Tink).
  ```kotlin
  // Ejemplo usando EncryptedSharedPreferences
  val masterKey = MasterKey.Builder(context)
      .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
      .build()
  val sharedPreferences = EncryptedSharedPreferences.create(
      context, "secret_shared_prefs", masterKey,
      EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
      EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
  )
  ```
* **Buenas Prácticas:** Evita persistir JWTs críticos en Storage general sin cifrado. Todo token debe considerarse Información Confidencial.

### 3. Exposición de Información Sensible en Logs
* **Recomendación Técnica:**
  Limita explícitamente los logs a entornos de depuración y elimina la inyección de la cabecera `Authorization` en los registros.
  ```kotlin
  val loggingInterceptor = HttpLoggingInterceptor().apply {
      level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
  }
  ```
* **Buenas Prácticas:** Implementa soluciones estandarizadas como `Timber` y plántalas únicamente bajo `if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())` para garantizar cero emisiones en `release`.

### 4. Secretos Hardcodeados
* **Recomendación Técnica:**
  El `APP_KEY` nunca debe residir de forma cruda en el código. Extrae la variable a un archivo de configuración (`local.properties`) inyectado por el pipeline (BuildConfig field) o mediante NDK/Secrets Gradle Plugin.
* **Buenas Prácticas:** Aplicar restricciones robustas sobre las API keys (ej: Firebase, Maps) usando validaciones SHA-1 directo en consola de Google Cloud para que, aunque se extraigan, no se puedan emplear fuera del cliente legítimo.

### 5. Configuración de Red Insegura
* **Recomendación Técnica:**
  Remueve el `android:usesCleartextTraffic="true"` del Manifest. Para el entorno de emulador o de desarrollo (`10.0.2.2`), restringe la regla mediante un archivo de configuración de seguridad de red:
  ```xml
  <network-security-config>
      <domain-config cleartextTrafficPermitted="true">
          <domain includeSubdomains="true">10.0.2.2</domain>
      </domain-config>
  </network-security-config>
  ```
* **Buenas Prácticas:** Garantizar encriptación de túnel (TLS) por defecto siempre en APIs de producción.

### 6. Vulnerabilidad IDOR en Gestión de Archivos
* **Recomendación Técnica:**
  Retira permisos de eliminación directa de imágenes de Firebase Storage desde el cliente. Haz que el servidor propio de la app gestione los recursos o proteja el bucket validando el token de autenticación (Firebase Storage Security Rules dependientes de `auth.uid`).
* **Buenas Prácticas:** Obtener siempre metadatos concretos del archivo (mediante un `ContentResolver`) antes de almacenarlos. No concatenar strings en base a la entrada del usuario directamente al bucket sin su previa normalización.
