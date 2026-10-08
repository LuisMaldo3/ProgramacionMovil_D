package com.saludplus.citas.data.repository

import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.model.Usuario

object Repositorio {

    private val usuarios = mutableListOf<Usuario>()
    private val citas = mutableListOf<Cita>()

    private val especialidades = listOf(
        Especialidad(1, "Medicina General", "Atención primaria"),
        Especialidad(2, "Pediatría", "Salud infantil"),
        Especialidad(3, "Cardiología", "Corazón y circulación"),
        Especialidad(4, "Dermatología", "Piel, cabello y uñas"),
        Especialidad(5, "Odontología", "Salud bucal")
    )

    private val medicos = listOf(
        Medico(1, "Dra. Ana Torres", 1, 4.8),
        Medico(2, "Dr. Luis Paredes", 1, 4.5),
        Medico(3, "Dra. Carla Rojas", 2, 4.9),
        Medico(4, "Dr. Marco Salas", 3, 4.7),
        Medico(5, "Dra. Lucía Vega", 3, 4.6),
        Medico(6, "Dr. Hugo Medina", 4, 4.4),
        Medico(7, "Dra. Rosa Quispe", 5, 4.8)
    )

    private val horariosBase = listOf(
        "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
        "11:00", "11:30", "15:00", "15:30", "16:00", "16:30"
    )

    var usuarioActual: Usuario? = null
        private set

    fun registrarUsuario(usuario: Usuario): Boolean {
        return false
    }

    fun iniciarSesion(correo: String, contrasena: String): Boolean {
        return false
    }

    fun cerrarSesion() {
    }

    fun buscarEspecialidades(texto: String): List<Especialidad> {
        return emptyList()
    }

    fun especialidadesDestacadas(): List<Especialidad> {
        return emptyList()
    }

    fun obtenerEspecialidad(id: Int): Especialidad? {
        return null
    }

    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> {
        return emptyList()
    }

    fun buscarMedicos(especialidadId: Int, texto: String): List<Medico> {
        return emptyList()
    }

    fun obtenerMedico(id: Int): Medico? {
        return null
    }

    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        return emptyList()
    }

    fun agendarCita(medicoId: Int, fecha: String, hora: String): Boolean {
        return false
    }

    fun citasDelUsuario(): List<Cita> {
        return emptyList()
    }

    fun obtenerCita(id: Int): Cita? {
        return null
    }

    fun cancelarCita(id: Int): Boolean {
        return false
    }
}