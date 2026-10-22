# HuellitasVet

Aplicación móvil Android para la gestión de mascotas y una clínica veterinaria. Desarrollada con Kotlin, Jetpack Compose, Room y Navigation Compose.

## Rediseño

- Inicio del dueño con portada fotográfica, degradado de alto contraste y botón de reserva.
- Tarjetas de mascotas con fondos por especie y servicios con acentos de color propios.
- Próxima cita y recordatorios con jerarquía visual y colores cálidos.
- Panel veterinario con métricas animadas, tarjetas de color, ingresos destacados y gráficos animados.
- Transiciones entre pantallas y navegación inferior con selección animada.
- Temas diferenciados para los perfiles y nuevo icono adaptativo de la app.
- Reserva de citas con fotografía clínica, pasos guiados, selección animada de mascota/servicio/horario y resumen fijo de la reserva.
- Confirmación visual con el estado pendiente; listado de citas con fotografías, contadores y agenda clínica con métricas por estado.
- Se conservaron los flujos de mascotas, citas, atención clínica, pacientes, servicios y perfil.

## Abrir y generar la app

1. Extrae el ZIP y abre la carpeta `HuellitasVet` con Android Studio.
2. Sincroniza Gradle con un SDK de Android instalado.
3. En Windows, ejecuta `gradlew.bat assembleDebug` desde la carpeta del proyecto para generar un APK.
4. El archivo se crea en `app/build/outputs/apk/debug/app-debug.apk`.

Configuración: `compileSdk 35`, `minSdk 26` y Java 17 para Gradle.

## Perfiles de demostración

- Dueño: `ana@correo.test` / `1234`
- Veterinario: `vet@huellitas.test` / `admin`

El APK adjunto es una compilación de depuración.
