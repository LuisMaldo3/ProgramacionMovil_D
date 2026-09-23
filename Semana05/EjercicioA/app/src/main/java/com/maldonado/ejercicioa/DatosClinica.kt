package com.maldonado.ejercicioa

import androidx.compose.ui.graphics.Color

// Colores compartidos para mantener el mismo diseño en las pantallas.
val MoradoClinica = Color(0xFF602B88)
val FondoClinica = Color(0xFFF4F0F8)
val LilaClinica = Color(0xFFEDE2F6)
val VerdeClinica = Color(0xFF16856A)

// Reunimos la información que necesitamos mostrar de cada médico.
data class Medico(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val calificacion: String,
    val experiencia: String,
    val descripcion: String
)

// Las citas nuevas comienzan con el estado Confirmada.
data class Cita(
    val id: Int,
    val medico: Medico,
    val fecha: String,
    val hora: String,
    val estado: String = "Confirmada"
)

// Utilizamos datos de ejemplo para que la aplicación funcione sin internet.
val medicosEjemplo = listOf(
    Medico(
        id = 1,
        nombre = "Dra. Ana Torres",
        especialidad = "Cardiología",
        calificacion = "4.9",
        experiencia = "12 años exp.",
        descripcion = "Especialista en arritmias e hipertensión, " +
                "formación en la Clínica Mayo."
    ),
    Medico(
        id = 2,
        nombre = "Dr. Luis Vega",
        especialidad = "Pediatría",
        calificacion = "4.7",
        experiencia = "8 años de experiencia",
        descripcion = "Atiende a niños y adolescentes. " +
                "Realiza controles de crecimiento y acompaña " +
                "a las familias en el cuidado de sus hijos."
    ),
    Medico(
        id = 3,
        nombre = "Dra. Rosa Díaz",
        especialidad = "Dermatología",
        calificacion = "4.8",
        experiencia = "10 años de experiencia",
        descripcion = "Especialista en el cuidado de la piel. " +
                "Brinda orientación para prevenir y tratar " +
                "problemas dermatológicos."
    )
)