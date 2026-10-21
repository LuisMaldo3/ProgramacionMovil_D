# 🐾 HuellitasVet

**Laboratorio 08 — Programación en Móviles | TECSUP**  
**Tecnologías:** Kotlin · Jetpack Compose · Material 3 · Room 2.6.1 · DataStore · Navigation Compose  
**Arquitectura:** MVVM + Repository

## 1. Descripción

**HuellitasVet** es una aplicación móvil de práctica para gestionar la atención en una clínica veterinaria, con dos perfiles que comparten una base de datos local:

- **Dueño (`CLIENTE`):** inicia sesión, registra y edita sus mascotas, revisa el historial clínico, solicita citas en horarios disponibles, consulta sus reservas y puede cancelarlas conforme a las reglas de la clínica.
- **Veterinario (`ADMIN`):** consulta el panel con contadores e ingresos, revisa la agenda, confirma o cancela citas, registra diagnósticos y tratamientos, actualiza el peso de los pacientes y administra servicios y ajustes.

Los registros se almacenan en **Room** (`huellitasvet.db`) y las preferencias pequeñas —sesión, perfil, modo oscuro, notificaciones y aceptación de citas— se almacenan en **DataStore** (`ajustes`). Es un proyecto de práctica **local**: los datos de distintos dispositivos no se sincronizan entre sí.

> **Aviso:** Las cuentas y contraseñas incluidas son **ficticias, solo para el laboratorio**. No se deben utilizar para una aplicación real ni almacenar contraseñas de usuarios reales en texto plano.

## 2. Requisitos y ejecución

- Android Studio y Android SDK compatible con **API 35**.
- **JDK 17**, Gradle Wrapper **8.9** (incluido en el repositorio).
- Dispositivo o emulador Android **API 26 o superior**.
- Internet la primera vez para obtener dependencias de Gradle.

1. Clona el repositorio y abre la carpeta que contiene `settings.gradle.kts` en Android Studio.
2. Ejecuta **Sync Project with Gradle Files** y espera a que terminen las descargas.
3. Compila con `./gradlew assembleDebug` (macOS/Linux) o `./gradlew.bat assembleDebug` (Windows).
4. Inicia un emulador, ejecuta la app y elige **Dueño** o **Veterinario**.
5. Revisa los datos locales desde **App Inspection → Database Inspector**, en `huellitasvet.db`.

### Cuentas de prueba

| Perfil | Correo | Clave |
|---|---|---|
| Dueño (Ana) | `ana@correo.test` | `1234` |
| Dueño (Luis) | `luis@correo.test` | `1234` |
| Veterinario | `vet@huellitas.test` | `admin` |

Los datos iniciales se crean solo cuando la tabla de usuarios está vacía; los ejemplos incluyen 4 servicios, 4 mascotas y aproximadamente 10 citas con fechas relativas al día de la instalación. La disponibilidad de las citas cambia con el tiempo.

## 3. Funcionalidades y reglas de negocio

| Área | Funciones |
|---|---|
| Sesión | Selector de rol, errores de credenciales, persistencia del perfil activo |
| Mascotas | Registro, edición, validación por campo, eliminación solo sin citas |
| Reservas | Servicio activo, fechas de hoy a 7 días, horarios 09:00–17:30, no repetir horas ocupadas |
| Cancelación del dueño | Solo `PENDIENTE` o `CONFIRMADA` y **más de dos horas** antes |
| Atención clínica | Diagnóstico de al menos 5 caracteres y peso positivo; actualizar cita y peso juntos |
| Panel | Citas del día, pendientes, atendidas, ingresos mensuales y gráficos |
| Servicios | Crear, editar, desactivar; un servicio con citas no se elimina |
| Preferencias | Modo oscuro, notificaciones, aceptar citas, cambio de perfil y cierre de sesión |

## 4. Arquitectura MVVM + Repository

