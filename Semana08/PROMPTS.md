# Prompts utilizados — Mejora IA Lab8

**Proyecto:** HuellitasVet · TECSUP · Programación en Móviles · Laboratorio 08 · Fase 2.  
**Herramientas que participaron:** Gemini en Android Studio (envío confirmado del prompt inicial) y asistencia de ChatGPT para propuestas, recursos, correcciones y archivos de código.  
**Arquitectura exigida:** Compose UI → ViewModel → interfaz `VetRepository` → `VetRepositoryRoom` → DAO → Room; DataStore para preferencias.

> **Integridad del registro.** El usuario confirmó expresamente que envió a Gemini el **prompt global inicial**. Los demás textos se redactaron como instrucciones completas para continuar en Gemini, pero no hay constancia de que todos se enviaran allí. Por eso están etiquetados como **preparados/no confirmados**. Si los pegaste en Gemini, puedes cambiar su estado a «enviado» y añadir los resultados reales; no se debe atribuir automáticamente a Gemini código elaborado con ChatGPT.

### Cómo se documentó cada prompt

Se respeta el formato de TECSUP en todas las secciones: **Texto enviado a Gemini**, **Qué generó la IA**, **Qué tuve que corregir yo** y **¿Respeta MVVM?**. Cuando solo existe una propuesta, la misma sección incluye **texto exacto preparado**, pero se identifica que el envío no está verificado. Los textos de prompts se copian íntegros, sin resumirlos.



## Prompt 1 — Instrucciones generales de diseño (ENVÍO A GEMINI CONFIRMADO)

**Texto enviado a Gemini:** **Confirmado por el usuario**, que indicó que copió y pegó estas instrucciones. Se reproduce el texto disponible literalmente:

````text
MEJORA IA LAB8 — REDISEÑO PROFESIONAL GLOBAL

Actúa como desarrollador Android Senior y diseñador UX/UI especializado en Kotlin, Jetpack Compose, Material 3, Room, DataStore, Navigation Compose y arquitectura MVVM + Repository.

Estoy trabajando en **HuellitasVet**, una aplicación veterinaria con dos perfiles: Dueño y Veterinario.

Quiero rediseñar profesionalmente todas las pantallas existentes hasta conseguir una calidad visual comparable a una aplicación comercial de Google Play.

**No crees otro proyecto ni reemplaces mi arquitectura actual.**

## Diseño y colores

**Perfil Dueño**

- Verde principal: #059669
- Verde secundario: #10B981
- Verde menta: #ECFDF5
- Fondo claro: #F6FDF9
- Texto oscuro: #1F2937

**Perfil Veterinario**

- Azul principal: #1D4ED8
- Azul secundario: #2563EB
- Azul claro: #DBEAFE
- Fondo claro: #F7F9FF
- Texto oscuro: #1F2937

Crea una identidad visual consistente mediante temas, colores centralizados y componentes reutilizables. Incluye compatibilidad real con modo oscuro.

## Pantallas completas

Todas las pantallas deben aprovechar adecuadamente la anchura y altura útil del dispositivo.

Usa Scaffold, WindowInsets, fillMaxSize, LazyColumn, weight e imePadding cuando corresponda.

Corrige pantallas demasiado pequeñas, componentes desproporcionados, espacios vacíos gigantes, barras mal alineadas y formularios cortados por el teclado.

Mantén correctamente las barras del sistema Android, sin duplicar paddings.

## Imágenes e iconos

Utiliza ilustraciones bonitas de perros, gatos y otras mascotas. Incluye iconos de huellas, calendario, vacunas, consultas, baños, medicamentos, pacientes y servicios.

Los iconos de perfiles humanos deben representar personas, no mascotas.

Prefiere recursos locales SVG convertidos a VectorDrawable, ImageVector o PNG/WebP incorporados al proyecto. No dependas de enlaces de imágenes de Internet ni inventes archivos de recursos inexistentes.

Incluye avatares adecuados según la especie de la mascota.

## Componentes

Mejora:

- Tarjetas, encabezados y botones.
- Navegación inferior.
- Campos de texto.
- Selectores y filtros.
- Estados vacíos.
- Mensajes de errores y confirmaciones.
- Indicadores de carga.
- Tipografía y espaciados.
- Animaciones suaves y transiciones.
- Gráficos existentes.

## Reglas técnicas obligatorias

- La interfaz Compose nunca accederá directamente a DAO ni Repository.
- Los ViewModels solo conocerán la interfaz VetRepository.
- Conserva Room, DataStore, los datos guardados y toda la navegación.
- No cambies funciones de negocio que ya funcionan.
- No uses XML para las pantallas.
- No generes clases duplicadas.
- No cambies el nombre de paquetes sin necesidad.
- Usa los nombres reales de clases, componentes y rutas.
- Mantén accesibilidad, buen contraste y controles táctiles cómodos.
- No sustituyas datos reales por números falsos.
- No elimines funcionalidades existentes.

Antes de modificar, inspecciona el proyecto actual. Indica qué archivos deben cambiar.

Después implementaré cada pantalla con prompts individuales.

**En este primer paso, analiza el proyecto y prepara los componentes visuales compartidos y el tema global. No realices commits automáticamente.**
````

**Qué generó la IA:** Este prompt pidió analizar el proyecto y preparar el tema global. No se dispone de una relación verificable de archivos creados **por Gemini** como resultado de ese envío. En el proyecto fuente posterior se encuentran componentes de interfaz y temas, pero muchos cambios también recibieron asistencia de ChatGPT.

**Qué tuve que corregir yo:** Durante la evolución del diseño se observó que Login quedaba demasiado blanco, los perfiles mostraban poca jerarquía, el historial clínico estaba comprimido y algunos textos eran pequeños. Se solicitaron revisiones visuales. También se entregó una corrección de `InicioScreen.kt` para un texto multilínea mal escrito en Kotlin; falta verificar la compilación final local.

