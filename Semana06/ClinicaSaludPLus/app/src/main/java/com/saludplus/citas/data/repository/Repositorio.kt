package com.saludplus.citas.data.repository

import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Local
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.model.Usuario

object Repositorio {

    private val usuarios = mutableListOf<Usuario>()
    private val citas = mutableListOf<Cita>()

    val locales = listOf(
        Local(
            id = 1,
            nombre = "La Molina",
            direccion = "Av. Javier Prado Este 6210, La Molina",
            telefono = "(01) 612-3456"
        ),
        Local(
            id = 2,
            nombre = "Independencia",
            direccion = "Av. Carlos Izaguirre 120, Independencia",
            telefono = "(01) 612-3457"
        ),
        Local(
            id = 3,
            nombre = "San Isidro",
            direccion = "Av. República de Panamá 3450, San Isidro",
            telefono = "(01) 612-3458"
        ),
        Local(
            id = 4,
            nombre = "Miraflores",
            direccion = "Av. Benavides 1240, Miraflores",
            telefono = "(01) 612-3459"
        ),
        Local(
            id = 5,
            nombre = "Santiago de Surco",
            direccion = "Av. Caminos del Inca 1500, Surco",
            telefono = "(01) 612-3460"
        )
    )

    var localSeleccionado: Local? = null

    fun seleccionarLocal(local: Local) {
        localSeleccionado = local
    }

    fun obtenerLocal(id: Int): Local? = locales.find { it.id == id }

    private val especialidades = listOf(
        Especialidad(1, "Medicina General", "Atención integral", "Atención Primaria"),
        Especialidad(2, "Pediatría", "Infancia y adolescencia", "Atención Primaria"),
        Especialidad(3, "Ginecología", "Salud de la mujer", "Especialidades Clínicas"),
        Especialidad(4, "Cardiología", "Corazón y sistema sanguíneo", "Especialidades Clínicas"),
        Especialidad(5, "Dermatología", "Piel, cabello y uñas", "Especialidades Clínicas"),
        Especialidad(6, "Traumatología", "Huesos y sistema muscular", "Especialidades Quirúrgicas"),
        Especialidad(7, "Oftalmología", "Salud visual", "Especialidades Quirúrgicas")
    )

    private val medicos = listOf(
        Medico(1, "Dr. Luis Paredes", 1, 4.5, 64, "20481"),
        Medico(2, "Dra. Rosa Quispe", 1, 4.8, 91, "20517"),
        Medico(3, "Dra. Carla Rojas", 2, 4.9, 110, "21034"),
        Medico(4, "Dr. Hugo Medina", 2, 4.4, 58, "21190"),
        Medico(5, "Dra. Ana Torres", 3, 4.9, 120, "12345"),
        Medico(6, "Dra. Claudia Rojas", 3, 4.8, 98, "12871"),
        Medico(7, "Dr. Luis Ramírez", 3, 4.7, 85, "13306"),
        Medico(8, "Dra. Mariana Soto", 3, 4.6, 72, "13752"),
        Medico(9, "Dr. Marco Salas", 4, 4.7, 77, "22418"),
        Medico(10, "Dra. Lucía Vega", 4, 4.6, 69, "22563"),
        Medico(11, "Dra. Elena Campos", 5, 4.8, 88, "23077"),
        Medico(12, "Dr. Pablo Ríos", 5, 4.5, 52, "23245"),
        Medico(13, "Dr. Diego Fuentes", 6, 4.7, 73, "24102"),
        Medico(14, "Dr. Andrés Lara", 6, 4.5, 49, "24388"),
        Medico(15, "Dra. Sofía Mendoza", 7, 4.8, 83, "25016"),
        Medico(16, "Dr. Jorge Navarro", 7, 4.4, 45, "25274")
    )

    // Horarios del diseño: de 08:00 a 12:00 cada 30 minutos
    private val horariosBase = listOf(
        "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
        "11:00", "11:30", "12:00"
    )

    var usuarioActual: Usuario? = null
        private set

    var ultimaCita: Cita? = null
        private set

    init {
        val demo = Usuario(1, "Carlos Mendoza", "carlos@saludplus.com", "123456", "900111222")
        usuarios.add(demo)   // usuario de prueba; la sesión se inicia desde Login o Registro
    }

    fun siguienteIdUsuario(): Int = (usuarios.maxOfOrNull { it.id } ?: 0) + 1

    fun registrarUsuario(usuario: Usuario): Boolean {
        val correoLimpio = usuario.correo.trim()
        val telefonoLimpio = usuario.telefono?.trim() ?: ""

        val yaExiste = usuarios.any {
            (correoLimpio.isNotEmpty() && it.correo.equals(correoLimpio, ignoreCase = true)) ||
                    (telefonoLimpio.isNotEmpty() && it.telefono == telefonoLimpio)
        }
        if (yaExiste) return false
        usuarios.add(usuario)
        return true
    }

    fun iniciarSesion(identificador: String, contrasena: String): Boolean {
        val limpio = identificador.trim()
        val encontrado = usuarios.find {
            (it.correo.equals(limpio, ignoreCase = true) || it.telefono == limpio) &&
                    it.contrasena == contrasena
        }
        usuarioActual = encontrado
        return encontrado != null
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    fun buscarEspecialidades(texto: String): List<Especialidad> =
        especialidades.filter { it.nombre.contains(texto, ignoreCase = true) }

    // Destacadas del diseño: Medicina General, Pediatría y Ginecología
    fun especialidadesDestacadas(): List<Especialidad> = especialidades.take(3)

    fun obtenerEspecialidad(id: Int): Especialidad? = especialidades.find { it.id == id }

    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> =
        medicos.filter { it.especialidadId == especialidadId }
            .sortedByDescending { it.calificacion }

    fun buscarMedicos(especialidadId: Int, texto: String): List<Medico> =
        medicosPorEspecialidad(especialidadId)
            .filter { it.nombre.contains(texto, ignoreCase = true) }

    fun obtenerMedico(id: Int): Medico? = medicos.find { it.id == id }

    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        val currentLocalId = localSeleccionado?.id
        val ocupados = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha && (currentLocalId == null || it.localId == currentLocalId) }
            .map { it.hora }
        return horariosBase.filter { it !in ocupados }
    }

    fun agendarCita(medicoId: Int, fecha: String, hora: String): Boolean {
        val usuario = usuarioActual ?: return false
        val local = localSeleccionado ?: return false
        val medico = obtenerMedico(medicoId) ?: return false
        val ocupado = citas.any {
            it.medicoId == medicoId && it.fecha == fecha && it.hora == hora && it.localId == local.id
        }
        if (ocupado) return false

        val nuevoId = (citas.maxOfOrNull { it.id } ?: 0) + 1
        val cita = Cita(
            id = nuevoId,
            usuarioId = usuario.id,
            medicoId = medicoId,
            especialidadId = medico.especialidadId,
            fecha = fecha,
            hora = hora,
            localId = local.id
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
