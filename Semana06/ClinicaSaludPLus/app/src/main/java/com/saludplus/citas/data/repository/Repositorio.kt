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

    var ultimaCita: Cita? = null
        private set

    fun siguienteIdUsuario(): Int = (usuarios.maxOfOrNull { it.id } ?: 0) + 1

    fun registrarUsuario(usuario: Usuario): Boolean {
        val yaExiste = usuarios.any { it.correo.equals(usuario.correo, ignoreCase = true) }
        if (yaExiste) return false
        usuarios.add(usuario)
        return true
    }

    fun iniciarSesion(correo: String, contrasena: String): Boolean {
        val encontrado = usuarios.find {
            it.correo.equals(correo, ignoreCase = true) && it.contrasena == contrasena
        }
        usuarioActual = encontrado
        return encontrado != null
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    fun buscarEspecialidades(texto: String): List<Especialidad> =
        especialidades.filter { it.nombre.contains(texto, ignoreCase = true) }

    fun especialidadesDestacadas(): List<Especialidad> = especialidades.take(4)

    fun obtenerEspecialidad(id: Int): Especialidad? = especialidades.find { it.id == id }

    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> =
        medicos.filter { it.especialidadId == especialidadId }
            .sortedByDescending { it.calificacion }

    fun buscarMedicos(especialidadId: Int, texto: String): List<Medico> =
        medicosPorEspecialidad(especialidadId)
            .filter { it.nombre.contains(texto, ignoreCase = true) }

    fun obtenerMedico(id: Int): Medico? = medicos.find { it.id == id }

    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        val ocupados = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha }
            .map { it.hora }
        return horariosBase.filter { it !in ocupados }
    }

    fun agendarCita(medicoId: Int, fecha: String, hora: String): Boolean {
        val usuario = usuarioActual ?: return false
        val medico = obtenerMedico(medicoId) ?: return false
        val ocupado = citas.any {
            it.medicoId == medicoId && it.fecha == fecha && it.hora == hora
        }
        if (ocupado) return false

        val nuevoId = (citas.maxOfOrNull { it.id } ?: 0) + 1
        val cita = Cita(
            nuevoId,
            usuario.id,
            medicoId,
            medico.especialidadId,
            fecha,
            hora
        )

        citas.add(cita)
        ultimaCita = cita
        return true
    }

    fun citasDelUsuario(): List<Cita> {
        val usuario = usuarioActual ?: return emptyList()
        return citas
            .filter { it.usuarioId == usuario.id }
            .sortedWith(compareBy({ it.fecha }, { it.hora }))
    }

    fun obtenerCita(id: Int): Cita? = citas.find { it.id == id }

    fun cancelarCita(id: Int): Boolean = citas.removeIf { it.id == id }
}