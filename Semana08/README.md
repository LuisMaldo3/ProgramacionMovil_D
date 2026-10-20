# HuellitasVet — estructura con solamente archivos [E]

Proyecto base **alternativo** reconstruido del PDF TECSUP *Laboratorio 08 · MVVM + Room*.
**No es** el ZIP original del docente.

## Contenido
- Se incluyen únicamente los **28 archivos Kotlin marcados [E] (ENTREGADO)** en las páginas 6 y 7 del PDF.
- Se incluyen las carpetas de `data/local/entity`, `data/local/dao`, `data/local/relation`, `data/repository`, `data/prefs`, `model`, `viewmodel`, `ui`, `ui/dueno`, `ui/veterinario`, `ui/navigation`, `ui/components/graficos`, `ui/theme`, etc.
- Las carpetas pendientes para archivos **[A]** y **[G]** quedan creadas y vacías (sin .kt, TODO, ni archivos de relleno).
- Los archivos de infraestructura (`build.gradle.kts`, `settings.gradle.kts`, wrapper, `AndroidManifest.xml`, etc.) están incluidos para reconocer el proyecto en Android Studio.

## Importante — todavía no compila
Esta edición cumple estrictamente el pedido de incluir **solo los archivos [E]**. Es normal que por ahora Android Studio muestre **referencias sin resolver** y el APK NO compile, por ejemplo:
- `MainActivity.kt` usa `VetApp` y `NavVet` ([G]), todavía ausentes.
- `VetRepository.kt` ([E]) declara métodos con `Usuario`, `Mascota`, `CitaConDetalle`, etc. ([A]), aún sin definir.
- `VetViewModelFactory.kt` y `ServiciosScreen.kt` ([E]) se refieren a `ServiciosViewModel` ([G]), todavía ausente.

Esto **no es un proyecto ejecutable** aún. No se han añadido clases artificiales para simular funcionalidades ni resolverlas anticipadamente. A medida que crees las clases [A] y completes las [G], podrás conectar las piezas y compilar.

## Abrir en Android Studio
1. Descomprime `HuellitasVet-Solo-E.zip`.
2. Android Studio → Open → elige la carpeta `HuellitasVet-Solo-E` que contiene `settings.gradle.kts`.
3. Se puede inspeccionar la estructura y editar archivos, pero el proyecto no ejecutará hasta desarrollar las piezas pendientes.

**Reglas del PDF:** La Fase 1 exige no usar IA para implementar los ejercicios y pide un commit por cada paso; conserva esos criterios si vas a entregar el laboratorio.
