# MowiMarket - Aplicación Android

Aplicación móvil Android nativa para MowiMarket, conectada con el backend Django.

## 🚀 Características Implementadas

### ✅ **Autenticación Completa:**
- Inicio de sesión con email y contraseña
- Registro de nuevos usuarios
- Validación de formularios
- Manejo de errores y mensajes
- Almacenamiento seguro de tokens JWT
- Persistencia de sesión de usuario
- Detección automática de rol (Admin/Cliente)

### ✅ **Infraestructura Completa:**
- **Modelos de datos**: Producto, Categoría, Pedido, Carrito, Usuario
- **API Service**: Retrofit con todos los endpoints del backend
- **Repository Pattern**: MowiRepository centralizado
- **ViewModels**: MainViewModel, AuthViewModel, ProductosViewModel, CarritoViewModel
- **Manejo de estado**: StateFlow para reactive UI
- **Navegación**: Navigation Compose

### ✅ **Funcionalidades de Cliente:**
- Pantalla Home con categorías
- Catálogo de productos con filtros
- Carrito de compras
- Historial de pedidos
- Perfil de usuario

### ✅ **Funcionalidades de Administrador:**
- Dashboard con KPIs
- Gestión de productos
- Gestión de pedidos
- Gestión de usuarios
- Analytics y reportes

### ✅ **Integración con API:**
- Conexión con el backend Django
- Uso de Retrofit para llamadas HTTP
- Gestión de tokens de acceso y refresh
- Sincronización de cuentas entre web y móvil

## 📋 Requisitos Previos

1. **Android Studio** (versión recomendada: Hedgehog o superior)
2. **JDK 11** o superior
3. **Backend Django** ejecutándose en tu máquina local

## 🔧 Configuración

### 1. Configurar la URL del Backend

Abre el archivo `RetrofitClient.kt` ubicado en:
```
app/src/main/java/com/miempresa/mowimarket/data/api/RetrofitClient.kt
```

**Para Emulador Android:**
La configuración actual ya está lista:
```kotlin
private const val BASE_URL = "http://10.0.2.2:8000/"
```
> **Nota:** `10.0.2.2` es la IP especial que usa el emulador para conectarse a localhost de tu computadora.

**Para Dispositivo Físico:**
Si vas a probar en un teléfono real conectado a la misma red WiFi:
```kotlin
private const val BASE_URL = "http://TU_IP_LOCAL:8000/"
```
Reemplaza `TU_IP_LOCAL` con tu IP (ejemplo: `192.168.1.100`)

Para saber tu IP local:
- **Windows:** Ejecuta `ipconfig` en CMD
- **Mac/Linux:** Ejecuta `ifconfig` en terminal

### 2. Iniciar el Backend Django

Antes de probar la app, asegúrate de que el servidor Django esté corriendo:

```bash
cd server/django_api
python manage.py runserver 0.0.0.0:8000
```

> **Importante:** Usa `0.0.0.0:8000` en lugar de `localhost:8000` para que sea accesible desde el emulador/dispositivo.

### 3. Verificar que la base de datos esté corriendo

Asegúrate de que MySQL esté activo y la base de datos `mowi_store` exista.

## 🏃 Ejecutar la Aplicación

1. Abre el proyecto en Android Studio:
   ```
   File > Open > Selecciona la carpeta "android maui/MowiMarket"
   ```

2. Espera a que Gradle sincronice las dependencias (puede tardar algunos minutos la primera vez)

3. Conecta un dispositivo Android o inicia un emulador

4. Haz clic en el botón **Run** (▶️) o presiona `Shift + F10`

## 📱 Uso de la Aplicación

### Registro de Nuevo Usuario

1. Abre la aplicación
2. Navega a la pantalla de autenticación (botón de login)
3. Selecciona la pestaña **"Crear Cuenta"**
4. Completa el formulario:
   - Nombre completo (mínimo 2 caracteres)
   - Email válido
   - Contraseña (mínimo 6 caracteres)
   - Confirmar contraseña
5. Presiona **"Crear Cuenta Nueva"**
6. Si el registro es exitoso, verás un mensaje de confirmación
7. Cambia a la pestaña **"Iniciar Sesión"** para entrar

### Iniciar Sesión

1. Selecciona la pestaña **"Iniciar Sesión"**
2. Ingresa tu email y contraseña
3. Presiona **"Iniciar Sesión"**
4. Si las credenciales son correctas, accederás a la aplicación

