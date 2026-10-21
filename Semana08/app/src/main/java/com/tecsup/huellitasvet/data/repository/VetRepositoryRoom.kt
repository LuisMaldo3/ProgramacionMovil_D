
package com.tecsup.huellitasvet.data.repository

import androidx.room.withTransaction

import com.tecsup.huellitasvet.data.local.VetDatabase
import com.tecsup.huellitasvet.data.local.entity.Cita
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.local.relation.MascotaConDueno
import com.tecsup.huellitasvet.model.ConteoServicio
import com.tecsup.huellitasvet.model.EstadoCita

import kotlinx.coroutines.flow.Flow

class VetRepositoryRoom(
    private val db: VetDatabase
) : VetRepository {

    private val usuarioDao = db.usuarioDao()
    private val servicioDao = db.servicioDao()
    private val mascotaDao = db.mascotaDao()
    private val citaDao = db.citaDao()

    // USUARIOS

    override suspend fun login(
        correo: String,
        clave: String
    ): Usuario? {
        return usuarioDao.login(correo, clave)
    }

    // SERVICIOS

    override fun observarServicios(): Flow<List<Servicio>> {
        return servicioDao.observarTodos()
    }

    override fun observarServiciosActivos(): Flow<List<Servicio>> {
        return servicioDao.observarActivos()
    }

    override suspend fun guardarServicio(s: Servicio) {
        if (s.id == 0) {
            servicioDao.insertar(s)
        } else {
            servicioDao.actualizar(s)
        }
    }

    override suspend fun eliminarServicio(s: Servicio) {
        servicioDao.eliminar(s)
    }

    override suspend fun cambiarActivo(
        id: Int,
        activo: Boolean
    ) {
        servicioDao.cambiarActivo(id, activo)
    }

    override suspend fun servicioTieneCitas(
        id: Int
    ): Boolean {
        return citaDao.contarCitasServicio(id) > 0
    }

    // MASCOTAS

    override fun observarMascotasDeDueno(
        duenoId: Int
    ): Flow<List<Mascota>> {
        return mascotaDao.observarPorDueno(duenoId)
    }

    override fun observarMascota(
        id: Int
    ): Flow<Mascota?> {
        return mascotaDao.observarUna(id)
    }

    override fun observarTodasLasMascotas():
            Flow<List<MascotaConDueno>> {
        return mascotaDao.observarTodas()
    }

    override suspend fun guardarMascota(m: Mascota) {
        if (m.id == 0) {
            mascotaDao.insertar(m)
        } else {
            mascotaDao.actualizar(m)
        }
    }

    override suspend fun eliminarMascota(m: Mascota) {
        mascotaDao.eliminar(m)
    }

    override suspend fun mascotaTieneCitas(
        id: Int
    ): Boolean {
        return mascotaDao.contarCitas(id) > 0
    }

    // HORARIOS

    override fun observarHorasOcupadas(
        fecha: String
    ): Flow<List<String>> {
        return citaDao.observarHorasOcupadas(fecha)
    }

    // CREAR CITA

    override suspend fun crearCita(
        mascotaId: Int,
        servicioId: Int,
        fecha: String,
        hora: String,
        nota: String
    ): Long {

        return db.withTransaction {

            val servicio = servicioDao.obtener(servicioId)
                ?: throw IllegalArgumentException(
                    "El servicio no existe"
                )

            require(servicio.activo) {
                "El servicio no está disponible"
            }

            val ocupadas = citaDao.contarOcupadas(
                fecha,
                hora
            )

            require(ocupadas == 0) {
                "El horario seleccionado ya está ocupado"
            }

            val nuevaCita = Cita(
                mascotaId = mascotaId,
                servicioId = servicioId,
                fecha = fecha,
                hora = hora,
                nota = nota,
                estado = EstadoCita.PENDIENTE.name,
                precio = servicio.precio,
                fechaCreacion = System.currentTimeMillis()
            )

            citaDao.insertar(nuevaCita)
        }
    }

    // CITAS DEL DUEÑO

    override fun observarCitasDeDueno(
        duenoId: Int
    ): Flow<List<CitaConDetalle>> {
        return citaDao.observarPorDueno(duenoId)
    }

    // CITAS POR FECHA

    override fun observarCitasPorFecha(
        fecha: String
    ): Flow<List<CitaConDetalle>> {
        return citaDao.observarPorFecha(fecha)
    }

    // OBSERVAR UNA CITA

    override fun observarCita(
        id: Int
    ): Flow<CitaConDetalle?> {
        return citaDao.observarUna(id)
    }

    // HISTORIAL CLINICO

    override fun observarHistorial(
        mascotaId: Int
    ): Flow<List<CitaConDetalle>> {
        return citaDao.observarHistorial(mascotaId)
    }

    override fun observarHistorialPeso(
        mascotaId: Int
    ): Flow<List<com.tecsup.huellitasvet.model.PuntoPeso>> {
        return citaDao.observarHistorialPeso(mascotaId)
    }

    // CAMBIAR ESTADO

    override suspend fun cambiarEstadoCita(
        id: Int,
        estado: EstadoCita
    ) {
        citaDao.actualizarEstado(
            id,
            estado.name
        )
    }

    // REGISTRAR ATENCION

    override suspend fun registrarAtencion(
        citaId: Int,
        diagnostico: String,
        tratamiento: String,
        peso: Double
    ) {

        db.withTransaction {

            val cita = citaDao.obtener(citaId)
                ?: throw IllegalArgumentException(
                    "La cita no existe"
                )

            citaDao.registrarAtencion(
                id = citaId,
                diagnostico = diagnostico,
                tratamiento = tratamiento,
                peso = peso
            )

            mascotaDao.actualizarPeso(
                id = cita.mascotaId,
                peso = peso
            )
        }
    }

    // ESTADISTICAS DEL PANEL

    override fun observarCitasDeFecha(
        fecha: String
    ): Flow<Int> {
        return citaDao.contarPorFecha(fecha)
    }

    override fun observarCantidadPorEstado(
        estado: EstadoCita
    ): Flow<Int> {
        return citaDao.contarPorEstado(
            estado.name
        )
    }

    override fun observarCitasPorServicio():
            Flow<List<ConteoServicio>> {
        return citaDao.contarPorServicio()
    }

    override fun observarIngresosDesde(
        fechaInicio: String
    ): Flow<Double> {
        return citaDao.ingresosDesde(fechaInicio)
    }
}
