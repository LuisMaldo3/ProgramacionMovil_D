# Clínica SaludPlus — App Paciente

Autor: Maldonado

App de agendamiento de citas médicas (Kotlin + Jetpack Compose, sin base de datos).

## Estructura
- `data/model`: Usuario, Especialidad, Medico, Cita
- `data/repository/Repositorio.kt`: colecciones en memoria + funciones con TODO
- `navigation`: Rutas y AppNavigation
- `ui/theme`, `ui/components`, `ui/screens` (15 pantallas)

Para ver lo pendiente: View > Tool Windows > TODO.

## Fase 2 — Mejora con IA (rama `mejora-ia`)
Mejora obligatoria: **calendario dinámico en la Pantalla 6 (Fecha y hora)** con `java.time.LocalDate`.

- Se muestran los próximos 5 días hábiles desde hoy (sin sábados, domingos ni días pasados).
- Las flechas `<` y `>` avanzan o retroceden una semana; no se puede ir antes de la semana actual.
- El mes y año ("Octubre 2026") cambian según la semana mostrada.
- Al cambiar de día, los horarios se recalculan solos y la hora elegida se reinicia.
- La Pantalla 7 muestra la fecha en texto en español ("Martes 16 de setiembre 2026").
- El bloqueo de horarios ya reservados sigue funcionando: la fecha se guarda como `yyyy-MM-dd`.
- Íconos con la librería Font Awesome (`compose-icons`); fotos de los médicos con Coil (retratos de ejemplo de randomuser.me, con un ícono de respaldo si no hay internet); `minSdk` 26 por `java.time`.

- Registro con casilla obligatoria de aceptación de Términos y Política de Privacidad (el botón "Aceptar y continuar" de Términos la marca sola).
- Usuario de prueba: `carlos@saludplus.com` / `123456`. La sesión se inicia desde Login o Registro.

Los prompts usados con el asistente de IA están documentados en [PROMPTS.md](PROMPTS.md).