**¿Respeta MVVM?:** **En principio, sí para la intención del prompt**, que exige explícitamente separar View, ViewModel y Repository. El cumplimiento del código resultante requiere inspección y compilación; en una revisión posterior los contadores de Perfil se trasladaron a `PerfilViewModel` para que la vista dejara de consultar Repository directamente.



## Prompt 2 — Commit 1 de cinco: Rediseño profesional completo de todas las pantallas (PREPARADO)

**Estado:** prompt completo **preparado durante la conversación**; el envío a Gemini no fue confirmado. El texto siguiente es el que se propuso para copiar y pegar (no se presenta como registro de ejecución en Gemini).

**Texto enviado a Gemini:** **Sin confirmación de envío**. **Texto completo preparado:**

````text
# PROMPT 1 — Rediseño profesional completo de todas las pantallas

**Contexto:** Ya te envié las instrucciones generales «MEJORA IA LAB8 — REDISEÑO PROFESIONAL GLOBAL». Ahora quiero que pases del análisis a la implementación integral del diseño. Trabaja SOLO sobre el proyecto HuellitasVet actual, en rama `mejora-ia-lab8`, en Kotlin, Jetpack Compose, Material 3, Navigation Compose, Room, DataStore y MVVM + VetRepository. No reconstruyas el proyecto desde cero.

## Dirección artística
Eleva todas las pantallas a una estética comercial premium: editorial veterinaria limpia, moderna, cálida, con fotografías reales de mascotas (no dibujos infantiles), iconografía coherente, espaciado generoso pero eficiente, tarjetas con proporciones correctas, jerarquía visual legible. No repitas monótonamente los mismos colores; busca armonía y personalidad. Paleta propuesta, ajustable justificadamente: fondo marfil muy claro `#FAFBF9`, superficies blancas `#FFFFFF`, verde petróleo `#087F6D`, menta `#DDF7EF`, turquesa `#10B8A6`, azul clínico `#2458DE`, azul hielo `#EDF3FF`, texto `#172B3A`, secundarios `#64748B`, coral `#EF6461`, ámbar `#F4A340`. El perfil Dueño tiene acento verde petróleo y el Veterinario azul clínico, pero comparte diseño central. Incluye tema oscuro real, con superficies y contrastes adaptados.

## Fotografías reales y recursos existentes
Crear/reutilizar los siguientes nombres de drawable de Android con archivos realmente añadidos: `pet_dog.jpg`, `pet_cat.jpg`, `pet_rabbit.jpg`, `pet_bird.jpg` en `app/src/main/res/drawable-nodpi/`; utilízalos como foto de ejemplo/fallback según especie, nunca como foto de un usuario si ya tiene imagen propia. Las fuentes CC0 están indicadas por separado; si los archivos aún no están en el proyecto, SOLICÍTALOS o comprueba su presencia antes de referenciarlos. Nunca generes `R.drawable.*` inexistentes. Usa `painterResource` y `ContentScale.Crop`/`Fit` según destino. Imágenes solo en secciones con valor visual (Splash, Inicio, tarjetas de mascotas, ficha, estados vacíos); NO pongas fotografías como sustituto de iconos en formularios clínicos, tablas o navegación. No uses imágenes de URL ni solicites permisos de Internet para avatares locales. Optimiza tamaño y respeta relación de aspecto. Documenta licencia y fuente en README final.

## Sistema de iconos
Usa iconos vectoriales de una única familia coherente (Material Symbols/Material Icons que esté disponible en Gradle; donde no exista el símbolo, crea VectorDrawable o Compose Canvas). Antes de importar nombres de icons verifica que existen; evita referencias no resueltas como `LockOutline`. Guía semántica: huella=mascotas; casa=inicio; calendario=citas/agenda; usuario=perfil; lupa=buscar; jeringa=vacunación; estetoscopio/latido=consulta; tijeras=baño y corte; portapapeles=historial/atención; escudo=seguridad; engranaje=ajustes; campana=recordatorios; reloj=duración; gráfico=estadísticas; check=confirmada; alerta=pendiente; X=cancelada. Tamaños 20–24 dp para acciones, 24 dp barra, 32–48 dp para iconos ilustrativos, área táctil mínimo 48 dp, contentDescription apropiado. No uses emojis como iconos del producto.

## Componentes y responsive
Audita TODOS los Composables y comparte tokens de espaciado (4/8/12/16/20/24), radios (12/16/20/24), elevación suave, tipografía (títulos 22–28sp, cabeceras 18–20sp, cuerpo 14–16sp, secundarios legibles 12–14sp), botones 48–56dp. Scaffold + WindowInsets + imePadding + LazyColumn/scroll según caso. Respeta top/bottom system bars, evita doble padding, exceso de padding, solapamiento del teclado, botones ocultos o enormes huecos. Prueba vistas 360×640dp, 393×852dp y 412×915dp y tamaños de fuente ampliados. Los formularios se desplazan; la barra inferior queda visible cuando corresponda. No fuerces alturas fijas para llenar artificialmente la pantalla. Animaciones discretas (<300 ms) y skeletons/estados vacíos/reintentar donde hagan sentido.

## Revisión exhaustiva por pantalla, sin dejar ninguna fuera
**Comunes:** Splash (hero de mascota real, logo, CTA), Login Dueño/Vet (selector claro, errors, contraseña visible, teclado), cambio de perfil y cierre sesión. **Dueño:** Inicio (saludo, próxima cita y foto de mascota + servicios + recordatorio, buen equilibrio del primer pliegue), Lista Mascotas (avatares reales, tarjetas eficientes), formulario mascota (foto/fallback y validación por campo), ficha de mascota (foto, datos y timeline clínico, reserva visual para gráfico posterior), Pedir Cita (pasos mascota/servicio/fecha/hora, disponibilidad y precio reales), Mis Citas (estados, confirmaciones, Cancelar bajo regla actual), Perfil (avatar de PERSONA y no mascota, estadísticas, preferencias). **Veterinario:** Panel (KPIs, ingresos reales, gráficos correctos), Agenda (fechas con día de semana en letras, timeline claro, estados y acciones), Atender Cita (formulario clínico legible y peso real), Pacientes (tarjetas, especies, búsqueda actual, estado vacío), Ficha de paciente (reutilizar pantalla existente si es compartida), Servicios (ícono, precio, duración, estado), Formulario Servicio (validaciones, íconos, duración), Ajustes (avatar humano, switches, cambio perfil, persistencia). Si descubres otras pantallas reales, también revísalas e inclúyelas en el reporte. No crees pantallas duplicadas.

