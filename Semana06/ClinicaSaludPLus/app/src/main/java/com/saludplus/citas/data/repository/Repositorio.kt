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

    private fun siguienteIdCita(): Int = (citas.maxOfOrNull { it.id } ?: 0) + 1

    fun registrarUsuario(usuario: Usuario): Boolean {
        val correo = usuario.correo.trim()
        val yaExiste = usuarios.any { it.correo.equals(correo, ignoreCase = true) }
        if (yaExiste) return false
        usuarios.add(usuario.copy(correo = correo))
        return true
    }

    fun iniciarSesion(correo: String, contrasena: String): Boolean {
        val usuario = usuarios.firstOrNull {
            it.correo.equals(correo.trim(), ignoreCase = true) && it.contrasena == contrasena
        } ?: return false
        usuarioActual = usuario
        return true
    }

    fun cerrarSesion() {
        usuarioActual = null
        ultimaCita = null
    }

    fun buscarEspecialidades(texto: String): List<Especialidad> {
        val t = texto.trim()
        if (t.isEmpty()) return especialidades
        return especialidades.filter {
            it.nombre.contains(t, ignoreCase = true) ||
                    it.descripcion.contains(t, ignoreCase = true)
        }
    }

    fun especialidadesDestacadas(): List<Especialidad> {
        return especialidades.take(4)
    }

    fun obtenerEspecialidad(id: Int): Especialidad? {
        return especialidades.firstOrNull { it.id == id }
    }

    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> {
        return medicos.filter { it.especialidadId == especialidadId }
    }

    fun buscarMedicos(especialidadId: Int, texto: String): List<Medico> {
        val t = texto.trim()
        return medicosPorEspecialidad(especialidadId).filter {
            t.isEmpty() || it.nombre.contains(t, ignoreCase = true)
        }
    }

    fun obtenerMedico(id: Int): Medico? {
        return medicos.firstOrNull { it.id == id }
    }

    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        val ocupados = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha }
            .map { it.hora }
            .toSet()
        return horariosBase.filter { it !in ocupados }
    }

    fun agendarCita(medicoId: Int, fecha: String, hora: String): Boolean {
        val usuario = usuarioActual ?: return false
        val medico = obtenerMedico(medicoId) ?: return false
        if (hora !in horariosDisponibles(medicoId, fecha)) return false

        val cita = Cita(
            id = siguienteIdCita(),
            usuarioId = usuario.id,
            medicoId = medico.id,
            especialidadId = medico.especialidadId,
            fecha = fecha,
            hora = hora
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

    fun obtenerCita(id: Int): Cita? {
        val usuario = usuarioActual ?: return null
        return citas.firstOrNull { it.id == id && it.usuarioId == usuario.id }
    }

    fun cancelarCita(id: Int): Boolean {
        val cita = obtenerCita(id) ?: return false
        return citas.remove(cita)
    }
}