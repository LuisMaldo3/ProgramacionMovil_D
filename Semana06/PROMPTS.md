# PROMPTS.md — Fase 2 (rama mejora-ia)

**Asistente de IA usado:** Claude (Anthropic), mediante chat.
**Mejora obligatoria:** calendario dinámico en la Pantalla 6 (Fecha y hora) con `java.time.LocalDate`.

El trabajo se hizo en 3 partes, y cada parte corresponde a un commit de la rama. Para cada una se deja:
1. el **prompt completo**, escrito con todo el detalle necesario (contexto, reglas, medidas, colores, datos y comportamiento) para poder pegarlo en una IA y obtener el mismo resultado; son la versión ordenada de lo que se le pidió a la IA durante el desarrollo;
2. una **respuesta resumida** de lo que la IA hizo, y
3. **qué tuve que corregir** del código que generó.

Orden para reproducirlo: pegar el prompt 1 en la IA, probar la app, luego el 2 y luego el 3, con el proyecto abierto en Android Studio.

---

## Prompt 1 — Base, datos, Splash, Registro y Login
**Commit:** `Parte 1: Splash, Registro y Login según el diseño de referencia`

**Prompt completo (para pegar en una IA):**

```text
ROL
Eres un desarrollador Android senior. Trabaja directamente sobre los archivos del proyecto abierto: IMPLEMENTA los cambios en los archivos, no entregues solo un plan ni ejemplos sueltos. Antes de tocar algo, lee los archivos que vas a modificar, navigation/Rutas.kt, navigation/AppNavigation.kt, data/model/*, data/repository/Repositorio.kt y ui/components/*.

PROYECTO
App Android "Clínica SaludPlus" (solo la App Paciente) hecha en Kotlin con Jetpack Compose y Material 3, paquete com.saludplus.citas, carpeta Semana06/ClinicaSaludPLus. NO hay base de datos ni servidor: los usuarios, especialidades, médicos y citas viven en memoria dentro del object data/repository/Repositorio.kt y se pierden al cerrar la app. Modelos: Usuario(id, nombre, correo, contrasena, telefono: String? = ""), Especialidad(id, nombre, descripcion), Medico(id, nombre, especialidadId, calificacion, resenas = 0, cmp = ""), Cita(id, usuarioId, medicoId, especialidadId, fecha, hora). Rutas (Rutas.kt): splash, registro, login, terminos, home, especialidades, medicos/{especialidadId}, fechahora/{medicoId}, confirmar/{medicoId}/{fecha}/{hora}, citaexitosa, miscitas, detallecita/{citaId}, perfil, resultados, notificaciones. Las fechas se guardan y viajan por las rutas como texto "yyyy-MM-dd".

REGLAS (no negociables)
1. No cambies el nombre, los parámetros ni el tipo de retorno de las funciones públicas del Repositorio (registrarUsuario, iniciarSesion, cerrarSesion, buscarEspecialidades, especialidadesDestacadas, obtenerEspecialidad, medicosPorEspecialidad, buscarMedicos, obtenerMedico, horariosDisponibles, agendarCita, citasDelUsuario, obtenerCita, cancelarCita, siguienteIdUsuario). Sí puedes cambiar los DATOS iniciales y agregar campos NUEVOS con valor por defecto a los modelos.
2. No cambies Rutas.kt ni los argumentos de navegación (Rutas.medicos(id), Rutas.fechaHora(medicoId), Rutas.confirmar(medicoId, fecha, hora), Rutas.detalleCita(id)).
3. Dependencias permitidas, y solo estas: material-icons-extended (ya está), la librería de íconos Font Awesome "br.com.devsrsouza.compose.icons:font-awesome:1.1.1" y la librería de imágenes Coil "io.coil-kt:coil-compose:2.7.0" (con el permiso INTERNET en el AndroidManifest). Sin base de datos. Sin archivos .java: todo en Kotlin. No dejes archivos sobrantes (por ejemplo un SplashScreen.java).
4. Ninguna pantalla terminada puede quedar con PantallaEnConstruccion.
5. Compila el proyecto (:app:assembleDebug) y corrige TODOS los errores antes de terminar.
6. GIT: no ejecutes ningún comando Git (ni status, log o diff), no leas .git, no cambies ni crees ramas. Los commits los hago yo. Al terminar, dime qué archivos creaste o modificaste.

DISEÑO DE REFERENCIA
Hay una imagen adjunta ("APP PACIENTE – Agendar citas médicas") con 7 pantallas: 1 Splash, 2 Crear cuenta, 3 Inicio, 4 Especialidades, 5 Médicos de Ginecología, 6 Seleccionar fecha y hora, 7 Confirmar cita. Las pantallas deben verse como ese diseño: mismos textos, mismos colores, mismas proporciones y cada elemento en su sitio. Los textos de ejemplo de la imagen (Juan Pérez, Setiembre 2023, etc.) son datos de muestra: la app muestra los datos reales.

ESCALA (para que quepa y se vea igual en cualquier teléfono)
Las medidas de este prompt están en dp (y sp para el texto) para una pantalla de referencia de 360 x 780 dp. Crea ui/theme/Escala.kt con una clase Escala(k, fuente) con las funciones d(valor): Dp = (valor * k).dp y s(valor): TextUnit = (valor * k / fuente).sp, y un @Composable rememberEscala() donde k = minOf(screenWidthDp / 360f, screenHeightDp / 780f, 1.15f) * FACTOR_DISENO, con FACTOR_DISENO = 0.85f (constante privada y comentada: 1.0 son las proporciones exactas de la imagen; bajarla hace todo más compacto) y fuente = fontScale del sistema pero nunca menor que 1 (así el texto NO crece cuando el usuario aumenta el tamaño de fuente del teléfono). Usa rememberEscala() en todas las pantallas: tamaño real = e.d(valor) y e.s(valor).

ZONAS SEGURAS (muy importante)
La app se dibuja de borde a borde (enableEdgeToEdge en MainActivity). Todo botón, enlace o pie que esté abajo debe quedar ENCIMA de la barra de navegación del teléfono (navigationBarsPadding) y todo contenido de arriba debajo de la barra de estado (statusBarsPadding). Nada importante puede quedar tapado. En pantallas largas usa un contenido con scroll y un pie fijo, no posiciones fijas desde el borde de abajo.

COLORES
Azul primario (botones, enlaces, día y hora seleccionados) #0468FD; azul deshabilitado #B7C9E8; texto principal #050A3A; azul marino de marca #0A1A6B; texto secundario #52607C; etiqueta de campo #4A5A7A; gris claro (placeholders, inactivos) #8D96AB; gris de íconos #9AA3B5. Fondo del Splash: degradado de #F3FAFE (arriba) a #E7F5FE. Bordes: campo #DDE3EA, tarjeta de ícono #E3E8F0, tarjeta #E8EDF2, hora #E8EDF3, caja de motivo #DFE2E5; divisor #EEF1F6. Rellenos: tarjeta de especialidad #F9FBFC, buscador #F1F4F7, tarjeta del médico #F3F7FD, hora sin seleccionar #F1F4F8, ícono de Confirmar #E8F0FE. Chip "Disponible": fondo #D9F8E8 y texto #118A3C; chip "Sin horarios": fondo #F3F4F6 y texto #6B7280. Estrella #F5A623; error #D93025. Tarjetas de Inicio: Agendar cita fondo #E5F1FE y texto #0468FD; Mis citas fondo #D4FDE9 y texto #07AF4E; Mis datos fondo #F2E9FD y texto #8736FD; Resultados fondo #FDF2E3 y texto #FD8407. Pon todos estos colores como constantes en ui/theme/Color.kt y úsalos desde ahí.

ÍCONOS E IMÁGENES
No uses imágenes para los íconos: usa los vectores de la librería Font Awesome (import compose.icons.FontAwesomeIcons, compose.icons.fontawesomeicons.Solid y compose.icons.fontawesomeicons.solid.NombreDelIcono, por ejemplo FontAwesomeIcons.Solid.User) con Icon(imageVector = ..., tint = ...). Para flechas, lupa, campana, chevrones, estrella, check y la barra inferior usa Material Icons. La única imagen del proyecto es la ilustración del Splash, que ya existe: R.drawable.ilustracion_bienvenida_saludplus (1122 x 1402 px, doctor con plantas y fondo celeste #E7F5FE).

PARTE 1 DE 3 — BASE, DATOS, SPLASH, REGISTRO Y LOGIN
Alcance: build.gradle y manifest, tema y colores, Escala.kt, MainActivity, modelos y datos del Repositorio, componentes de formulario, barra superior, logo e íconos de especialidades, y las pantallas Splash, Crear cuenta, Iniciar sesión y Términos. No toques las demás pantallas.

1) CONFIGURACIÓN
- app/build.gradle.kts: agrega Font Awesome (br.com.devsrsouza.compose.icons:font-awesome:1.1.1) y Coil (io.coil-kt:coil-compose:2.7.0). En esta parte minSdk sigue en 24.
- AndroidManifest.xml: agrega <uses-permission android:name="android.permission.INTERNET" /> (las fotos de los médicos se descargan en la Parte 2).
- MainActivity: llama a enableEdgeToEdge() antes de setContent.
- ui/theme/Color.kt (constantes de la tabla de colores), ui/theme/Theme.kt (colorScheme claro: primary #0468FD, fondo y superficie blancos, texto #050A3A, error #D93025) y ui/theme/Escala.kt como se describió.
- Elimina cualquier archivo sobrante (SplashScreen.java, XML de drawable sin uso o dañados).

2) MODELOS Y DATOS (solo datos; mismas funciones)
- Usuario: agrega telefono: String? = "". Medico: agrega resenas: Int = 0 y cmp: String = "".
- Especialidades (id, nombre, descripción): 1 Medicina General – Atención integral; 2 Pediatría – Infancia y adolescencia; 3 Ginecología – Salud de la mujer; 4 Cardiología – Corazón y sistema sanguíneo; 5 Dermatología – Piel, cabello y uñas; 6 Traumatología – Huesos y sistema muscular; 7 Oftalmología – Salud visual.
- Médicos (id, nombre, especialidadId, calificación, reseñas, cmp): 1 Dr. Luis Paredes 1 4.5 64 20481; 2 Dra. Rosa Quispe 1 4.8 91 20517; 3 Dra. Carla Rojas 2 4.9 110 21034; 4 Dr. Hugo Medina 2 4.4 58 21190; 5 Dra. Ana Torres 3 4.9 120 12345; 6 Dra. Claudia Rojas 3 4.8 98 12871; 7 Dr. Luis Ramírez 3 4.7 85 13306; 8 Dra. Mariana Soto 3 4.6 72 13752; 9 Dr. Marco Salas 4 4.7 77 22418; 10 Dra. Lucía Vega 4 4.6 69 22563; 11 Dra. Elena Campos 5 4.8 88 23077; 12 Dr. Pablo Ríos 5 4.5 52 23245; 13 Dr. Diego Fuentes 6 4.7 73 24102; 14 Dr. Andrés Lara 6 4.5 49 24388; 15 Dra. Sofía Mendoza 7 4.8 83 25016; 16 Dr. Jorge Navarro 7 4.4 45 25274.
- Horarios base (los del diseño, cada 30 minutos): 08:00, 08:30, 09:00, 09:30, 10:00, 10:30, 11:00, 11:30, 12:00.
- Usuario demo: Usuario(1, "Carlos Mendoza", "carlos@saludplus.com", "123456", "900111222"), que arranca como usuarioActual.
- registrarUsuario devuelve false si ya existe otro usuario con el mismo correo (solo si el correo no está vacío, sin distinguir mayúsculas) o con el mismo teléfono. iniciarSesion(identificador, contrasena) acepta el correo O el teléfono más la contraseña, deja el usuario en usuarioActual y devuelve true o false. cerrarSesion pone usuarioActual en null. Las listas se manejan con operaciones de colecciones (filter, find, any, sortedByDescending, take, removeIf).

3) COMPONENTES (ui/components)
- BotonPrincipal(texto, onClick, modifier, habilitado): ancho completo, 62 de alto, esquinas 14, fondo #0468FD, texto blanco 20 sp SemiBold; deshabilitado #B7C9E8 con texto blanco.
- CampoDiseno(etiqueta, valor, onCambio, ejemplo, icono: ImageVector, error, esPassword, teclado): una fila con DOS partes. A la izquierda una tarjeta de ícono de 65 x 61, esquinas 14, fondo blanco, borde 1 dp #E3E8F0 y sombra de 2 dp, con el ícono de Font Awesome en #0468FD de 28 dp. A la derecha (3 dp de separación) una columna desplazada 4 dp hacia arriba con la etiqueta (17 sp, #4A5A7A, sangría de 7) y debajo, a 3 dp, la caja de entrada: 46 de alto, esquinas 10, borde 1 dp #DDE3EA, fondo blanco, padding horizontal 14, hecha con BasicTextField de una línea (texto 23 sp negro, cursor azul, placeholder de ejemplo en #8D96AB, PasswordVisualTransformation si esPassword, teclado según el tipo). Si hay error, muestra debajo un texto rojo de 13 sp con 88 de sangría.
- CampoBusqueda(valor, onCambio, placeholder): 48 de alto, esquinas 12, fondo #F1F4F7 sin borde, padding horizontal 14, lupa de Material gris de 24 y un BasicTextField con placeholder de 17 sp en #52607C.
- BarraSuperior(titulo, onAtras, accion): blanca, mide 96 de alto desde el borde de arriba de la pantalla; en su parte de abajo hay una fila de 48 con la flecha atrás de Material (30, a 16 del borde, solo si hay onAtras), el título centrado en 21 sp SemiBold #050A3A y, opcional, una acción a la derecha con 16 de margen.
- ImagenesDiseno.kt: LogoSaludPlus(): logo de 122 x 109 (escalado) dibujado con Canvas: un brazo vertical azul #0468FD (x 0.335 del ancho, ancho 0.37, alto completo), un brazo horizontal azul (x 0.02, y 0.33 del alto, ancho 0.66, alto 0.38), ambos con esquinas de 0.11 del ancho; un lóbulo turquesa #1EA7D8 (x 0.56, y 0.27, ancho 0.44, alto 0.56, esquinas 0.20 del ancho) y encima un corazón blanco (FontAwesomeIcons.Solid.Heart de 41, desplazado 0.40 del ancho y 0.47 del alto). IconoEspecialidad(especialidad, tamano): ícono tintado dentro de un círculo pastel (el glifo mide la mitad del círculo): Medicina General = Solid.User #1E9BF0 sobre #E0F3FE; Pediatría = Solid.Baby #F28C28 sobre #FFE9D2; Ginecología = Solid.Venus #EC4899 sobre #FDE7F1; Cardiología = Solid.Heartbeat #E53935 sobre #FDE5E5; Dermatología = Solid.Allergies #F28C28 sobre #FFE9D2; Traumatología = Solid.Bone #1E6FE0 sobre #E3EEFE; Oftalmología = Solid.Eye #1E6FE0 sobre #E3EEFE. cargoMedico(medico, especialidad): texto bajo el nombre; femenino si el nombre empieza con "Dra.": Medicina General "Médica general"/"Médico general", Pediatría "Pediatra", Ginecología "Ginecóloga"/"Ginecólogo", Cardiología "Cardióloga"/"Cardiólogo", Dermatología "Dermatóloga"/"Dermatólogo", Traumatología "Traumatóloga"/"Traumatólogo", Oftalmología "Oftalmóloga"/"Oftalmólogo".

4) PANTALLA 1 — SplashScreen (tres bloques que siempre caben)
Columna a pantalla completa con el degradado de fondo (#F3FAFE arriba, #E7F5FE desde el 45 % hasta abajo) y estos tres bloques:
a) Arriba, debajo de la barra de estado (statusBarsPadding) y con 20 de margen: LogoSaludPlus centrado, 8 de espacio, "Clínica" (36 sp, Bold, #0A1A6B), "SaludPlus" (45 sp, Bold, #0A1A6B), 6 de espacio y "Tu salud, nuestra prioridad" (22 sp, #52607C), todo centrado.
b) En el medio, con weight(1f) y todo el ancho: la ilustración R.drawable.ilustracion_bienvenida_saludplus con fillMaxSize, contentScale = Crop y alignment = BiasAlignment(0f, -0.3f), de modo que ocupa TODO el espacio que sobra. Encima, un degradado de 30 de alto de #E7F5FE a transparente en el borde de arriba y otro de 40 de alto de transparente a #E7F5FE en el borde de abajo, para que la imagen se funda con el fondo.
c) Abajo, con fondo #E7F5FE y navigationBarsPadding, padding 22 a los lados, 10 arriba y 14 abajo: el botón "Comenzar" (ancho completo, 55 de alto, esquinas 12, azul, texto blanco 20 sp SemiBold) y debajo el enlace "Ya tengo una cuenta" (18 sp, Bold, azul, con 12 de padding vertical para facilitar el toque).
"Comenzar" navega a Rutas.REGISTRO y el enlace a Rutas.LOGIN. Nada puede quedar fuera de la pantalla ni tapado por la barra de navegación.

5) PANTALLA 2 — RegistroScreen (Crear cuenta)
Estructura: Column a pantalla completa, fondo blanco, con statusBarsPadding, navigationBarsPadding e imePadding, y DOS partes: (1) un contenido con weight(1f) y scroll vertical, y (2) un pie fijo.
Contenido, de arriba hacia abajo: 20 de espacio; "Crear cuenta" centrado (28 sp, Bold, #050A3A, altura de línea 34); 6; "Regístrate para agendar tus citas" centrado (18 sp, #52607C); 30; cuatro CampoDiseno con 17 de margen horizontal y 20 entre ellos: "Nombre" (ejemplo "Juan Pérez", ícono Solid.User), "Teléfono" (ejemplo "987 654 321", ícono Solid.PhoneVolume, teclado numérico), "Correo (opcional)" (ejemplo "juan@correo.com", ícono Solid.Envelope, teclado de correo) y "Contraseña" (puntos "••••••••", ícono Solid.Lock, modo contraseña); 26; botón "Registrarme"; si hay error general, texto rojo de 14 sp centrado; 16; "Al registrarte aceptas nuestros" centrado (18 sp, #52607C) y debajo "Términos y Condiciones" (18 sp, SemiBold, azul, abre Rutas.TERMINOS) seguido de un punto gris; 12 de espacio final.
Pie fijo (siempre visible, encima de la barra de navegación), con 8 arriba y 14 abajo: "¿Ya tienes cuenta? " (18 sp, Bold, #050A3A) y "Iniciar sesión" (18 sp, Bold, azul, va a Rutas.LOGIN), centrados.
Comportamiento y validaciones: el teléfono solo acepta dígitos y espacios; nombre no vacío ("Ingresa tu nombre"); teléfono con exactamente 9 dígitos ("Ingresa un teléfono de 9 dígitos"); correo opcional: si se escribe debe ser válido ("Correo no válido"); contraseña de al menos 6 caracteres ("Mínimo 6 caracteres"). Los errores salen debajo de cada caja. Si registrarUsuario devuelve false, muestra "Ya existe una cuenta con ese correo o teléfono". Si se registra bien, inicia sesión con el correo (o con el teléfono si el correo quedó vacío) y navega a Rutas.HOME con popUpTo(Rutas.SPLASH) { inclusive = true }. No hay casilla de términos ni campo de confirmar contraseña.

6) PANTALLA LOGIN — LoginScreen (mismo estilo, no está en la imagen)
Misma estructura que el Registro (contenido con scroll y pie fijo): "Iniciar sesión", "Ingresa para gestionar tus citas", dos CampoDiseno ("Correo o teléfono" con Solid.Envelope y "Contraseña" con Solid.Lock), botón "Iniciar sesión" y pie "¿No tienes cuenta? Regístrate" (va a Rutas.REGISTRO). Validación: el primer campo debe ser un correo válido o un teléfono de 9 dígitos y la contraseña no puede estar vacía. Si Repositorio.iniciarSesion es correcto navega a Rutas.HOME con popUpTo(Rutas.SPLASH) { inclusive = true }; si no, "Correo, teléfono o contraseña incorrectos".

7) TÉRMINOS — TerminosScreen
Barra superior con flecha y el título "Términos y condiciones", un texto con scroll (5 apartados: uso de la aplicación, datos personales, cancelaciones, responsabilidad y cambios; 18 sp, lineHeight 26, #52607C) y el botón "Entendido" abajo (sobre la barra de navegación) que vuelve atrás.

CIERRE: compila, prueba el flujo Splash -> Registro -> Inicio y Splash -> Login, comprueba que el botón y los enlaces de abajo se ven completos con barra de gestos y con barra de 3 botones, y dime los archivos modificados.
```