## Reglas críticas
No cambies Room ni DAO ni flujos ni operaciones de negocio para conseguir efectos visuales. View = solo estado y acciones del ViewModel; ViewModel solo interfaz VetRepository, no implementación concreta; conserva DataStore y NavVet. No uses métricas inventadas. No infieras género por nombre: si no hay campo explícito, usa «Responsable». Fechas en español con nombre del día y zona horaria consistente. Los perfiles no se mezclan accidentalmente.

## Entrega verificable
(1) Audita archivos reales y reporta 3–5 problemas visuales encontrados en cada familia de pantallas. (2) Modifica componentes compartidos y después TODAS las pantallas. (3) Lista recursos existentes/faltantes, dependencias y archivos editados. (4) Ejecuta `./gradlew :app:assembleDebug` si tienes terminal y confirma el resultado real; si falla, corrige. (5) Indica qué pantallas probaste realmente en emulador y cuáles quedan pendientes. NO hagas commit; espera mi confirmación. El commit previsto es `ia(ui): mejora profesionalmente todas las pantallas`.


---
````

**Qué generó la IA:** En el ZIP revisado aparecen `PremiumUI.kt`, las pantallas Compose, avatares de mascotas y personas y el tema Material 3. No está confirmado qué archivos específicos generó Gemini; los recursos gráficos y diversas correcciones fueron elaborados con ayuda de ChatGPT.

**Qué tuve que corregir yo:** El diseño inicial dejaba pantallas monótonas y textos de tamaño reducido; se pidieron varias revisiones de Perfil, Ajustes, historial, peso, barras y confirmaciones. Se preparó un fondo con imágenes para Login.

**¿Respeta MVVM?:** El objetivo es compatible con MVVM al limitarse al diseño. **Condición:** Composables sin consultas a DAO/Repository; en la copia revisada los cálculos de Perfil están en `PerfilViewModel`.



## Prompt 3 — Commit 2 de cinco: Gráfico de peso clínico real (PREPARADO)

**Estado:** prompt completo **preparado durante la conversación**; el envío a Gemini no fue confirmado. El texto siguiente es el que se propuso para copiar y pegar (no se presenta como registro de ejecución en Gemini).

**Texto enviado a Gemini:** **Sin confirmación de envío**. **Texto completo preparado:**

````text
# PROMPT 2 — Gráfico de peso clínico real

Partimos del rediseño profesional del Commit 1 en la rama `mejora-ia-lab8`. Implementa ahora la MEJORA A del Laboratorio 08: gráfico de evolución del peso registrado en cada atención de una mascota en su ficha clínica.

1. Inspecciona las clases reales para localizar de dónde sale el peso por atención, fecha y estado de cita; no supongas columnas. Si falta información histórica, indica el cambio mínimo de esquema y la migración segura; sube la versión de Room solamente si cambió el esquema, sin borrar datos.
2. Agrega en el DAO apropiado una consulta Room que devuelva mediciones de la mascota correspondientes SOLO a citas ATENDIDAS, sin pesos inválidos y en orden cronológico real (fecha, hora y criterio de desempate). Decide DTO/proyección tipada si resulta necesario.
3. Declara la operación primero en la interfaz `VetRepository` y luego impleméntala en `VetRepositoryRoom`, sin pasar consultas a las vistas.
4. Expón en el ViewModel existente de la ficha (p. ej. `MascotaViewModel`, si es la clase real) un StateFlow con Loading/Success/Empty/Error, ligado al `mascotaId` actual; evita colecciones huérfanas y vuelve a actualizarse tras registrar una atención.
5. Dibuja en Jetpack Compose con Canvas o reutiliza `GraficoLineas` del laboratorio: línea con curvas/segmentos limpios, puntos, ejes de kg y fecha corta, rejilla tenue y gradiente discreto; sin bibliotecas de gráficos externas. Si hay un punto, dibuja un punto y su valor; si hay 0, mensaje de estado vacío; contempla pesos repetidos, fechas iguales, escalas y valores extremos. No inventes valores.
6. Inserta el gráfico debajo de los datos de peso y antes o dentro de la pestaña de historial, con una tarjeta moderna y paleta acorde al rol. No reduzcas demasiado el gráfico ni desplaces controles fuera de pantalla. Accesibilidad: texto alternativo descriptivo de tendencia y lista textual de mediciones.
7. Prueba: mascota sin atención, con 1 atención, con 3 y con un nuevo peso agregado por veterinario; verifica que la serie cambie y corresponda a Room. Conserva navegación y temas.
8. Compara el resultado con las pruebas de Fase 1 y ejecuta compilación real. Entrega archivos completos modificados, pruebas y posibles cambios de esquema. No hagas el commit automáticamente.

Mensaje del commit tras verificar: `ia: agrega grafico de peso en la ficha de la mascota`.


---
````

**Qué generó la IA:** En el proyecto revisado están `model/PuntoPeso.kt`, `CitaDao.observarHistorialPeso`, `VetRepository.observarHistorialPeso`, `VetRepositoryRoom`, `MascotaViewModel` y `ui/components/graficos/GraficoLineas.kt`.

**Qué tuve que corregir yo:** Se revisó la escala visual y el espaciado del gráfico para que las fechas no se encimen y se solicitó tratamiento de los casos con 0/1/múltiples mediciones. La compilación final no está confirmada.

**¿Respeta MVVM?:** **Sí en la organización revisada:** DAO → contrato `VetRepository` → implementación → `MascotaViewModel` → `GraficoLineas`/ficha. Falta prueba de ejecución y persistencia.



