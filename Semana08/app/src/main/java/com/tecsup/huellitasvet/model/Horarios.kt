package com.tecsup.huellitasvet.model

import java.util.Locale

/** 18 turnos, desde las 09:00 hasta las 17:30, cada media hora. */
object Horarios {
    val todos: List<String> = (9 * 60..17 * 60 + 30 step 30).map { minutos ->
        String.format(Locale.US, "%02d:%02d", minutos / 60, minutos % 60)
    }
}