**Respuesta resumida:**
Se agregó la librería de íconos Font Awesome (`br.com.devsrsouza.compose.icons:font-awesome:1.1.1`) y un archivo `Escala.kt` que escala las medidas del diseño (360 x 780 dp) al tamaño real del teléfono. El Splash dibuja el logo con código y muestra la ilustración a todo el ancho; Registro y Login usan el campo del diseño (tarjeta de ícono + etiqueta + caja de texto) con validaciones. El `Repositorio` quedó con 7 especialidades, 16 médicos y login por correo o teléfono, y el modelo `Usuario` ganó el campo `telefono`.

**Qué tuve que corregir:**
- El build fallaba por dos archivos XML dañados en `res/drawable` (`ic_doctor_bienvenida.xml` e `ic_logo_saludplus.xml`, error "Final de archivo prematuro"); como no se usan, los eliminé.
- `RegistroScreen` no compilaba en la línea `telefono = tel` porque el modelo `Usuario` de mi rama no tenía el campo `telefono`; lo agregué.
- Se borró `SplashScreen.java`, un archivo sobrante que chocaba con `SplashScreen.kt`.
- En el teléfono la interfaz se veía demasiado grande y casi no cabía: agregué un factor general de tamaño y que el texto no crezca con el tamaño de fuente del sistema.
- En Registro y Login el enlace "¿Ya tienes cuenta?" y el botón quedaban muy abajo, tapados por la barra de navegación del teléfono: los reorganicé en un contenido con scroll y un pie fijo que respeta la barra de navegación y la barra de estado.
- El Splash tenía posiciones fijas y el botón "Comenzar" quedaba fuera de la pantalla: lo reorganicé en tres bloques (logo y textos arriba, ilustración en el espacio que sobra, botón y enlace abajo) para que siempre quepa.