## Prompt 4 — Commit 3 de cinco: Reprogramación profesional de citas (PREPARADO)

**Estado:** prompt completo **preparado durante la conversación**; el envío a Gemini no fue confirmado. El texto siguiente es el que se propuso para copiar y pegar (no se presenta como registro de ejecución en Gemini).

**Texto enviado a Gemini:** **Sin confirmación de envío**. **Texto completo preparado:**

````text
# PROMPT 3 — Reprogramación profesional de citas

Partimos de los commits 1 y 2 de HuellitasVet, rama `mejora-ia-lab8`. Implementa la MEJORA B: el dueño puede reprogramar fecha y hora de una cita PENDIENTE o CONFIRMADA sin perder la información original.

1. Revisa esquema y negocio reales. Añade botón «Reprogramar» en Mis Citas únicamente para estados permitidos. No habilitar CANCELADA ni ATENDIDA. Mantén la regla de más de 2 horas: valida usando la fecha/hora de la cita original y reloj actual del dispositivo/zona configurada; define qué ocurre exactamente al llegar a 2 horas (bloquear).
2. Diseña nueva pantalla o flujo existente en Compose con resumen de mascota, servicio y reserva actual, selección de nuevo día en letras y horarios; disponibles destacados y ocupados/pasados inhabilitados; muestra mensajes claros. Conservar colores premium y barra del perfil Dueño.
3. Consultas de disponibilidad por duración del servicio y calendario real. Excluye del chequeo de solapamiento el ID de la cita a mover; contempla citas solapadas, no solo coincidencia exacta de la hora de inicio. Respeta el horario y la granularidad que use el proyecto; no inventes reglas de agenda.
4. Agrega interfaz en `VetRepository` antes de implementación. DAO maneja consultas/escrituras y `VetRepositoryRoom` revalida en el momento de guardar dentro de una transacción Room (`withTransaction` o equivalente existente) la regla de 2 horas, estado, disponibilidad y actualización del MISMO ID. No se crea una segunda cita. Si el esquema necesita cambios, define migración y aumenta versión; evita destructivas.
5. Usa ViewModel con StateFlow para carga de disponibilidad, fecha y hora elegidas, confirmación, loading y errores. View sin DAO ni Repository directo. Evita doble envío con botón deshabilitado mientras guarda. Explica conflictos de escritura concurrentes; utiliza restricciones/serialización apropiadas al almacenamiento si es necesario.
6. Pruebas: cita pendiente a >2 h, confirmada a >2 h, <2 h, exactamente 2 h, horario ocupado, horario libre, misma cita, cita atendida/cancelada y reinicio. Verifica que Mis Citas y Agenda del Vet muestren el mismo cambio y que no se altere el precio del servicio. Asegura que cancelar y atender sigan funcionando.
7. Compila, describe archivos reales tocados y no hagas commit automático.

Mensaje del commit tras verificar: `ia: agrega reprogramacion de citas`.


---
````

**Qué generó la IA:** Existe `ReprogramarCitaScreen.kt`, `ReprogramarCitaViewModel.kt`, el método `VetRepository.reprogramarCita` y su implementación en `VetRepositoryRoom`, con validaciones de horario y estado.

**Qué tuve que corregir yo:** Se reportó `No parameter with name 'onReprogramar' found` en `NavVet.kt`; el parámetro se había pasado a `InicioScreen` por error. Se preparó una corrección para mantenerlo solamente donde correspondía. La integración se realizó con ayuda de ChatGPT, no se atribuye a Gemini.

**¿Respeta MVVM?:** **Diseñado para respetar MVVM:** pantalla → `ReprogramarCitaViewModel` → interfaz `VetRepository` → Room. Se debe confirmar que no queden validaciones esenciales solo en Compose.



## Prompt 5 — Commit 4 de cinco: Búsqueda combinada y auditoría visual final de TODA la app (PREPARADO)

**Estado:** prompt completo **preparado durante la conversación**; el envío a Gemini no fue confirmado. El texto siguiente es el que se propuso para copiar y pegar (no se presenta como registro de ejecución en Gemini).

**Texto enviado a Gemini:** **Sin confirmación de envío**. **Texto completo preparado:**

````text
# PROMPT 4 — Búsqueda combinada y auditoría visual final de TODA la app

Estamos en el Commit 4 de 5 de la rama `mejora-ia-lab8`. Ya debería existir un diseño premium y las mejoras de peso y reprogramación. Implementa la MEJORA C y realiza el pulido visual exhaustivo para que ninguna pantalla se vea descuidada.

## Búsqueda de pacientes
- Inspecciona `PacientesScreen`, ViewModel y modelos reales. No crees un segundo buscador ni dupliques pantalla.
- Buscar simultáneamente por nombre de mascota o nombre de dueño/responsable, con coincidencia parcial, espacios extra ignorados y sensibilidad reducida a mayúsculas/tildes cuando sea posible.
- Filtros de especie: Todos, Perros, Gatos y Otros. Si hay texto y filtro al mismo tiempo, se deben aplicar ambos. Texto vacío implica todas las coincidencias del filtro.
- Mantén los datos de Room como Flow; usa `combine`, `stateIn` y `StateFlow` en ViewModel. Ninguna consulta Room en Compose. No cambies las capas si el filtro puede resolverse en el ViewModel; si lo requiere, agrega interfaz Repository antes de la implementación.
- Tarjetas con foto real local fallback por especie, nombre, raza, responsable y última visita reales; íconos consistentes de búsqueda, filtros y ficha. Estado vacío profesional con botón limpiar filtros. No inventes fechas ni dueños. Mantén estado de búsqueda al entrar/salir de la ficha según alcance del ViewModel.
- Comprueba combinado: «Luna» + Gatos, búsqueda por nombre de responsable + Perros, cadena sin coincidencias, cambios de Room, usuario con varios animales.

