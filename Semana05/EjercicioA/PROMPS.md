# Requerimientos y prompt de la mejora

## Requerimientos funcionales

1. **Cancelar citas con confirmación:** permitir cancelar reservas confirmadas mediante un diálogo que muestre médico, fecha y hora. Al aceptar, conservar el registro con estado Cancelada. Cerrar el diálogo o elegir Mantener cita no modifica la reserva.

2. **Filtrar citas por estado:** mostrar las categorías Todas, Confirmadas, Completadas y Canceladas, con sus contadores actualizados. Presentar un mensaje cuando una categoría no tenga resultados.

3. **Recuperar citas canceladas:** incorporar Deshacer cancelación para restaurar el estado Confirmada, conservando el identificador y los datos de la reserva, sin duplicarla.

4. **Proteger el historial médico:** mostrar únicamente las atenciones completadas, sin acciones de cancelación o recuperación.

## Prompt para generar los cambios

Actualiza mi aplicación Android Clínica Salud+, desarrollada con Kotlin,
Jetpack Compose y Navigation Compose, para incorporar una mejora funcional
en la rama mejora-ia.

Revisa los archivos ClinicaApp.kt, CitasScreen.kt, DatosClinica.kt,
AgendarScreen.kt y ConfirmarScreen.kt que te proporcionaré. Respeta sus
funciones, modelos y parámetros existentes.

Implementa estas funciones:

- Agrega Cancelar cita únicamente a las reservas confirmadas.
- Antes de cancelar, muestra un AlertDialog con médico, fecha y hora,
  y las opciones Sí, cancelar y Mantener cita.
- Cambia el estado a Cancelada solo al aceptar. Conserva el registro
  y diferencia su estado mediante una etiqueta roja.
- Incorpora filtros Todas, Confirmadas, Completadas y Canceladas
  mediante una LazyRow, con contadores calculados desde la lista actual.
- Muestra un mensaje cuando el filtro seleccionado no tenga resultados.
- Agrega Deshacer cancelación a las reservas canceladas. Restaura
  el estado Confirmada sobre el mismo registro, sin duplicarlo.
- Mantén el historial limitado a citas completadas, sin filtros
  ni acciones para modificar sus registros.

Conserva la lista compartida en ClinicaApp con remember y
mutableStateListOf. Utiliza mutableStateOf para el filtro y el
identificador de la cita pendiente de confirmación.

Conecta CitasScreen mediante alCambiarEstado. Actualiza cada cita
con copy y reemplaza el elemento de la lista observable. Valida
que solo se permitan cambios entre Confirmada y Cancelada.

Incluye el parámetro esHistorial en CitasScreen y conserva las
firmas actuales de AgendarScreen y ConfirmacionScreen.

Mantén el diseño morado, las tarjetas redondeadas, el menú lateral,
el flujo de navegación y el padding del Scaffold.
No utilices ViewModel ni MVVM.

Entrega los archivos modificados completos, con comentarios claros
en los bloques importantes. Organiza la entrega en tres commits:
cancelación con confirmación; filtros y recuperación; documentación.
Incluye los comandos de git add, git commit y git push de cada avance.