### Sincronización Web-Móvil

✅ Las cuentas son **completamente intercambiables**:
- Una cuenta creada en la **app web** funciona en **Android**
- Una cuenta creada en **Android** funciona en la **app web**
- Los tokens y datos se almacenan localmente en cada dispositivo

## 🛠️ Estructura del Proyecto

```
app/src/main/java/com/miempresa/mowimarket/
├── data/
│   ├── api/
│   │   ├── AuthApiService.kt       # Interface Retrofit con endpoints
│   │   └── RetrofitClient.kt        # Configuración de Retrofit
│   ├── models/
│   │   ├── User.kt                  # Modelo de usuario
│   │   ├── LoginRequest.kt          # Request de login
│   │   ├── RegisterRequest.kt       # Request de registro
│   │   ├── AuthResponse.kt          # Response de autenticación
│   │   └── ErrorResponse.kt         # Manejo de errores
│   ├── preferences/
│   │   └── TokenManager.kt          # Gestión de tokens y sesión
│   └── repository/
│       └── AuthRepository.kt        # Lógica de negocio de autenticación
├── ui/
│   ├── viewmodels/
│   │   └── AuthViewModel.kt         # ViewModel de autenticación
│   ├── screens/
│   │   └── auth/
│   │       └── AuthScreen.kt        # Pantalla de login/registro
│   └── components/
│       ├── MowiButton.kt            # Botón personalizado
│       └── MowiTextField.kt         # Campo de texto personalizado
└── MainActivity.kt                  # Actividad principal
```

## 🔐 Seguridad

- ✅ Las contraseñas se envían de forma segura al backend
- ✅ Los tokens JWT se almacenan localmente usando SharedPreferences
- ✅ Las validaciones se realizan tanto en cliente como en servidor
- ⚠️ **Nota:** En producción, se recomienda usar HTTPS en lugar de HTTP

## 🐛 Solución de Problemas

### Error: "Error de conexión"

**Causa:** El backend no está corriendo o la URL está mal configurada.

**Solución:**
1. Verifica que Django esté corriendo: `http://localhost:8000/api/`
2. Si usas emulador, asegúrate de usar `10.0.2.2:8000`
3. Si usas dispositivo físico, verifica que estén en la misma red WiFi

### Error: "Email o contraseña incorrectos"

**Causa:** Las credenciales no existen en la base de datos.

**Solución:**
1. Verifica que el usuario esté registrado en el backend
2. Prueba crear una nueva cuenta desde la app
3. Verifica que la base de datos MySQL esté corriendo

### La app se cierra al iniciar

**Causa:** Dependencias no sincronizadas correctamente.

**Solución:**
1. En Android Studio: `File > Invalidate Caches and Restart`
2. Limpia el proyecto: `Build > Clean Project`
3. Reconstruye: `Build > Rebuild Project`

### No se conecta desde dispositivo físico

**Causa:** Firewall bloqueando la conexión o IP incorrecta.

**Solución:**
1. Verifica tu IP local: `ipconfig` (Windows) o `ifconfig` (Mac/Linux)
2. Actualiza `BASE_URL` en `RetrofitClient.kt` con tu IP
3. Asegúrate de que el firewall permita conexiones al puerto 8000
4. Ambos dispositivos deben estar en la misma red WiFi

## 📦 Dependencias Principales

- **Jetpack Compose** - UI moderna y declarativa
- **Retrofit 2.9.0** - Cliente HTTP para llamadas a la API
- **Gson** - Serialización/deserialización JSON
- **OkHttp** - Cliente HTTP con logging
- **Navigation Compose** - Navegación entre pantallas
- **Coroutines** - Programación asíncrona
- **ViewModel & LiveData** - Arquitectura MVVM

## 🎯 Próximos Pasos

- [ ] Implementar refresh de tokens automático
- [ ] Agregar opción "Recordarme" en el login
- [ ] Implementar recuperación de contraseña
- [ ] Agregar biometría (huella/Face ID)
- [ ] Implementar cierre de sesión
- [ ] Agregar perfil de usuario

## 📞 Soporte

Si encuentras algún problema o tienes preguntas:
1. Revisa la sección de "Solución de Problemas"
2. Verifica que todas las configuraciones estén correctas
3. Asegúrate de que el backend esté funcionando correctamente

---

**Versión:** 1.0
**Última actualización:** 2025-11-30