---

## Prompt 2 — Inicio, Especialidades, Médicos y utilidades con LocalDate
**Commit:** `Parte 2: Inicio, Especialidades y Médicos según el diseño y utilidades de fechas con LocalDate`

**Prompt completo (para pegar en una IA):**

```text
ROL
Eres un desarrollador Android senior. Trabaja directamente sobre los archivos del proyecto abierto: IMPLEMENTA los cambios en los archivos, no entregues solo un plan ni ejemplos sueltos. Antes de tocar algo, lee los archivos que vas a modificar, navigation/Rutas.kt, navigation/AppNavigation.kt, data/model/*, data/repository/Repositorio.kt y ui/components/*.

PROYECTO
App Android "Clínica SaludPlus" (solo la App Paciente) hecha en Kotlin con Jetpack Compose y Material 3, paquete com.saludplus.citas, carpeta Semana06/ClinicaSaludPLus. NO hay base de datos ni servidor: los usuarios, especialidades, médicos y citas viven en memoria dentro del object data/repository/Repositorio.kt y se pierden al cerrar la app. Modelos: Usuario(id, nombre, correo, contrasena, telefono: String? = ""), Especialidad(id, nombre, descripcion), Medico(id, nombre, especialidadId, calificacion, resenas = 0, cmp = ""), Cita(id, usuarioId, medicoId, especialidadId, fecha, hora). Rutas (Rutas.kt): splash, registro, login, terminos, home, especialidades, medicos/{especialidadId}, fechahora/{medicoId}, confirmar/{medicoId}/{fecha}/{hora}, citaexitosa, miscitas, detallecita/{citaId}, perfil, resultados, notificaciones. Las fechas se guardan y viajan por las rutas como texto "yyyy-MM-dd".

REGLAS (no negociables)
1. No cambies el nombre, los parámetros ni el tipo de retorno de las funciones públicas del Repositorio (registrarUsuario, iniciarSesion, cerrarSesion, buscarEspecialidades, especialidadesDestacadas, obtenerEspecialidad, medicosPorEspecialidad, buscarMedicos, obtenerMedico, horariosDisponibles, agendarCita, citasDelUsuario, obtenerCita, cancelarCita, siguienteIdUsuario). Sí puedes cambiar los DATOS iniciales y agregar campos NUEVOS con valor por defecto a los modelos.
2. No cambies Rutas.kt ni los argumentos de navegación (Rutas.medicos(id), Rutas.fechaHora(medicoId), Rutas.confirmar(medicoId, fecha, hora), Rutas.detalleCita(id)).
3. Dependencias permitidas, y solo estas: material-icons-extended (ya está), la librería de íconos Font Awesome "br.com.devsrsouza.compose.icons:font-awesome:1.1.1" y la librería de imágenes Coil "io.coil-kt:coil-compose:2.7.0" (con el permiso INTERNET en el AndroidManifest). Sin base de datos. Sin archivos .java: todo en Kotlin. No dejes archivos sobrantes (por ejemplo un SplashScreen.java).
4. Ninguna pantalla terminada puede quedar con PantallaEnConstruccion.
5. Compila el proyecto (:app:assembleDebug) y corrige TODOS los errores antes de terminar.
6. GIT: no ejecutes ningún comando Git (ni status, log o diff), no leas .git, no cambies ni crees ramas. Los commits los hago yo. Al terminar, dime qué archivos creaste o modificaste.

DISEÑO DE REFERENCIA
Hay una imagen adjunta ("APP PACIENTE – Agendar citas médicas") con 7 pantallas: 1 Splash, 2 Crear cuenta, 3 Inicio, 4 Especialidades, 5 Médicos de Ginecología, 6 Seleccionar fecha y hora, 7 Confirmar cita. Las pantallas deben verse como ese diseño: mismos textos, mismos colores, mismas proporciones y cada elemento en su sitio. Los textos de ejemplo de la imagen (Juan Pérez, Setiembre 2023, etc.) son datos de muestra: la app muestra los datos reales.

ESCALA (para que quepa y se vea igual en cualquier teléfono)
Las medidas de este prompt están en dp (y sp para el texto) para una pantalla de referencia de 360 x 780 dp. Crea ui/theme/Escala.kt con una clase Escala(k, fuente) con las funciones d(valor): Dp = (valor * k).dp y s(valor): TextUnit = (valor * k / fuente).sp, y un @Composable rememberEscala() donde k = minOf(screenWidthDp / 360f, screenHeightDp / 780f, 1.15f) * FACTOR_DISENO, con FACTOR_DISENO = 0.85f (constante privada y comentada: 1.0 son las proporciones exactas de la imagen; bajarla hace todo más compacto) y fuente = fontScale del sistema pero nunca menor que 1 (así el texto NO crece cuando el usuario aumenta el tamaño de fuente del teléfono). Usa rememberEscala() en todas las pantallas: tamaño real = e.d(valor) y e.s(valor).

ZONAS SEGURAS (muy importante)
La app se dibuja de borde a borde (enableEdgeToEdge en MainActivity). Todo botón, enlace o pie que esté abajo debe quedar ENCIMA de la barra de navegación del teléfono (navigationBarsPadding) y todo contenido de arriba debajo de la barra de estado (statusBarsPadding). Nada importante puede quedar tapado. En pantallas largas usa un contenido con scroll y un pie fijo, no posiciones fijas desde el borde de abajo.

COLORES
Azul primario (botones, enlaces, día y hora seleccionados) #0468FD; azul deshabilitado #B7C9E8; texto principal #050A3A; azul marino de marca #0A1A6B; texto secundario #52607C; etiqueta de campo #4A5A7A; gris claro (placeholders, inactivos) #8D96AB; gris de íconos #9AA3B5. Fondo del Splash: degradado de #F3FAFE (arriba) a #E7F5FE. Bordes: campo #DDE3EA, tarjeta de ícono #E3E8F0, tarjeta #E8EDF2, hora #E8EDF3, caja de motivo #DFE2E5; divisor #EEF1F6. Rellenos: tarjeta de especialidad #F9FBFC, buscador #F1F4F7, tarjeta del médico #F3F7FD, hora sin seleccionar #F1F4F8, ícono de Confirmar #E8F0FE. Chip "Disponible": fondo #D9F8E8 y texto #118A3C; chip "Sin horarios": fondo #F3F4F6 y texto #6B7280. Estrella #F5A623; error #D93025. Tarjetas de Inicio: Agendar cita fondo #E5F1FE y texto #0468FD; Mis citas fondo #D4FDE9 y texto #07AF4E; Mis datos fondo #F2E9FD y texto #8736FD; Resultados fondo #FDF2E3 y texto #FD8407. Pon todos estos colores como constantes en ui/theme/Color.kt y úsalos desde ahí.

ÍCONOS E IMÁGENES
No uses imágenes para los íconos: usa los vectores de la librería Font Awesome (import compose.icons.FontAwesomeIcons, compose.icons.fontawesomeicons.Solid y compose.icons.fontawesomeicons.solid.NombreDelIcono, por ejemplo FontAwesomeIcons.Solid.User) con Icon(imageVector = ..., tint = ...). Para flechas, lupa, campana, chevrones, estrella, check y la barra inferior usa Material Icons. La única imagen del proyecto es la ilustración del Splash, que ya existe: R.drawable.ilustracion_bienvenida_saludplus (1122 x 1402 px, doctor con plantas y fondo celeste #E7F5FE).

PARTE 2 DE 3 — INICIO, ESPECIALIDADES, MÉDICOS Y UTILIDADES DE FECHAS
Alcance: minSdk 26, utilidades con java.time.LocalDate, fotos de los médicos, barra inferior, y las pantallas Inicio, Especialidades y Médicos. Ya existen (Parte 1): Escala, colores, tema, BotonPrincipal, CampoBusqueda, BarraSuperior, IconoEspecialidad, cargoMedico, los datos del Repositorio y las pantallas de acceso. No toques esas.

1) minSdk Y UTILIDADES DE FECHAS
- En app/build.gradle.kts sube minSdk de 24 a 26 (java.time.LocalDate lo requiere).
- Crea util/FechasEs.kt (paquete com.saludplus.citas.util) con un object FechasEs, solo con LocalDate, sin librerías externas: esHabil(fecha) (true si no es sábado ni domingo); primerDiaHabil(desde = LocalDate.now()) (si cae en fin de semana avanza al lunes); diasHabiles(desde, cantidad = 5) (lista de días hábiles consecutivos desde el primer día hábil, saltando sábados y domingos); diaSemanaCorto(fecha) ("Lun", "Mar", "Mié", "Jue", "Vie"); nombreMes(fecha) (en español con mayúscula inicial; septiembre se escribe "Setiembre"); mesYAnio(dias: List<LocalDate>) ("Octubre 2026"; si los días cruzan de mes "Octubre – Noviembre 2026"; si cruzan de año "Diciembre 2026 – Enero 2027"); fechaLarga(fecha: LocalDate) y fechaLarga(iso: String) ("Martes 16 de setiembre 2026": día de la semana con mayúscula, mes en minúscula, sin "de" antes del año; la versión String recibe "yyyy-MM-dd" y si no se puede convertir devuelve el mismo texto); fechaCorta(iso: String) ("Mié 7 oct 2026"); rangoHora(hora: String) ("09:30" -> "09:30 a 10:00", las citas duran 30 minutos).

2) FOTOS DE LOS MÉDICOS (ImagenesDiseno.kt)
FotoMedico(medico, tamano): círculo del tamaño pedido con fondo pastel y, dentro, el ícono FontAwesomeIcons.Solid.UserMd (persona con estetoscopio) tintado, de 0.56 del tamaño. Encima, un AsyncImage de Coil (coil.compose.AsyncImage, contentScale = Crop, fillMaxSize) con una foto real: https://randomuser.me/api/portraits/women/N.jpg para las "Dra." y .../men/N.jpg para los "Dr.". Fijos: Ana Torres women/44, Claudia Rojas women/65, Luis Ramírez men/32, Mariana Soto women/68; para los demás elige N de una lista según el id del médico. El ícono sirve de respaldo: se ve mientras la foto carga y se queda si no hay internet. Colores del círculo (fondo y glifo), según id % 4: #DCEBFF/#0468FD, #D4FDE9/#07AF4E, #F2E9FD/#8736FD, #FDF2E3/#FD8407. Ya agregaste el permiso INTERNET en la Parte 1.

3) BARRA INFERIOR — BarraInferior(rutaActual, navController)
Fondo blanco, una línea superior de 1 dp #EEF1F6 y una fila de 72 de alto más navigationBarsPadding con 4 destinos del mismo ancho: Inicio (Icons.Filled.Home, Rutas.HOME), Citas (CalendarMonth, Rutas.MIS_CITAS), Resultados (Description, Rutas.RESULTADOS) y Perfil (Person, Rutas.PERFIL). Cada uno: ícono de 32 y debajo la etiqueta de 16 sp; el activo en #0468FD con etiqueta Bold y los demás en #8D96AB. Al tocar uno que no es el actual: navigate(ruta) { popUpTo(Rutas.HOME); launchSingleTop = true }. Es un Column/Row propio, no el NavigationBar por defecto (sin el óvalo indicador).

4) PANTALLA 3 — HomeScreen
Scaffold con containerColor blanco, contentWindowInsets = WindowInsets(0.dp) y bottomBar = BarraInferior(Rutas.HOME). Contenido en una columna con scroll vertical:
a) Fila superior con 56 de margen arriba, 18 a la izquierda y 14 a la derecha: a la izquierda (con 10 de padding arriba) "¡Hola, <primer nombre de Repositorio.usuarioActual, o 'Paciente'>!" (31 sp, Bold, #050A3A, línea 38), 6 de espacio y "¿Qué deseas hacer hoy?" (21 sp, #52607C, línea 26); a la derecha la campana (Icons.Filled.Notifications, 32, #050A3A) que abre Rutas.NOTIFICACIONES.
b) 27 de espacio y una cuadrícula de 2 x 2 tarjetas con 16 de margen lateral, 12 entre columnas y 14 entre filas; cada tarjeta mide 128 de alto, esquinas 16, sin sombra, con el fondo de su color y dentro (columna centrada) 19 de espacio, el ícono de Font Awesome de 54 del color del texto, 12 de espacio y el texto (19 sp, Bold, línea 24). Agendar cita (Solid.CalendarAlt, azul) -> Rutas.ESPECIALIDADES; Mis citas (Solid.CalendarCheck, verde #07AF4E) -> Rutas.MIS_CITAS; Mis datos (Solid.User, morado #8736FD) -> Rutas.PERFIL; Resultados (Solid.FileAlt, naranja #FD8407) -> Rutas.RESULTADOS.
c) 32 de espacio y una fila con 16 de margen: "Especialidades destacadas" (18 sp, Bold) a la izquierda y "Ver todas" (18 sp, Bold, azul) a la derecha -> Rutas.ESPECIALIDADES.
d) 14 de espacio y un LazyRow con Repositorio.especialidadesDestacadas(), contentPadding horizontal 14 y 8 entre tarjetas. Cada tarjeta: 108 de ancho y 140 de alto, esquinas 16, relleno #F9FBFC, borde 1 dp #E8EDF2, padding horizontal 2; dentro, 10 de espacio, IconoEspecialidad de 64, 8 de espacio y el nombre. Al tocar -> Rutas.medicos(id). Las 3 primeras tarjetas deben entrar completas en la pantalla.
e) El NOMBRE de la especialidad debe ir centrado y completo: componente NombreEspecialidad(texto, anchoMax = 98) que mide el texto con rememberTextMeasurer y baja el tamaño de letra (desde 17 sp, de a 0.5, hasta un mínimo de 9) hasta que la palabra más larga entre en una sola línea. Si el nombre es de una sola palabra (Ginecología, Cardiología...) va en UNA línea con softWrap = false; si tiene espacios (Medicina General) puede ir en dos líneas cortando solo en el espacio. Jamás se parte una palabra a la mitad. Termina la columna con 24 de espacio.

5) PANTALLA 4 — EspecialidadesScreen
Columna blanca: BarraSuperior("Especialidades", con flecha atrás), 4 de espacio, CampoBusqueda ("Buscar especialidad...", 16 de margen lateral), 12 de espacio y una LazyColumn. La lista se calcula desde Repositorio.buscarEspecialidades(texto) en cada recomposición, así que se filtra sola en tiempo real. Cada fila es TarjetaEspecialidad: 84 de alto, padding start 32 y end 20, IconoEspecialidad de 46, 19 de espacio, una columna con 13 de padding arriba, el nombre (18 sp, Bold, #050A3A, línea 24), 7 de espacio y la descripción (16 sp, #52607C, línea 20), y a la derecha el chevron (Icons.Filled.ChevronRight, 24, #9AA3B5). Debajo de cada fila un HorizontalDivider #EEF1F6 con 97 de sangría. Al tocar -> Rutas.medicos(id). Si no hay resultados, "No se encontraron especialidades" centrado arriba.

6) PANTALLA 5 — MedicosScreen(especialidadId)
Columna blanca: BarraSuperior("Médicos de <nombre de la especialidad>", flecha atrás y, como acción, la lupa de Material (30) que muestra u oculta un CampoBusqueda "Buscar médico..."; al ocultarlo se borra el texto), 14 de espacio y una LazyColumn con Repositorio.buscarMedicos(especialidadId, texto), que ya viene ordenado por calificación de mayor a menor (sortedByDescending). Cada fila mide 157 de alto, es clickable y lleva: FotoMedico de 96 con 18 a la izquierda y 15 arriba; una columna con 123 a la izquierda y 24 arriba con el nombre (19 sp, Bold, línea 24), 7 de espacio, cargoMedico (18 sp, #52607C), 7 de espacio y una fila con la estrella (Icons.Filled.Star, 22, #F5A623) y "4.9 (120)" = calificación y reseñas (18 sp, #52607C); y abajo a la derecha (18 de margen derecho y 23 abajo) un chip de esquinas 8 con padding 10 x 4 y texto de 15 sp SemiBold. Texto del chip: se toman FechasEs.diasHabiles(LocalDate.now(), 5) y el primer día en que Repositorio.horariosDisponibles(medicoId, día) no esté vacío: si es hoy "Disponible hoy", si es mañana "Disponible mañana", si es otro "Disponible esta semana" (verde #D9F8E8/#118A3C); si ninguno tiene horarios, "Sin horarios" en gris. Entre filas un HorizontalDivider con 16 de margen. Al tocar una fila -> Rutas.fechaHora(medico.id). Lista vacía: "No hay médicos disponibles".

CIERRE: compila y prueba Inicio -> Agendar cita -> Especialidades (escribe en el buscador y comprueba que filtra) -> Ginecología -> Médicos. Comprueba que "Ginecología" y "Cardiología" se ven completas y en una sola línea en las tarjetas destacadas y que la barra inferior navega a los 4 destinos. Dime los archivos modificados.
```