## Revisión total de pantallas — NO OMITIR
Haz inventario de TODOS los destinos de NavVet y sus pantallas Composable reales. Revisa con especial cuidado: Splash, Login de ambos roles, Inicio del Dueño, Mascotas, Registrar/Editar, Ficha clínica e historial y gráfico, Pedir cita, Mis citas y Reprogramar, Perfil, Panel Veterinario, Agenda, Atender cita, Pacientes, Ficha del paciente, Servicios, Nuevo/Editar servicio y Ajustes. También componentes, diálogos, estados vacíos y navegación inferior.

Rechaza pantallas que todavía tengan: avatar de mascota en perfil humano; iconos repetidos, incorrectos o desalineados; fotos borrosas; titulares demasiado pequeños; botones solapados; espacios vacíos gigantes; color azul o verde aplicado a todas las superficies; tarjetas sin alineación; barras de navegación diminutas; fechas numéricas difíciles de leer; contenido fuera de Safe Area; formulario oculto por teclado; textos con bajo contraste; pérdida de modo oscuro.

Usa fotografía real solo donde agrega valor, respetando mascota y privacidad; para acciones usa iconos vectoriales, no emojis. Mantén identidad elegante compartida, adaptada a los roles. Verifica fotografías en drawable-nodpi y que exista todo recurso referenciado. Estilos de inputs, tipografías y paddings coherentes. Revisa dispositivos 360×640dp y 412×915dp, modo oscuro, texto ampliado y teclado abierto. Arregla las pantallas deficientes, sin rediseñar partes correctas porque sí.

## Arquitectura y entregables
Respetar View→ViewModel→VetRepository(interface)→VetRepositoryRoom→DAO→Room, DataStore y migraciones. Ejecuta compilación, tests que existan y las pruebas de aceptación que puedas verificar; comunica sin falsedad cuáles no probaste. Presenta una tabla: pantalla, defecto detectado, corrección y verificación; enumera archivos tocados. No hagas el commit automáticamente.

Mensaje del commit tras verificar: `ia: agrega busqueda de pacientes`.


---
````

**Qué generó la IA:** En la copia revisada existen `PacientesViewModel.kt` con `combine` y la pantalla `PacientesScreen.kt`, además de una revisión visual de formularios, perfiles y componentes. No está probado que Gemini haya generado esos archivos.

**Qué tuve que corregir yo:** Se revisaron ajustes de iconos humanos, tarjetas pequeñas, diagnóstico/tratamiento poco legibles y confirmaciones estáticas; se introdujo un componente de check animado en `PremiumUI.kt` con su correspondiente integración.

**¿Respeta MVVM?:** **Sí en la estructura que se observa:** la búsqueda y los filtros están en `PacientesViewModel`, no dentro de un DAO accedido desde Compose. La validación real se hace al compilar y usar el filtro combinado.



## Prompt 6 — Commit 5 de cinco: README + PROMPTS.md completos y verídicos (PREPARADO)

**Estado:** prompt completo **preparado durante la conversación**; el envío a Gemini no fue confirmado. El texto siguiente es el que se propuso para copiar y pegar (no se presenta como registro de ejecución en Gemini).

**Texto enviado a Gemini:** **Sin confirmación de envío**. **Texto completo preparado:**

````text
# PROMPT 5 — README + PROMPTS.md completos y verídicos

Estamos al final de la Fase 2 Mejora IA Lab8, rama `mejora-ia-lab8`, tras los cuatro commits de código. Quiero **UN SOLO PROMPT final** que genere los DOS documentos de entrega: actualizar `README.md` y crear `PROMPTS.md`. No modifiques Kotlin, Room ni recursos visuales; no hagas el commit automáticamente.

## README.md — Actualizar, no borrar la Fase 1
Conserva el nombre HuellitasVet, descripción, requisitos, instalación y ejecución, perfiles y cuentas de laboratorio sin publicar credenciales sensibles reales, árbol ACTUAL de paquetes, diagrama MVVM + Repository, cómo usa Room y DataStore, explicación de reglas de negocio, pruebas de aceptación y seis capturas reales 3 del Dueño + 3 del Veterinario. Corrige rutas de imágenes si cambiaron. Preserva respuestas y apartados «Para pensar» que ya existan; no inventes preguntas ausentes.

Agrega sección «Fase 2 — Mejora IA Lab8» con objetivos, nueva dirección visual, fotografías locales y fuentes/licencia, tabla por pantalla de mejoras reales, componentes compartidos, cambios en temas y modo oscuro, gráfico de peso con consultas reales, reprogramación con regla de 2 horas, búsqueda simultánea de mascota/dueño/especie, esquema/migraciones SOLO si se hicieron, lista de pruebas realmente ejecutadas y resultado real, capturas de antes y después SOLO si están disponibles, diagramas y árbol actualizados, dependencias finales y los CINCO mensajes de commit de la fase.

## PROMPTS.md — Registro fiel de todo lo enviado a Gemini
Incluye título «Prompts utilizados — Mejora IA Lab8». Para cada uno de los 5 prompts principales y cada prompt adicional real enviado (por ejemplo, 17 prompts por pantalla del primer commit, el prompt global previo, correcciones de diseño o de compilación) registra obligatoriamente:
1. **Texto enviado a Gemini:** reproducir el texto que realmente se envió, sin reformularlo. Si no tienes historial, poner «[PENDIENTE: pegar texto original]»; nunca inventar.
2. **Qué generó la IA:** archivos, componentes y funciones que se verifican en Git.
3. **Qué tuve que corregir yo:** errores o ajustes reales, con evidencia de código/historial; si ninguno, indicar ninguno verificado o pendiente de validación.
4. **¿Respeta MVVM?:** Sí/No, con explicación concreta (View no toca DAO/Repository, ViewModel solo interfaz VetRepository, RepositoryRoom maneja DAO, versionado Room cuando corresponda).