```text
        ┌───────────────────────────────────────┐
        │      View (pantallas Compose)         │
        │   UI, eventos y collectAsState()      │
        └──────────────────┬────────────────────┘
                           │ acciones / estado
                           ▼
        ┌───────────────────────────────────────┐
        │        ViewModel (reglas)             │
        │      StateFlow, validaciones          │
        └──────────────────┬────────────────────┘
                           │ interfaz
                           ▼
        ┌───────────────────────────────────────┐
        │      VetRepository (contrato)         │
        └──────────────────┬────────────────────┘
                           │ implementación
                           ▼
        ┌───────────────────────────────────────┐
        │ VetRepositoryRoom → DAO → Room/SQLite │
        │              huellitasvet.db          │
        └───────────────────────────────────────┘

        PreferenciasStore → DataStore («ajustes»)
                        ↕
               ViewModels de sesión/UI
```

**Separación de responsabilidades:** las pantallas solo se relacionan con ViewModels; los ViewModels usan la interfaz `VetRepository`; la implementación del repositorio coordina los DAOs; y Room gestiona el acceso a SQLite. Las consultas observables usan `Flow`, mientras que las escrituras usan funciones `suspend`.

## 5. Árbol de paquetes

```text
com.tecsup.huellitasvet/
├── MainActivity.kt
├── VetApp.kt
├── data/
│   ├── local/
│   │   ├── entity/
│   │   │   ├── Usuario.kt
│   │   │   ├── Mascota.kt
│   │   │   ├── Servicio.kt
│   │   │   └── Cita.kt
│   │   ├── dao/
│   │   │   ├── UsuarioDao.kt
│   │   │   ├── MascotaDao.kt
│   │   │   ├── ServicioDao.kt
│   │   │   └── CitaDao.kt
│   │   ├── relation/
│   │   │   ├── MascotaConDueno.kt
│   │   │   └── CitaConDetalle.kt
│   │   ├── VetDatabase.kt
│   │   └── DatosIniciales.kt
│   ├── repository/
│   │   ├── VetRepository.kt
│   │   └── VetRepositoryRoom.kt
│   └── prefs/
│       └── PreferenciasStore.kt
├── model/
│   ├── Rol.kt, EstadoCita.kt, Especie.kt, Sexo.kt
│   └── ConteoServicio.kt, Horarios.kt
├── viewmodel/
│   ├── VetViewModelFactory.kt
│   ├── SesionViewModel.kt
│   ├── InicioViewModel.kt
│   ├── MascotaViewModel.kt
│   ├── PedirCitaViewModel.kt
│   ├── MisCitasViewModel.kt
│   ├── PanelViewModel.kt
│   ├── AgendaViewModel.kt
│   ├── AtenderCitaViewModel.kt
│   ├── PacientesViewModel.kt
│   └── ServiciosViewModel.kt
└── ui/
    ├── theme/             (Color.kt, Type.kt, Theme.kt)
    ├── components/        (avatar, chips, tarjetas, campos, botones)
    │   └── graficos/     (GraficoDona.kt, GraficoBarras.kt)
    ├── navigation/        (Rutas.kt, NavVet.kt)
    ├── dueno/             (Splash, Login, Inicio, Mascotas,
    │                       Formulario, Ficha, PedirCita, MisCitas, Perfil)
    └── veterinario/      (Panel, Agenda, AtenderCita, Pacientes,
                            Servicios, FormularioServicio, Ajustes)
```

## 6. Capturas de pantalla (obligatorias: 6)

**Pendiente:** tomar capturas *reales del emulador* con la aplicación funcionando. Los mockups de la guía no deben presentarse como evidencia de ejecución.

| Perfil | Captura requerida | Archivo sugerido |
|---|---|---|
| Dueño | Inicio del dueño con mascotas y cita | `![img.png](img.png)` |
| Dueño | Registrar mascota | `![img_1.png](img_1.png)` |
| Dueño | Mis citas (próximas / historial) | `![img_2.png](img_2.png)` |
| Veterinario | Panel con estadísticas y gráficos | `![img_4.png](img_4.png)` |
| Veterinario | Agenda con citas | `![img_5.png](img_5.png)` |
| Veterinario | Atender cita (registro clínico) | `![img_6.png](img_6.png)` |


