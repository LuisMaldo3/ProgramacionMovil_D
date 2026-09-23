
## Requerimientos de la mejora

### RF01 — Cancelar una reserva

Permitir cancelar una reserva con estado Confirmada desde la pantalla
Mis reservas. Las reservas completadas o canceladas no muestran esta acción.

### RF02 — Confirmar la cancelación

Mostrar un diálogo con el nombre de la clase, día, horario y sala.
La reserva solo cambia cuando el usuario pulsa Sí, cancelar.
Mantener reserva o cerrar el diálogo conserva el estado anterior.

### RF03 — Actualizar el estado y los cupos

Conservar la reserva cancelada en el listado con una etiqueta diferenciada.
Actualizar la lista mediante el estado compartido de FitApp.
Contar únicamente las reservas confirmadas al calcular los cupos ocupados.

### RF04 — Filtrar las reservas

Mostrar filtros para Todas, Confirmadas, Completadas y Canceladas.
Mantener un único filtro seleccionado y actualizar el listado cuando
cambie el estado de una reserva.

## Prompt consolidado

El siguiente texto resume la solicitud realizada durante la conversación;
no es una transcripción literal de un único mensaje.

> Mejora mi aplicación TECSUP Fit desarrollada con Kotlin y Jetpack Compose.
> Conserva el diseño verde, el fondo blanco, las tarjetas y la navegación
> inferior con Inicio, Reservas, Rutinas y Perfil.
>
> Agrega la cancelación de reservas confirmadas mediante un AlertDialog.
> Muestra la clase, el día, el horario y la sala antes de confirmar.
> Incluye las opciones Sí, cancelar y Mantener reserva.
> Cerrar el diálogo no debe modificar la reserva.
>
> Conserva los registros cancelados con un estado visual diferente.
> Actualiza el estado compartido en FitApp y libera el cupo al cancelar.
> Evita duplicar reservas confirmadas de la misma clase y horario.
>
> Agrega una LazyRow con filtros para Todas, Confirmadas, Completadas y
> Canceladas. El listado debe actualizarse después de una cancelación.
>
> Usa remember, mutableStateOf y mutableStateListOf, sin ViewModel ni MVVM.
> Entrega los archivos completos, con comentarios en la lógica importante
> y divide los cambios en tres commits: cancelación, filtros y documentación.