**Respuesta resumida:**
Inicio muestra el saludo con el nombre, las 4 tarjetas, un `LazyRow` de especialidades destacadas y la `NavigationBar` (Inicio, Citas, Resultados, Perfil). Especialidades usa un `LazyColumn` con buscador que filtra en tiempo real. Médicos recibe `especialidadId`, ordena por calificación de mayor a menor y muestra una etiqueta de disponibilidad calculada con los horarios libres. Se creó `util/FechasEs.kt` (días hábiles, mes y año, fecha en español, rango de hora).

**Qué tuve que corregir:**
- `LocalDate` necesita API 26 y el proyecto tenía `minSdk = 24`: lo subí a 26 en `app/build.gradle.kts`.
- El usuario de demostración tenía el teléfono 987654321, el mismo del ejemplo del diseño, y al registrarme con ese número salía "Ya existe una cuenta": cambié el teléfono del usuario demo.
- Las fotos de los médicos, recortadas de una captura pequeña del diseño, se veían borrosas: las reemplacé por fotos reales de retratos de ejemplo (randomuser.me) cargadas con la librería Coil; agregué el permiso INTERNET, y si la foto no carga o no hay conexión se ve el ícono de persona con estetoscopio (Font Awesome) en un círculo de color. Eliminé las imágenes recortadas.
- Los nombres de las especialidades destacadas de Inicio se partían en dos líneas (por ejemplo "Ginecología"): ensanché las tarjetas y ajusté el tamaño de la letra midiendo el texto para que cada palabra entre completa.
- Los horarios base tenían 12 horarios (con la tarde); los dejé en los 9 del diseño (08:00 a 12:00).

