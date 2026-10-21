# HuellitasVet Global Professional Redesign (Commit 1)

Comprehensive commercial-grade redesign of **HuellitasVet** for both Owner (Dueño) and Veterinarian (Veterinario) profiles. Implements custom Material 3 palettes, adaptive layouts, WebP image integrations, human & pet avatars, polished cards, dark mode, keyboard handling (IME padding), and complete UI screen overhauls while preserving Room, DataStore, MVVM architecture, and business logic.

## User Review Required

> [!IMPORTANT]
> **Updated Palette**: Petroleum Green `#087F6D`, Turquoise `#10B8A6`, Soft Green `#DDF7EF`, Warm White `#FAFBF9`, Professional Blue `#2458DE`, Light Blue `#EDF3FF`, Dark Text `#172B3A`, Secondary Text `#64748B`, Coral `#EF6461`, Amber `#F4A340`.

> [!NOTE]
> **Preserved Logic & Database**: All Room DAOs, DataStore preferences, ViewModels, and Navigation routes are strictly maintained without schema or logic breaks.

## Proposed Changes

### Theme & Shared Components
#### [MODIFY] [Color.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/theme/Color.kt)
- Update color tokens for Dueño, Veterinario, status badges, and backgrounds.

#### [MODIFY] [Theme.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/theme/Theme.kt)
- Update `VetTheme` color schemes and system bar colors for light/dark modes and roles.

#### [MODIFY] [MascotaAvatar.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/components/MascotaAvatar.kt)
- Enhance pet avatars using local species WebP images (`mascota_perro`, `mascota_gato`, `mascota_conejo`, `mascota_ave`).

#### [MODIFY] [CitaCard.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/components/CitaCard.kt)
- Redesign appointment cards with rounded corners (`16.dp`), service icons/images, and status chips.

#### [MODIFY] [AppTopBar.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/components/AppTopBar.kt)
- Redesign top bar with title, back navigation, actions, and role-based coloring.

#### [MODIFY] [BarraNavegacion.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/veterinario/BarraNavegacionVeterinario.kt)
- Modernize bottom navigation bars for Dueño and Veterinario with proper active indicator pills and vector icons.

### Dueño Screens
#### [MODIFY] [SplashScreen.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/dueno/SplashScreen.kt)
- Hero layout with `splash_fondo.webp`, logo, slogan, smooth entrance animation, "Comenzar" & "Iniciar sesión" buttons.

#### [MODIFY] [LoginScreen.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/dueno/LoginScreen.kt)
- Clean layout with Dueño/Veterinario tab switch, email & password fields with toggle, error feedback, and IME padding.

#### [MODIFY] [InicioScreen.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/dueno/InicioScreen.kt)
- Premium dashboard with personalized greeting, hero image, upcoming appointment card, pets carousel, popular services, reminders, and quick actions.

#### [MODIFY] [MascotaScreen.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/dueno/MascotaScreen.kt)
- Redesigned pet cards with species WebP images, breed, age, weight, and empty state.

#### [MODIFY] [FormularioMascotaScreen.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/dueno/FormularioMascotaScreen.kt)
- Clean pet form with avatar preview, species and sex selectors, date picker, weight input, and IME padding.

#### [MODIFY] [FichaMascotaScreen.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/dueno/FichaMascotaScreen.kt)
- Hero pet photo, general info, Datos/Historial tabs, timeline of clinical attention with Spanish dates, diagnosis & treatment cards.

#### [MODIFY] [PedirCitaScreen.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/dueno/PedirCitaScreen.kt)
- Pet selector with photos, service cards with images & prices, horizontal calendar, time slots grid, booking summary, and confirmation.

#### [MODIFY] [MisCitasScreen.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/dueno/MisCitasScreen.kt)
- Próximas and Historial tabs, modern appointment cards, cancellation dialog, and rule notices.

#### [MODIFY] [PerfilScreen.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/dueno/PerfilScreen.kt)
- Human person avatar, user info, 3 statistics cards, Dark Mode switch, role switch, and logout.

### Veterinario Screens
#### [MODIFY] [PanelScreen.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/veterinario/PanelScreen.kt)
- Clinic dashboard with `veterinario_banner.webp`, stats cards, Donut chart, Bar chart, pending alerts, and quick links.

#### [MODIFY] [AgendaScreen.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/veterinario/AgendaScreen.kt)
- Horizontal date selector, timeline view, appointment cards with pet pictures, status badges, Confirm & Atender buttons.

#### [MODIFY] [AtenderCitaScreen.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/veterinario/AtenderCitaScreen.kt)
- Clinical appointment execution form with pet summary, previous vs new weight input, diagnosis, treatment, and register button.

#### [MODIFY] [PacientesScreen.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/veterinario/PacientesScreen.kt)
- Search bar, species filters, patient cards with last care info, and navigation to patient details.

#### [MODIFY] [FichaPacienteScreen.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/veterinario/FichaPacienteScreen.kt)
- Detailed patient record with pet photo, owner details, clinical history timeline, past weights, and past treatments.

#### [MODIFY] [ServiciosScreen.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/veterinario/ServiciosScreen.kt)
- Catalog with service WebP images, name, description, duration, price, active switch, edit & add service actions.

#### [MODIFY] [FormularioServicioScreen.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/veterinario/FormularioServicioScreen.kt)
- Form for service details (name, description, price, duration, active switch) with IME padding and validation.

#### [MODIFY] [AjustesScreen.kt](file:///C:/Users/ASUS/OneDrive/Desktop/ProgrmacionMovil/Semana08/app/src/main/java/com/tecsup/huellitasvet/ui/veterinario/AjustesScreen.kt)
- Human person avatar, vet account info, Dark Mode, notifications, accept appointments toggle, role switch, and logout.

## Verification Plan

### Automated Tests
- Build verification via `gradle_build` (`app:assembleDebug`).

### Manual Verification
- Screen visual layout check and dark mode verification across all screens.