Incluye una **reflexión de cinco líneas** respondiendo: qué mejoró la IA y qué no; qué error cometió; qué aprendí de MVVM + Room y las reglas de negocio; cómo mejoró UX/UI; por qué siempre hay que revisar el código generado. No inventes experiencias.

## Comprobación y salida
Lee árbol real y `git log`, revisa navegación, fuentes de imágenes y archivos existentes. Verifica links Markdown locales, que existan las seis capturas originales y no haya información falsa ni rutas rotas. Lista documentación y referencias que falten para que yo las proporcione. Actualiza únicamente `README.md` y `PROMPTS.md` (más capturas solo si ya proporcionadas). No afirmes pruebas que no se ejecutaron.

Mensaje de commit final tras mi revisión: `docs: agrega PROMPTS.md`.


---
````

**Qué generó la IA:** Se prepararon `README.md` y `PROMPTS.md`, con árbol de paquetes, arquitectura, etapas, imágenes, prompts completos y reflexión de cinco líneas. Estos documentos se han creado mediante ChatGPT; no se afirma que Gemini los haya generado.

**Qué tuve que corregir yo:** Se corrigió la documentación anterior, que resumía prompts sin texto completo, no distinguía la autoría y todavía mencionaba once imágenes cuando el ZIP actual contiene catorce.

**¿Respeta MVVM?:** **No modifica las capas de la app**, porque se trata de archivos Markdown. El README explica las responsabilidades de UI, ViewModel, Repository y DAO. Es necesario verificar los enlaces a capturas y el historial real de Git.



## Prompt adicional — Revisión UI/UX premium de todos los perfiles y del historial (PREPARADO)

**Texto enviado a Gemini:** **No se confirmó que se enviara este texto**. Se conserva el texto completo que se preparó para reproducir las mejoras de la última revisión:

````text
# Prompt de revisión UI/UX profesional — HuellitasVet

**Estado:** prompt preparado con ayuda de ChatGPT para que puedas copiarlo a Gemini en Android Studio. No se afirma que ya lo hayas enviado.

Actúa como **Lead Android Engineer, Product Designer y UX/UI Senior**. Trabaja sobre el proyecto Kotlin/Jetpack Compose HuellitasVet que ya está abierto. La prioridad no es generar nuevas maquetas: **implementa un producto real**, conservando Room 2.6.1, DataStore, Navigation Compose, el patrón `View → ViewModel → VetRepository(interface) → VetRepositoryRoom → DAO` y las reglas de negocio existentes.

## Identidad visual

1. No copies los mockups académicos. Diseña una identidad veterinaria contemporánea, cálida para Dueño y clínica para Veterinario: verde petróleo `#087F6D`, turquesa `#10B8A6`, azul clínico `#2458DE`, fondo cálido `#FAFBF9`, texto `#172B3A`, ámbar `#F4A340` y rojo `#EF6461`. El color debe ser estratégico: evita pantallas completamente verdes o azules.
2. Consolida colores, tipografía, botones, espaciado y componentes reutilizables en Material 3, respetando dark mode.
3. Tipografía legible y jerarquizada: encabezados de 21–26sp, cuerpo 15–17sp, secundarios 13–14sp, interlineado generoso. No comprimas diagnósticos ni tratamientos en dos líneas.
4. Tarjetas suaves de 18–26dp, iconos vectoriales consistentes, áreas táctiles de al menos 48dp, separación vertical regular y estados de error/success/empty/loading diseñados. Evita emojis.
5. Revisa `Scaffold`, `WindowInsets`, `LazyColumn`, `imePadding`, `fillMaxSize`, paddings del sistema y modo horizontal; el contenido puede desplazarse, pero ningún control queda recortado u oculto por el teclado.

## Fotografías y perfiles

- Reutiliza los recursos locales en `res/drawable-nodpi`: imágenes de mascotas, `splash_fondo`, `inicio_banner` y `veterinario_banner` sin solicitar Internet.
- Usa `avatar_duena.webp`, `avatar_dueno.webp` y `avatar_veterinario.webp` únicamente como retratos *ilustrativos de las cuentas de práctica*. En cuentas desconocidas utiliza un icono vectorial de persona, no una fotografía arbitraria ni una mascota.
- En los perfiles humanos utiliza iconos de persona, preferencias, calendario, cambio de rol y cerrar sesión; reserva `Pets` para mascotas y especies.
- Mantén los nombres y correos desde la sesión real: ningún nombre ni estadística codificados de forma estática.

## Revisión obligatoria pantalla por pantalla

**Dueño**: Splash con fotografía y CTA; Login con foto y formulario sobre superficie contrastada, selector de rol y teclado gestionado; Inicio con banner y próxima cita; Mis mascotas con avatares, raza, peso y edades legibles; Registrar/Editar mascota con validaciones por campo y respuesta visual; Ficha con hero fotográfico, datos, historial y gráfico de peso; Pedir cita con estados de horario y resumen; Mis citas con estados, precio y botones separados del contenido; Reprogramar cita con controles claros y reglas; Perfil con retrato humano, estadísticas de 3 colores, preferencia y salida.

**Veterinario**: Panel con métricas y gráficos reales; Agenda de día con fechas en letras y citas legibles; Atender cita con registro clínico y peso anterior/actual; Pacientes con búsqueda combinada y tarjetas; Ficha de paciente con historia, diagnóstico, tratamiento y peso completos; Servicios con iconos/fotos, precio y controles; Nuevo/Editar servicio con validaciones e iconos; Ajustes con retrato humano, preferencias y acceso por rol.

## Historial clínico y peso

- Cada cita ATENDIDA tiene tarjeta de al menos 16sp para título, fecha entendible, diagnóstico y tratamiento en líneas completas, y chip de peso cuando exista. Timeline continuo y tarjetas sin solapamientos.
- Gráfico Canvas reactivo desde Room con estados de 0, 1 y muchos puntos; mostrar el último peso en kg y la variación cronológica real. Evitar etiquetas superpuestas, uso de números inventados y divisiones por cero.
- La misma ficha en Dueño y Veterinario debe expresar la misma información clínica, con acceso según rol.