---

## Prompt 3 — Calendario dinámico, Confirmar cita, Cita agendada y resto de pantallas
**Commit:** `Parte 3: calendario dinámico con LocalDate, Confirmar y Cita agendada, con PROMPTS.md`

**Prompt completo (para pegar en una IA):**

```text
ROL
Eres un desarrollador Android senior. Trabaja directamente sobre los archivos del proyecto abierto: IMPLEMENTA los cambios en los archivos, no entregues solo un plan ni ejemplos sueltos. Antes de tocar algo, lee los archivos que vas a modificar, navigation/Rutas.kt, navigation/AppNavigation.kt, data/model/*, data/repository/Repositorio.kt y ui/components/*.

PROYECTO
App Android "Clínica SaludPlus" (solo la App Paciente) hecha en Kotlin con Jetpack Compose y Material 3, paquete com.saludplus.citas, carpeta Semana06/ClinicaSaludPLus. NO hay base de datos ni servidor: los usuarios, especialidades, médicos y citas viven en memoria dentro del object data/repository/Repositorio.kt y se pierden al cerrar la app. Modelos: Usuario(id, nombre, correo, contrasena, telefono: String? = ""), Especialidad(id, nombre, descripcion), Medico(id, nombre, especialidadId, calificacion, resenas = 0, cmp = ""), Cita(id, usuarioId, medicoId, especialidadId, fecha, hora). Rutas (Rutas.kt): splash, registro, login, terminos, home, especialidades, medicos/{especialidadId}, fechahora/{medicoId}, confirmar/{medicoId}/{fecha}/{hora}, citaexitosa, miscitas, detallecita/{citaId}, perfil, resultados, notificaciones. Las fechas se guardan y viajan por las rutas como texto "yyyy-MM-dd".

REGLAS (no negociables)
1. No cambies el nombre, los parámetros ni el tipo de retorno de las funciones públicas del Repositorio (registrarUsuario, iniciarSesion, cerrarSesion, buscarEspecialidades, especialidadesDestacadas, obtenerEspecialidad, medicosPorEspecialidad, buscarMedicos, obtenerMedico, horariosDisponibles, agendarCita, citasDelUsuario, obtenerCita, cancelarCita, siguienteIdUsuario). Sí puedes cambiar los DATOS iniciales y agregar campos NUEVOS con valor por defecto a los modelos.
2. No cambies Rutas.kt ni los argumentos de navegación (Rutas.medicos(id), Rutas.fechaHora(medicoId), Rutas.confirmar(medicoId, fecha, hora), Rutas.detalleCita(id)).
3. Dependencias permitidas, y solo estas: material-icons-extended (ya está), la librería de íconos Font Awesome "br.com.devsrsouza.compose.icons:font-awesome:1.1.1" y la librería de imágenes Coil "io.coil-kt:coil-compose:2.7.0" (con el permiso INTERNET en el AndroidManifest). Sin base de datos. Sin archivos .java: todo en Kotlin. No dejes archivos sobrantes (por ejemplo un SplashScreen.java).
4. Ninguna pantalla terminada puede quedar con PantallaEnConstruccion.
5. Compila el proyecto (:app:assembleDebug) y corrige TODOS los errores antes de terminar.
6. GIT: no ejecutes ningún comando Git (ni status, log o diff), no leas .git, no cambies ni crees ramas. Los commits los hago yo. Al terminar, dime qué archivos creaste o modificaste.

DISEÑO DE REFERENCIA
Hay una imagen adjunta ("APP PACIENTE – Agendar citas médicas") con 7 pantallas: 1 Splash, 2 Crear cuenta, 3 Inicio, 4 Especialidades, 5 Médicos de Ginecología, 6 Seleccionar fecha y hora, 7 Confirmar cita. Las pantallas deben verse como ese diseño: mismos textos, mismos colores, mismas proporciones y cada elemento en su sitio. Los textos de ejemplo de la imagen (Juan Pérez, Setiembre 2023, etc.) son datos de muestra: la app muestra los datos reales.

ESCALA (para que quepa y se vea igual en cualquier teléfono)
Las medidas de este prompt están en dp (y sp para el texto) para una pantalla de referencia de 360 x 780 dp. Crea ui/theme/Escala.kt con una clase Escala(k, fuente) con las funciones d(valor): Dp = (valor * k).dp y s(valor): TextUnit = (valor * k / fuente).sp, y un @Composable rememberEscala() donde k = minOf(screenWidthDp / 360f, screenHeightDp / 780f, 1.15f) * FACTOR_DISENO, con FACTOR_DISENO = 0.85f (constante privada y comentada: 1.0 son las proporciones exactas de la imagen; bajarla hace todo más compacto) y fuente = fontScale del sistema pero nunca menor que 1 (así el texto NO crece cuando el usuario aumenta el tamaño de fuente del teléfono). Usa rememberEscala() en todas las pantallas: tamaño real = e.d(valor) y e.s(valor).

ZONAS SEGURAS (muy importante)
La app se dibuja de borde a borde (enableEdgeToEdge en MainActivity). Todo botón, enlace o pie que esté abajo debe quedar ENCIMA de la barra de navegación del teléfono (navigationBarsPadding) y todo contenido de arriba debajo de la barra de estado (statusBarsPadding). Nada importante puede quedar tapado. En pantallas largas usa un contenido con scroll y un pie fijo, no posiciones fijas desde el borde de abajo.

COLORES
Azul primario (botones, enlaces, día y hora seleccionados) #0468FD; azul deshabilitado #B7C9E8; texto principal #050A3A; azul marino de marca #0A1A6B; texto secundario #52607C; etiqueta de campo #4A5A7A; gris claro (placeholders, inactivos) #8D96AB; gris de íconos #9AA3B5. Fondo del Splash: degradado de #F3FAFE (arriba) a #E7F5FE. Bordes: campo #DDE3EA, tarjeta de ícono #E3E8F0, tarjeta #E8EDF2, hora #E8EDF3, caja de motivo #DFE2E5; divisor #EEF1F6. Rellenos: tarjeta de especialidad #F9FBFC, buscador #F1F4F7, tarjeta del médico #F3F7FD, hora sin seleccionar #F1F4F8, ícono de Confirmar #E8F0FE. Chip "Disponible": fondo #D9F8E8 y texto #118A3C; chip "Sin horarios": fondo #F3F4F6 y texto #6B7280. Estrella #F5A623; error #D93025. Tarjetas de Inicio: Agendar cita fondo #E5F1FE y texto #0468FD; Mis citas fondo #D4FDE9 y texto #07AF4E; Mis datos fondo #F2E9FD y texto #8736FD; Resultados fondo #FDF2E3 y texto #FD8407. Pon todos estos colores como constantes en ui/theme/Color.kt y úsalos desde ahí.

ÍCONOS E IMÁGENES
No uses imágenes para los íconos: usa los vectores de la librería Font Awesome (import compose.icons.FontAwesomeIcons, compose.icons.fontawesomeicons.Solid y compose.icons.fontawesomeicons.solid.NombreDelIcono, por ejemplo FontAwesomeIcons.Solid.User) con Icon(imageVector = ..., tint = ...). Para flechas, lupa, campana, chevrones, estrella, check y la barra inferior usa Material Icons. La única imagen del proyecto es la ilustración del Splash, que ya existe: R.drawable.ilustracion_bienvenida_saludplus (1122 x 1402 px, doctor con plantas y fondo celeste #E7F5FE).

PARTE 3 DE 3 — CALENDARIO DINÁMICO (FASE 2), CONFIRMAR CITA, CITA AGENDADA Y RESTO DE PANTALLAS
Alcance: la mejora obligatoria de la Fase 2 (calendario dinámico con java.time.LocalDate en la Pantalla 6), la Pantalla 7, Cita agendada, y el estilo del resto de pantallas. Ya existen (Partes 1 y 2): Escala, colores, componentes, FechasEs, FotoMedico, cargoMedico, BarraInferior y las pantallas de acceso, Inicio, Especialidades y Médicos.

1) PANTALLA 6 — FechaHoraScreen(medicoId): CALENDARIO DINÁMICO
A) Lógica obligatoria (en la Fase 1 los días eran una lista fija; ahora se generan con LocalDate):
1. Se muestran los próximos 5 días hábiles a partir de hoy, sin sábados, domingos ni días pasados. Si hoy es sábado o domingo, empieza el lunes.
2. Estado con rememberSaveable (sobrevive a una rotación): semana (Int, 0 = semana actual), fechaIso (día seleccionado como texto "yyyy-MM-dd", al inicio el primer día hábil) y hora (String?, null al inicio). inicio = remember { FechasEs.primerDiaHabil(LocalDate.now()) }.
3. Los 5 días mostrados = FechasEs.diasHabiles(inicio.plusWeeks(semana), 5).
4. Flechas "<" y ">" (Icons.Filled.ChevronLeft y ChevronRight, 28): ">" suma una semana; "<" resta una y está DESHABILITADA (gris, sin responder) cuando semana == 0, así que no se puede retroceder antes de la semana actual. Al cambiar de semana se selecciona automáticamente el primer día de esa semana y la hora se reinicia (hora = null).
5. El título entre las flechas es FechasEs.mesYAnio(dias) ("Octubre 2026") y cambia según la semana mostrada.
6. Al tocar otro día cambia fechaIso y la hora seleccionada se REINICIA (hora = null).
7. Los horarios se recalculan SOLOS en cada recomposición a partir del estado: Repositorio.horariosDisponibles(medicoId, fechaIso). No los guardes en una variable aparte ni los "actualices" a mano. Así, un horario ya reservado para ese médico y esa fecha NO aparece, y el calendario nunca rompe el bloqueo de horarios. No cambies Repositorio.horariosDisponibles.
8. Los horarios van en un LazyVerticalGrid de 3 columnas. Si no hay horarios: "No hay horarios disponibles para este día".
9. "Continuar" solo se habilita cuando hay una hora elegida (el día siempre está elegido) y navega con Rutas.confirmar(medicoId, fechaIso, hora).
B) Diseño (de arriba hacia abajo, en una Column blanca): BarraSuperior("Seleccionar fecha y hora", flecha atrás); 11 de espacio; tarjeta del médico (margen lateral 16, 122 de alto, esquinas 16, fondo #F3F7FD, padding start 12) con FotoMedico de 96, 13 de espacio y una columna centrada con el nombre (22 sp, Bold, línea 28), 13 de espacio y cargoMedico (21 sp, #52607C); 30 de espacio; fila del mes (28 de alto, padding horizontal 27) con el chevron izquierdo, el mes y año centrados (21 sp, SemiBold) y el chevron derecho; 34 de espacio; fila de 5 columnas de 58 de ancho, 10 de separación y 15 de margen lateral: cada columna lleva la abreviatura (Lun...Vie; 17 sp, línea 20, #52607C; la del día seleccionado en azul y Bold), 5 de espacio y un cuadro de 58 x 64, esquinas 12, con el número del día (25 sp, Bold); el seleccionado con fondo #0468FD y número blanco, los demás sin fondo; 27 de espacio; la cuadrícula de horas con weight(1f), 19 de margen lateral, 23 entre columnas y 15.5 entre filas, con celdas de 59 de alto, esquinas 12, relleno #F1F4F8 y borde 1 dp #E8EDF3, texto de 20 sp; la seleccionada en azul con texto blanco; al final el botón "Continuar" (BotonPrincipal) en una caja con navigationBarsPadding y padding de 16 a los lados, 8 arriba y 20 abajo. Solo días hábiles: no hay "Sáb" ni "Dom".

2) PANTALLA 7 — ConfirmarCitaScreen(medicoId, fecha, hora)
BarraSuperior("Confirmar cita", flecha atrás); contenido con scroll y, fuera del scroll, el botón abajo.
- 7 de espacio y tarjeta del médico (igual a la de la Pantalla 6, 122 de alto) con tres líneas: nombre (22 sp, Bold), cargoMedico (21 sp) y "CMP: <cmp>" (20 sp, #52607C).
- 11 de espacio y cuatro filas (FilaInfo privada): padding start 26, 2 arriba y 28.5 abajo; a la izquierda un cuadro de 44 con esquinas 12 y fondo #E8F0FE (6 de padding arriba) con el ícono de Font Awesome de 22 en azul, 20 de espacio y una columna con la etiqueta (16 sp, #52607C) y el valor (18 sp, SemiBold, #050A3A). Fila 1 "Fecha" (Solid.CalendarAlt): FechasEs.fechaLarga(fecha), por ejemplo "Martes 16 de setiembre 2026" (la fecha llega como "yyyy-MM-dd" por la ruta y SOLO se muestra en español; no cambies lo que viaja por la ruta ni lo que guarda el Repositorio). Fila 2 "Hora" (Solid.Clock): FechasEs.rangoHora(hora), por ejemplo "09:30 a 10:00". Fila 3 "Tipo de atención" (Solid.UserMd): "Consulta presencial". Fila 4 "Dirección" (Solid.MapMarkerAlt): "Av. Los Olivos 123" y en otra línea "Lima".
- 2 de espacio y "Motivo de la consulta" (18 sp, Bold) seguido de " (opcional)" en peso normal y #52607C; 14 de espacio; caja multilínea de 74 de alto, margen lateral 17, esquinas 10, borde 1 dp #DFE2E5, padding 14 x 12, BasicTextField con el placeholder "Describe tu motivo" (19 sp, #52607C). El motivo puede quedarse solo en pantalla (no cambies la firma de agendarCita).
- Abajo (con navigationBarsPadding, margen 17 a los lados, 8 arriba y 20 abajo): BotonPrincipal "Agendar cita". Si Repositorio.agendarCita(medicoId, fecha, hora) devuelve true, navega a Rutas.CITA_EXITOSA con popUpTo(Rutas.HOME); si devuelve false, muestra "Ese horario ya fue ocupado; elige otro".

3) CITA AGENDADA — CitaExitosaScreen (no está en la imagen; mismo estilo)
Fondo blanco con navigationBarsPadding y 17 de margen lateral, centrado: 90 de espacio, un círculo de 120 con fondo #D4FDE9 y el ícono Icons.Filled.CheckCircle de 80 en #07AF4E, "¡Cita agendada!" (28 sp, Bold) y "Te esperamos en la clínica" (18 sp, #52607C); 24 de espacio y una tarjeta (esquinas 16, fondo #F3F7FD, padding 16) con filas Médico, Especialidad, Fecha (FechasEs.fechaLarga) y Hora (FechasEs.rangoHora) leídas de Repositorio.ultimaCita. Abajo (spacer con peso): "Ver mis citas" (BotonPrincipal -> Rutas.MIS_CITAS con popUpTo(Rutas.HOME)) y "Volver al inicio" (OutlinedButton de 62 de alto, esquinas 14, borde azul -> Rutas.HOME con popUpTo(Rutas.HOME) { inclusive = true }).

4) RESTO DE PANTALLAS (mismo estilo, sin cambiar su lógica)
Scaffold con containerColor blanco, contentWindowInsets = WindowInsets(0.dp) y BarraSuperior. Tarjetas blancas con borde 1 dp #E8EDF2 y esquinas 16, textos con la paleta de colores, botones de 62 de alto.
- MisCitasScreen (con BarraInferior Rutas.MIS_CITAS): LazyColumn de TarjetaCita (ícono CalendarMonth azul, médico 19 sp Bold, especialidad 17 sp, "fecha · hora" 16 sp con la fecha corta FechasEs.fechaCorta); al tocar abre Rutas.detalleCita(id). Si no hay citas: "Aún no tienes citas agendadas" centrado.
- DetalleCitaScreen: tarjeta #F3F7FD con Médico, Especialidad, Fecha (fechaLarga) y Hora (rangoHora), y el botón rojo "Cancelar cita" que abre un AlertDialog ("¿Seguro que deseas cancelar esta cita?", "Sí, cancelar" / "No"); al confirmar llama a Repositorio.cancelarCita(id) y vuelve atrás.
- PerfilScreen (con BarraInferior Rutas.PERFIL): ícono de cuenta, nombre, tarjeta con Correo, Teléfono y Citas agendadas, y el botón rojo "Cerrar sesión" (Repositorio.cerrarSesion() y navegar a Rutas.SPLASH limpiando toda la pila con popUpTo(navController.graph.id) { inclusive = true }).
- ResultadosScreen (con BarraInferior Rutas.RESULTADOS): lista fija de 4 exámenes (modelo propio) con fecha corta y estado. NotificacionesScreen: un mensaje por cita ("Recordatorio: tienes cita con <médico> el <fecha larga> a las <hora>") o "No tienes notificaciones".
- Todas las pantallas con botones abajo respetan navigationBarsPadding.

5) PRUEBAS QUE DEBES HACER Y REPORTARME
a) Con hoy miércoles se ven Mié, Jue, Vie, Lun y Mar (5 días hábiles, ninguno sábado ni domingo). Si hoy fuera fin de semana, el primer día es el lunes.
b) "<" está deshabilitada en la primera semana; ">" avanza 7 días y el mes y año se actualizan; "<" vuelve a la semana anterior.
c) Agendo con el Dr. X el día D a las 09:30: al volver a Fecha y hora con el MISMO médico y el mismo día, las 09:30 ya no aparecen; con OTRO médico sí aparecen.
d) Cambiar de día reinicia la hora y deshabilita "Continuar"; elegir una hora lo habilita.
e) En Confirmar cita la fecha sale en texto en español con el día de la semana correcto.
f) Flujo completo: Inicio -> Especialidades -> Médicos -> Fecha y hora -> Confirmar -> Cita agendada -> Mis citas, y la cita aparece en Mis citas. Al presionar Atrás desde Cita agendada se vuelve a Inicio, no a Confirmar.
g) Ningún botón ni enlace queda tapado por la barra de navegación del teléfono.

CIERRE: compila, corrige errores y dime los archivos modificados.
```

**Respuesta resumida:**
`FechaHoraScreen` guarda la semana (0 = actual), el día y la hora; los 5 días salen de `FechasEs.diasHabiles(...)`; la flecha izquierda se deshabilita en la semana actual; al cambiar de día o de semana la hora vuelve a `null`; los horarios se calculan con `Repositorio.horariosDisponibles(medicoId, fecha)` en cada recomposición, por eso un horario reservado deja de aparecer; "Continuar" solo se habilita con una hora elegida. Confirmar cita muestra la fecha con `FechasEs.fechaLarga` ("Martes 16 de setiembre 2026") y la hora como rango.

**Qué tuve que corregir:**
- El código generado usaba `Modifier.padding(horizontal = ..., top = ..., bottom = ...)`, que no existe como sobrecarga; lo cambié por `start`, `end`, `top` y `bottom`.
- La fecha sigue viajando por la ruta y guardándose como `yyyy-MM-dd`; solo se muestra en español, para no romper el bloqueo de horarios reservados.
- Si hoy es sábado o domingo el calendario empieza el lunes, y si la semana cruza de mes el título muestra ambos meses.

---

## Observaciones propias
- (Escribe aquí lo que notaste al probar la app en tu teléfono.)
