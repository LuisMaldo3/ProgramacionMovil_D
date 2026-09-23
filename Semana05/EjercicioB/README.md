

## Requerimientos funcionales

### RF-01 — Consultar clases disponibles

Mostrar las clases mediante una LazyColumn, con nombre, horario y sala.

Incluir los filtros Hoy y Esta semana en una LazyRow.
Hoy presenta las clases con horarios para ese día, mientras que
Esta semana muestra todas las clases de demostración.

### RF-02 — Consultar el detalle y seleccionar un horario

Abrir el detalle de la clase seleccionada mediante su identificador
como parámetro de navegación.

Mostrar nombre, descripción, duración, sala y cupos disponibles.
Al pulsar Reservar cupo, permitir elegir un único horario mediante
un diálogo. Los horarios agotados no pueden seleccionarse.

### RF-03 — Registrar y consultar reservas

Registrar la clase y el horario elegidos y mostrar una confirmación
con sus datos y el botón Ver mis reservas.

Actualizar los cupos disponibles y evitar duplicar una reserva
confirmada de la misma clase y horario.

Presentar las reservas en una LazyColumn, diferenciando visualmente
los estados Confirmada y Completada.

### RF-04 — Navegar entre secciones y consultar el perfil

Incluir una barra inferior dentro del Scaffold con cuatro pestañas:
Inicio, Reservas, Rutinas y Perfil.

Resaltar la pestaña correspondiente a la pantalla actual.

Mostrar las tarjetas de rutinas y un perfil con iniciales, nombre,
plan, clases tomadas y racha de asistencia.
Las estadísticas del perfil utilizan datos de demostración.