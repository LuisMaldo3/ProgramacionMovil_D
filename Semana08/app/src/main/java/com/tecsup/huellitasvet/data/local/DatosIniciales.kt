
package com.tecsup.huellitasvet.data.local

import androidx.room.withTransaction
import com.tecsup.huellitasvet.data.local.entity.Cita
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.model.Rol
import kotlinx.coroutines.flow.first
import java.time.LocalDate

object DatosIniciales {

    suspend fun sembrar(db: VetDatabase) {

        // Solo cargar cuando no hay usuarios
        if (db.usuarioDao().contar() != 0) return

        // 1. SERVICIOS
        val listaServicios = listOf(
            Servicio(
                nombre = "Consulta general",
                descripcion = "Revisión médica preventiva",
                duracionMin = 30,
                precio = 60.0,
                activo = true,
                icono = "heart"
            ),
            Servicio(
                nombre = "Vacunación",
                descripcion = "Control y aplicación de vacunas",
                duracionMin = 30,
                precio = 45.0,
                activo = true,
                icono = "syringe"
            ),
            Servicio(
                nombre = "Baño y corte",
                descripcion = "Higiene y cuidado de mascotas",
                duracionMin = 60,
                precio = 50.0,
                activo = true,
                icono = "scissors"
            ),
            Servicio(
                nombre = "Desparasitación",
                descripcion = "Control de parásitos",
                duracionMin = 15,
                precio = 25.0,
                activo = false,
                icono = "heart"
            )
        )

        // Evita duplicar servicios del proyecto base
        val nombresExistentes = db.servicioDao()
            .observarTodos()
            .first()
            .map { it.nombre }
            .toSet()

        val faltantes = listaServicios.filter {
            it.nombre !in nombresExistentes
        }

        if (faltantes.isNotEmpty()) {
            db.servicioDao().insertarTodos(faltantes)
        }

        val servicios = db.servicioDao()
            .observarTodos()
            .first()
            .associateBy { it.nombre }

        val consulta = requireNotNull(
            servicios["Consulta general"]
        )

        val vacunacion = requireNotNull(
            servicios["Vacunación"]
        )

        val bano = requireNotNull(
            servicios["Baño y corte"]
        )

        // 2. USUARIOS, MASCOTAS Y CITAS
        db.withTransaction {

            // Evitar una segunda inserción
            if (db.usuarioDao().contar() != 0) {
                return@withTransaction
            }

            val usuarios = listOf(
                Usuario(
                    nombre = "Ana Torres",
                    correo = "ana@correo.test",
                    clave = "1234",
                    rol = Rol.CLIENTE.name
                ),
                Usuario(
                    nombre = "Luis Pérez",
                    correo = "luis@correo.test",
                    clave = "1234",
                    rol = Rol.CLIENTE.name
                ),
                Usuario(
                    nombre = "Dra. Sofía Paredes",
                    correo = "vet@huellitas.test",
                    clave = "admin",
                    rol = Rol.ADMIN.name
                )
            )

            db.usuarioDao().insertarTodos(usuarios)

            // Recuperar IDs reales de los dueños
            val anaId = requireNotNull(
                db.usuarioDao().login(
                    "ana@correo.test", "1234"
                )
            ).id

            val luisId = requireNotNull(
                db.usuarioDao().login(
                    "luis@correo.test", "1234"
                )
            ).id

            // 3. MASCOTAS
            val lunaId = db.mascotaDao().insertar(
                Mascota(
                    duenoId = anaId,
                    nombre = "Luna",
                    especie = "PERRO",
                    raza = "Beagle",
                    anioNacimiento = 2021,
                    peso = 12.9,
                    sexo = "HEMBRA"
                )
            ).toInt()

            val maxId = db.mascotaDao().insertar(
                Mascota(
                    duenoId = anaId,
                    nombre = "Max",
                    especie = "PERRO",
                    raza = "Shih Tzu",
                    anioNacimiento = 2020,
                    peso = 8.4,
                    sexo = "MACHO"
                )
            ).toInt()

            val rockyId = db.mascotaDao().insertar(
                Mascota(
                    duenoId = luisId,
                    nombre = "Rocky",
                    especie = "PERRO",
                    raza = "Golden Retriever",
                    anioNacimiento = 2019,
                    peso = 24.0,
                    sexo = "MACHO"
                )
            ).toInt()

            val michiId = db.mascotaDao().insertar(
                Mascota(
                    duenoId = luisId,
                    nombre = "Michi",
                    especie = "GATO",
                    raza = "Mestizo",
                    anioNacimiento = 2022,
                    peso = 3.5,
                    sexo = "HEMBRA"
                )
            ).toInt()

            // 4. FECHAS DINAMICAS
            fun fecha(dias: Long): String {
                return LocalDate.now()
                    .plusDays(dias)
                    .toString()
            }

            // 5. CITAS DE PRUEBA
            val citas = listOf(

                // Tres atenciones anteriores de Luna
                Cita(
                    mascotaId = lunaId,
                    servicioId = consulta.id,
                    fecha = fecha(-20),
                    hora = "09:00",
                    nota = "Control preventivo",
                    estado = EstadoCita.ATENDIDA.name,
                    precio = consulta.precio,
                    diagnostico = "Control general favorable",
                    tratamiento = "Alimentación balanceada",
                    pesoRegistrado = 12.4
                ),

                Cita(
                    mascotaId = lunaId,
                    servicioId = vacunacion.id,
                    fecha = fecha(-10),
                    hora = "10:00",
                    nota = "Vacunación anual",
                    estado = EstadoCita.ATENDIDA.name,
                    precio = vacunacion.precio,
                    diagnostico = "Vacuna anual aplicada",
                    tratamiento = "Control en doce meses",
                    pesoRegistrado = 12.7
                ),

                Cita(
                    mascotaId = lunaId,
                    servicioId = consulta.id,
                    fecha = fecha(-2),
                    hora = "11:00",
                    nota = "Control de rutina",
                    estado = EstadoCita.ATENDIDA.name,
                    precio = consulta.precio,
                    diagnostico = "Mascota en buen estado",
                    tratamiento = "Continuar cuidados habituales",
                    pesoRegistrado = 12.9
                ),

                // Atención pasada de Max
                Cita(
                    mascotaId = maxId,
                    servicioId = bano.id,
                    fecha = fecha(-8),
                    hora = "15:00",
                    nota = "Higiene general",
                    estado = EstadoCita.ATENDIDA.name,
                    precio = bano.precio,
                    diagnostico = "Piel y pelaje saludables",
                    tratamiento = "Baño con champú suave",
                    pesoRegistrado = 8.4
                ),

                // Atención pasada de Michi
                Cita(
                    mascotaId = michiId,
                    servicioId = consulta.id,
                    fecha = fecha(-5),
                    hora = "10:30",
                    nota = "Control felino",
                    estado = EstadoCita.ATENDIDA.name,
                    precio = consulta.precio,
                    diagnostico = "Control clínico normal",
                    tratamiento = "Seguimiento preventivo",
                    pesoRegistrado = 3.5
                ),

                // Citas de hoy
                Cita(
                    mascotaId = lunaId,
                    servicioId = consulta.id,
                    fecha = fecha(0),
                    hora = "09:30",
                    nota = "Consulta de seguimiento",
                    estado = EstadoCita.PENDIENTE.name,
                    precio = consulta.precio
                ),

                Cita(
                    mascotaId = maxId,
                    servicioId = vacunacion.id,
                    fecha = fecha(0),
                    hora = "15:30",
                    nota = "Vacunación programada",
                    estado = EstadoCita.CONFIRMADA.name,
                    precio = vacunacion.precio
                ),

                // Citas futuras
                Cita(
                    mascotaId = rockyId,
                    servicioId = consulta.id,
                    fecha = fecha(1),
                    hora = "09:30",
                    nota = "Primera consulta",
                    estado = EstadoCita.PENDIENTE.name,
                    precio = consulta.precio
                ),

                Cita(
                    mascotaId = michiId,
                    servicioId = vacunacion.id,
                    fecha = fecha(2),
                    hora = "11:30",
                    nota = "Vacuna de refuerzo",
                    estado = EstadoCita.CONFIRMADA.name,
                    precio = vacunacion.precio
                ),

                // Cita cancelada
                Cita(
                    mascotaId = lunaId,
                    servicioId = bano.id,
                    fecha = fecha(5),
                    hora = "16:00",
                    nota = "Baño cancelado",
                    estado = EstadoCita.CANCELADA.name,
                    precio = bano.precio
                )
            )

            // Insertar todas las citas
            for (cita in citas) {
                db.citaDao().insertar(cita)
            }
        }
    }
}