## Confirmaciones y microinteracciones

- Al confirmar realmente el guardado de una cita, mostrar un `Dialog` con check animado usando `animateFloatAsState` y `AnimatedVisibility`, detalles de mascota/servicio/fecha y acción **Ver mis citas**. No mostrar éxito antes de la escritura.
- Reutilizar esta animación para mascota registrada/actualizada, cita reprogramada y atención registrada, diferenciando cancelación.
- Al producirse un error mostrar mensaje claro y recuperable, sin navegar prematuramente. Evitar Lottie y bibliotecas innecesarias.
- Aplicar animaciones discretas también a selección de chips, cambios de tab y carga; evitar animaciones permanentes.

## Invariantes funcionales

- Login con clave inválida nunca navega. Validación de mascota y bloqueo de eliminación con citas.
- Citas pendientes/confirmadas/atendidas/canceladas con precios históricos; horarios ocupados deshabilitados; cancelar y reprogramar solamente con más de dos horas y disponibilidad de horario real; ingresos mensuales solo desde citas atendidas.
- Registro de atención y actualización de peso en transacción Room. Servicio con citas no se elimina. DataStore conserva sesión y modo oscuro.
- No alterar entidades ni esquema de Room por ajustes de UI. Si una característica funcional lo requiriese, declara migración antes de editar la versión de base de datos.

## Forma de trabajar y entrega

1. Inspecciona antes de editar; reutiliza paquetes y firmas reales.
2. Presenta lista de archivos reales y aplica cambios directamente, nunca demos que no compilan.
3. Ejecuta Gradle `:app:assembleDebug`, repara errores Kotlin y, en emulador, revisa todas las pantallas en modo claro/oscuro, con datos y sin datos, en 360×640dp, 393×852dp y 412×915dp.
4. Documenta archivos cambiados y resultados reales en `PROMPTS.md`, sin inventar tests, aportes de herramientas ni pantallas verificadas.
5. No hagas Git commit ni push automáticamente. Conserva rama de Mejora IA Lab8 y deja `main` intacta.

**Resultado esperado:** producto visualmente coherente, dinámico, con iconos profesionales y fotos locales; perfiles humanos, historias legibles, tarjetas de peso, registro con check animado y navegación completamente funcional.
````

**Qué generó la IA:** En la versión fuente revisada aparecen `ui/components/PremiumUI.kt`, `ui/components/HistorialItem.kt`, `ui/dueno/PerfilScreen.kt`, `ui/veterinario/AjustesScreen.kt` y `viewmodel/PerfilViewModel.kt`, junto con las imágenes locales ilustrativas. El paquete revisado fue preparado con asistencia de ChatGPT; su autoría no se atribuye automáticamente a Gemini.

**Qué tuve que corregir yo:** Se solicitó ampliar el historial clínico, mostrar diagnóstico y tratamiento con letra legible, mejorar perfiles humanos y mover las estadísticas de Perfil fuera de Compose. Las confirmaciones de registro se cambiaron por un diálogo visual con check animado.

**¿Respeta MVVM?:** **La separación ha mejorado**: los contadores están en `PerfilViewModel` y la UI muestra estados. Debe comprobarse la integración final en Android Studio y que las reglas de negocio sigan en Repository y no solo en UI.



## Prompt adicional — Auditoría técnica final de la aplicación (PREPARADO)

**Texto enviado a Gemini:** **No se confirmó su envío**. Es el prompt final de auditoría que se dejó preparado para garantizar que el proyecto funcionara antes de entregarlo:

````text
# Prompt de auditoría final — HuellitasVet

Actúa como un desarrollador Android Senior y diseñador UX/UI de aplicaciones veterinarias premium. Tengo abierto el proyecto **HuellitasVet**, basado en Kotlin, Jetpack Compose, Material 3, Room, DataStore y la interfaz `VetRepository`. Ya integra 11 recursos locales WebP, Splash y Login fotográficos, banners en Inicio y Panel, avatares fotorrealistas para perros/gatos/otras especies, gráfico de peso clínico, reprogramación de citas y búsqueda avanzada de pacientes.

## OBJETIVO

Revisa y termina profesionalmente el **proyecto actual**, sin crear otro ni eliminar funciones. Quiero una app atractiva, moderna, rápida y coherente para Dueño y Veterinario. Corrige las pantallas que se vean pequeñas, con espacios vacíos grandes, imágenes mal recortadas, texto poco legible, botones desalineados o contrastes deficientes. Utiliza fotografía local de forma estratégica y Material Icons uniformes.

## AUDITORÍA OBLIGATORIA DE CADA PANTALLA

Dueño: Splash, Login, Inicio, Mascotas, Registrar/Editar mascota, Ficha e historial, Pedir cita, Mis citas, Reprogramar, Perfil.
Veterinario: Login, Panel y gráficos, Agenda, Atender cita, Pacientes, Ficha paciente, Servicios, Nuevo/Editar servicio, Ajustes.

Comprueba todas las pantallas listadas en `NavVet.kt`. No supongas que las capturas del PDF son la UI definitiva: conserva funciones y supera su calidad visual.

## ESTILO

Usa verde petróleo #087F6D y turquesa #10B8A6 en Dueño; azul #2458DE y azul hielo #EDF3FF en Veterinario; superficies blanco cálido #FAFBF9, textos #172B3A, coral #EF6461 y ámbar #F4A340 para estados. No tiñas todos los elementos del mismo color: espacio, tipografía, fotografía y jerarquía visual deben dar sensación premium. Crea componentes reutilizables, controles táctiles >=48dp, modo oscuro completo y estados vacíos bien resueltos. Respeta sistemas de navegación/estado existentes.

## IMÁGENES REALES DEL PROYECTO (LOCALMENTE)