## 7. Pruebas de aceptación — Paso 14

**Estado inicial: pendientes de comprobar en el emulador.** Marca `[x]` únicamente después de verificar cada escenario; si no pasa, registra el error y corrígelo antes de entregar.

- [ ] **Login incorrecto:** muestra error y no permite entrar.
- [ ] **Mascota sin nombre:** muestra errores junto a los campos y no la guarda.
- [ ] **Eliminar mascota con citas:** muestra aviso y no permite eliminarla.
- [ ] **Pedir cita:** aparece en Mis citas como `PENDIENTE` y conserva el precio del servicio.
- [ ] **Horario ocupado:** se muestra gris y no se puede elegir.
- [ ] **Agenda del veterinario:** muestra la cita a la hora elegida; al confirmarla, el dueño la ve `CONFIRMADA`.
- [ ] **Atender cita:** pasa a `ATENDIDA`, aparece en el historial de la mascota y actualiza su peso.
- [ ] **Cancelar con menos de dos horas:** no permite la cancelación.
- [ ] **Ingresos del mes:** coinciden con la suma de los precios de citas `ATENDIDA` del mes.
- [ ] **Desactivar servicio:** ya no aparece entre los servicios disponibles para reservar.
- [ ] **Eliminar servicio con citas:** no se elimina; se informa al usuario.
- [ ] **Cerrar y abrir la app:** permanecen las citas, el perfil y la preferencia de modo oscuro.

**Registro de prueba:** fecha de ejecución: __________ · emulador/API: __________ · resultado global: __________

### Guía rápida para comprobar las dos pruebas más importantes

1. **Reserva y agenda:** entra como Ana, reserva una cita para una fecha futura; cambia al perfil veterinario y confirma el horario. Vuelve a Ana y verifica el nuevo estado.
2. **Atención e historial:** desde la agenda, atiende una cita válida, registra diagnóstico y peso; vuelve al dueño y comprueba la misma atención en la ficha e historial. El peso debe coincidir con el de la atención registrada.

## 8. «Para pensar»: reflexiones técnicas

La guía del laboratorio pide incluir respuestas de **«Para pensar»** en el README, pero en el PDF proporcionado **no están los enunciados específicos** de esa sección. Las siguientes son explicaciones de conceptos presentes en el laboratorio; si el docente entregó preguntas adicionales, sus respuestas exactas deben agregarse aquí.

**¿Por qué usar `Flow` para leer y `suspend` para escribir?**  
`Flow` informa automáticamente a la UI cuando cambia una tabla observada, por ejemplo al confirmar una cita. Las operaciones `suspend` realizan una escritura o consulta puntual sin bloquear el hilo principal.

**¿Por qué fecha y hora se guardan como texto?**  
Las fechas ISO (`aaaa-MM-dd`) y las horas (`HH:mm`) se ordenan y consultan fácilmente en SQLite. Para aplicar reglas de negocio se convierten a `LocalDate`, `LocalTime` y `LocalDateTime`.

**¿Por qué se copia el precio del servicio a la cita?**  
El importe reservado no debe cambiar si el veterinario modifica el precio del servicio en el futuro. Así se conservan los valores históricos y se calculan los ingresos correctamente.

**¿Por qué registrar una atención dentro de una transacción?**  
La cita debe quedar `ATENDIDA` y el peso de la mascota debe actualizarse como una sola operación. Si falla cualquiera de los cambios, Room revierte la transacción y evita datos inconsistentes.

**¿Qué información va en Room y cuál va en DataStore?**  
Room guarda datos estructurados y relacionados, como usuarios, mascotas, servicios y citas. DataStore guarda preferencias pequeñas, como modo oscuro, sesión, notificaciones y disponibilidad de reservas.

