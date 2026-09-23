package com.maldonado.ejerciciob

import androidx.compose.ui.graphics.Color

// Compartimos los colores para mantener el mismo diseño en todas las pantallas.
val VerdeFit = Color(0xFF10745E)
val VerdeClaroFit = Color(0xFFE0F5ED)
val FondoTarjetaFit = Color(0xFFF0F0F0)
val TextoFit = Color(0xFF292929)
val TextoSecundarioFit = Color(0xFF817C86)

// Cada horario pertenece a una clase y tiene su propia disponibilidad.
// El identificador permitirá reconocerlo al registrar una reserva.
data class HorarioClase(
    val id: Int,
    val dia: String,
    val hora: String,
    val sala: String,
    val capacidad: Int,
    val cuposDisponibles: Int
)

// Reunimos la información que aparece en la tarjeta y en el detalle.
data class ClaseGimnasio(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val duracionMinutos: Int,
    val horarios: List<HorarioClase>
)

// Una reserva conserva la clase y el horario que eligió el usuario.
// Las reservas nuevas comienzan confirmadas.
data class Reserva(
    val id: Int,
    val clase: ClaseGimnasio,
    val horario: HorarioClase,
    val estado: String = "Confirmada"
)

// Estos datos se mostrarán en la pestaña Perfil.
// Las estadísticas iniciales son valores de demostración.
data class UsuarioFit(
    val nombre: String,
    val iniciales: String,
    val plan: String,
    val clasesTomadas: Int,
    val rachaAsistencia: Int
)

val usuarioEjemplo = UsuarioFit(
    nombre = "Diego Ramos",
    iniciales = "DR",
    plan = "Plan Premium",
    clasesTomadas = 14,
    rachaAsistencia = 3
)

// Utilizamos horarios de demostración para trabajar sin conexión.
// Hoy y Esta semana permitirán filtrar las clases en la pantalla de inicio.
val clasesEjemplo = listOf(
    ClaseGimnasio(
        id = 1,
        nombre = "Yoga funcional",
        descripcion = "Mejora tu movilidad, equilibrio y respiración " +
                "mediante ejercicios de estiramiento y control corporal.",
        duracionMinutos = 50,
        horarios = listOf(
            HorarioClase(
                id = 101,
                dia = "Hoy",
                hora = "7:00 am",
                sala = "Sala 2",
                capacidad = 15,
                cuposDisponibles = 10
            ),
            HorarioClase(
                id = 102,
                dia = "Mañana",
                hora = "8:00 am",
                sala = "Sala 2",
                capacidad = 15,
                cuposDisponibles = 6
            )
        )
    ),
    ClaseGimnasio(
        id = 2,
        nombre = "Cross Training",
        descripcion = "Entrenamiento funcional de alta intensidad. " +
                "Cupos limitados.",
        duracionMinutos = 45,
        horarios = listOf(
            HorarioClase(
                id = 201,
                dia = "Hoy",
                hora = "6:00 pm",
                sala = "Sala 1",
                capacidad = 12,
                cuposDisponibles = 8
            ),
            HorarioClase(
                id = 202,
                dia = "Hoy",
                hora = "8:00 pm",
                sala = "Sala 1",
                capacidad = 12,
                cuposDisponibles = 4
            ),
            HorarioClase(
                id = 203,
                dia = "Mañana",
                hora = "6:00 pm",
                sala = "Sala 1",
                capacidad = 12,
                cuposDisponibles = 7
            )
        )
    ),
    ClaseGimnasio(
        id = 3,
        nombre = "Spinning",
        descripcion = "Entrenamiento en bicicleta estática para mejorar " +
                "la resistencia cardiovascular al ritmo de la música.",
        duracionMinutos = 45,
        horarios = listOf(
            HorarioClase(
                id = 301,
                dia = "Hoy",
                hora = "7:30 pm",
                sala = "Sala 3",
                capacidad = 20,
                cuposDisponibles = 9
            ),
            HorarioClase(
                id = 302,
                dia = "Mañana",
                hora = "7:30 pm",
                sala = "Sala 3",
                capacidad = 20,
                cuposDisponibles = 12
            )
        )
    ),
    // Esta clase solo aparece al consultar Esta semana.
    // Así los filtros muestran una diferencia visible.
    ClaseGimnasio(
        id = 4,
        nombre = "Pilates",
        descripcion = "Ejercicios de control y postura para fortalecer " +
                "el abdomen y mejorar la estabilidad corporal.",
        duracionMinutos = 50,
        horarios = listOf(
            HorarioClase(
                id = 401,
                dia = "Mañana",
                hora = "9:00 am",
                sala = "Sala 2",
                capacidad = 15,
                cuposDisponibles = 8
            )
        )
    )
)

// Incluimos una atención anterior para mostrar el estado Completada.
// Las nuevas reservas se agregarán después desde la aplicación.
val reservasIniciales = listOf(
    Reserva(
        id = 1,
        clase = clasesEjemplo.first { it.id == 1 },
        horario = HorarioClase(
            id = 100,
            dia = "Ayer",
            hora = "7:00 am",
            sala = "Sala 2",
            capacidad = 15,
            cuposDisponibles = 0
        ),
        estado = "Completada"
    )
)