package com.saludplus.citas.navigation

object Rutas {
    const val SPLASH = "splash"
    const val REGISTRO = "registro"
    const val LOGIN = "login"
    const val TERMINOS = "terminos"
    const val HOME = "home"
    const val ESPECIALIDADES = "especialidades"
    const val MEDICOS = "medicos/{especialidadId}"
    const val FECHA_HORA = "fechahora/{medicoId}"
    const val CONFIRMAR = "confirmar/{medicoId}/{fecha}/{hora}"
    const val CITA_EXITOSA = "citaexitosa"
    const val MIS_CITAS = "miscitas"
    const val DETALLE_CITA = "detallecita/{citaId}"
    const val PERFIL = "perfil"
    const val RESULTADOS = "resultados"
    const val NOTIFICACIONES = "notificaciones"

    fun medicos(especialidadId: Int) = "medicos/$especialidadId"
    fun fechaHora(medicoId: Int) = "fechahora/$medicoId"
    fun confirmar(medicoId: Int, fecha: String, hora: String) = "confirmar/$medicoId/$fecha/$hora"
    fun detalleCita(citaId: Int) = "detallecita/$citaId"
}
