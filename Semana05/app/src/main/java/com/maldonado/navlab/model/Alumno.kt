package com.maldonado.navlab.model

data class Alumno(
    val id: Int,
    val nombreCorto: String,
    val carrera: String,
    val carreraPerfil: String = carrera,
    val nombreCompleto: String = nombreCorto,
    val idEstudiante: String = "—",
    val correo: String = "—",
    val correoPerfil: String = correo,
    val facultad: String = "—",
    val biografia: String = "—",
    val telefono: String = "—",
    val cicloActual: String = "—",
    val fotoResId: Int? = null
)

object AlumnoRepository {
    val alumnos = listOf(
        Alumno(
            id = 1,
            nombreCorto = "Maldonado",
            carrera = "Ingeniería de Sistemas",
            carreraPerfil = "Ingeniería de Software",
            nombreCompleto = "Maldonado",
            idEstudiante = "2024-0001",
            correo = "juan.leon@example.com",
            correoPerfil = "juan.leon@tecsup.edu.pe",
            facultad = "Ingeniería y Tecnología",
            biografia = "Estudiante destacado con interés en desarrollo Android.",
            telefono = "+51 987 654 321",
            cicloActual = "VI Ciclo"
        ),
        Alumno(
            id = 2,
            nombreCorto = "Maria Garcia",
            carrera = "Arquitectura"
        ),
        Alumno(
            id = 3,
            nombreCorto = "Carlos Perez",
            carrera = "Medicina"
        ),
        Alumno(
            id = 4,
            nombreCorto = "Ana Lopez",
            carrera = "Derecho"
        ),
        Alumno(
            id = 5,
            nombreCorto = "Luis Ramirez",
            carrera = "Administración"
        )
    )

    fun getAlumnoById(id: Int): Alumno {
        return alumnos.find { it.id == id } ?: alumnos.first()
    }
}