`app/src/main/res/drawable-nodpi/`: splash_fondo, inicio_banner, veterinario_banner, mascota_perro, mascota_gato, mascota_conejo, mascota_ave, servicio_consulta, servicio_vacunacion, servicio_bano, servicio_alimentacion (extensión `.webp`). **Ya existen**, no inventes nuevos IDs. Usa `Image(painterResource(R.drawable.nombre), contentScale=ContentScale.Crop)` con recortes proporcionados. No cargues URLs externas ni conviertas la fotografía en texto o botones: acciones siguen siendo Composables. No atribuyas fotografías de ejemplo a mascotas si existe imagen personal del usuario.

Prioridad: **Login** debe conservar una hero photo visible al entrar, logotipo con huella, selector de rol, formulario contrastado, contraste en modo oscuro, keyboard insets, correo y contraseña reales; Splash debe quedar lleno y elegante sin botonería rasterizada. Inicio y Panel deben aprovechar el ancho y colocar imágenes sin ocultar contenido. Las listas clínicas deben mantener su velocidad y datos reales.

## NEGOCIO Y MVVM INVIOLABLE

Compose UI -> ViewModel -> VetRepository (interface) -> VetRepositoryRoom -> DAO -> Room. La View nunca invoca DAOs ni repositorios directamente. Conserva `Flow/StateFlow` y preferencias de DataStore. Reprogramar solo citas PENDIENTE/CONFIRMADA con >2h de plazo, verificar solapamientos de duración, preservar ID y actualizar en transacción. Peso: graficar únicamente mediciones reales de citas ATENDIDAS. Pacientes: búsqueda simultánea mascota/dueño y especie. No inventar datos de ingresos ni historial; no borrar esquema Room ni usar destructiveMigration para «arreglar» datos.

## QA Y RESULTADO

Inspecciona archivos reales y corrige errores de compilación; especialmente rutas de `NavVet.kt`, importaciones y firmas de callbacks. Ejecuta `:app:assembleDebug` y en emulador recorre todas las pantallas, probando 360×640, 393×852 y 412×915 dp. El teclado no debe cubrir formularios. Documenta qué pruebas pudiste realizar y qué no.

Entrega un resumen concreto de archivos cambiados, pantallas mejoradas, recursos usados, compilación y errores pendientes. **No hagas commits automáticamente**; no sobrescribas documentación con información inventada. Sigue los requisitos de TECSUP y documenta los prompts realmente enviados en `PROMPTS.md`.
````

**Qué generó la IA:** La auditoría fue solicitada como actividad pendiente; no hay evidencia de una nueva ejecución de Gemini ni de un APK compilado como resultado de este texto.

**Qué tuve que corregir yo:** Se identificó la necesidad de comprobar definitivamente el Login, las rutas de reprogramación, el gráfico clínico, los avatares humanos y las confirmaciones animadas. Ninguna prueba se marca como aprobada sin ejecutar el emulador.

**¿Respeta MVVM?:** **Sí en sus instrucciones**, porque exige no conectar Compose directamente con DAOs ni saltarse la interfaz `VetRepository`. El resultado efectivo requeriría auditar los cambios que aplique el agente.


## Correcciones conocidas y trazabilidad de herramientas

| Situación detectada | Evidencia en la conversación | Acción / responsabilidad |
|---|---|---|
| Referencia `verticalScroll` sin resolver en `PacientesScreen.kt` | Error `Unresolved reference 'verticalScroll'` | Se indicó agregar import de Compose para scroll y `rememberScrollState`; confirmar compilación posterior |
| Navegación de reprogramación | Error `No parameter with name 'onReprogramar' found` en `NavVet.kt` | El argumento se colocó en `InicioScreen` por error; se proporcionó la corrección. Asistencia de **ChatGPT**, no comprobada como generación de Gemini |
| Texto multilinea en Inicio | Errores `Expecting '"'`, `Expecting ','` y `Expecting an element` | Se escapó el salto de línea en la cadena de Kotlin y se compartió un parche |
| Diseño demasiado simple | Comentarios del usuario sobre perfiles, historial, peso y pantalla blanca de Login | Revisión de iconos, tamaños, fotos y diseño responsivo; ver código y pruebas finales |
| `PerfilScreen` consultaba Repository directamente | Revisión técnica del proyecto | Se trasladaron los contadores a `PerfilViewModel`, conservando el contrato Repository |

## Reflexión (5 líneas)

1. **¿Qué mejoró la IA y qué no?** La IA me ayudó a proponer un diseño más atractivo, con imágenes de mascotas, iconos y nuevas pantallas, pero algunas secciones seguían viéndose simples y tuve que pedir más ajustes.
2. **¿Qué error cometió?** En el código asistido aparecieron errores como el parámetro `onReprogramar` en una pantalla equivocada y una cadena mal escrita en `InicioScreen`; aprendí a no confiar en el código sin compilarlo.
3. **¿Qué aprendí sobre MVVM?** Entendí que la pantalla debe mostrar información y enviar eventos, mientras el ViewModel prepara los estados y usa la interfaz del Repository.
4. **¿Qué aprendí sobre Room?** Comprendí que los historiales de peso y las citas deben salir de registros reales y actualizarse de forma consistente, sin perder los datos al cerrar la aplicación.
5. **¿Qué aprendí sobre reglas de negocio?** Aprendí que restricciones como las dos horas para cancelar o reprogramar y los horarios ocupados se tienen que validar en la capa de datos, no solamente ocultando botones.

## Comprobaciones antes de la entrega

- [ ] Confirmar cuáles de los textos preparados se enviaron realmente a Gemini y sustituir «No confirmado» cuando haya evidencia.
- [ ] Agregar prompts adicionales enviados realmente, conservando su texto exacto.
- [ ] Registrar en **Qué generó la IA** únicamente los archivos que Gemini haya modificado; diferenciar los aportes de ChatGPT.
- [ ] Ejecutar `./gradlew.bat :app:assembleDebug` y las pruebas en el emulador.
- [ ] Comprobar en `git log --oneline -5` que los cinco commits existan y tengan los mensajes previstos.
- [ ] Verificar las seis capturas: tres del perfil Dueño y tres del Veterinario.
