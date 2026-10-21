
package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.HistorialItem
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.navigation.Rutas
import com.tecsup.huellitasvet.viewmodel.PacienteResumen
import com.tecsup.huellitasvet.viewmodel.PacientesViewModel
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.Locale

private val AzulPacientes = Color(0xFF1D4ED8)
private val FondoPacientes = Color(0xFFF7F9FF)
private val TextoPacientes = Color(0xFF1F2937)
private val GrisPacientes = Color(0xFF8A94A2)
private val BordePacientes = Color(0xFFE2E8F0)

@Composable
fun PacientesScreen(
    vm: PacientesViewModel,
    onVolver: () -> Unit,
    onPaciente: (Int) -> Unit,
    onSeleccionarTab: (String) -> Unit
) {
    val texto by vm.busqueda.collectAsState()
    val filtro by vm.especie.collectAsState()
    val pacientes by vm.pacientes.collectAsState()

    Scaffold(
        containerColor = FondoPacientes,
        topBar = {
            CabeceraPacientes(
                "Pacientes",
                onVolver
            )
        },
        bottomBar = {
            BarraNavegacionVeterinario(
                Rutas.PACIENTES,
                onSeleccionarTab
            )
        }
    ) { inner ->

        Column(
            Modifier.fillMaxSize().padding(inner)
        ) {

            OutlinedTextField(
                value = texto,
                onValueChange = vm::buscar,
                placeholder = {
                    Text(
                        "Buscar mascota o dueño...",
                        color = GrisPacientes,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Search,
                        null,
                        tint = GrisPacientes
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 22.dp)
                    .height(55.dp),
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = AzulPacientes,
                    unfocusedBorderColor = BordePacientes
                )
            )

            Row(
                Modifier.fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(
                        horizontal = 22.dp,
                        vertical = 12.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(
                    8.dp
                )
            ) {

                listOf(
                    "TODOS" to "Todos",
                    "PERRO" to "Perros",
                    "GATO" to "Gatos",
                    "OTRO" to "Otros"
                ).forEach { (valor, titulo) ->

                    val activo = filtro == valor

                    Surface(
                        onClick = {
                            vm.filtrarPorEspecie(valor)
                        },
                        color = if (activo)
                            AzulPacientes
                        else Color.White,
                        contentColor = if (activo)
                            Color.White
                        else TextoPacientes,
                        shape = RoundedCornerShape(25.dp),
                        border = if (activo)
                            null
                        else BorderStroke(
                            1.dp,
                            BordePacientes
                        )
                    ) {
                        Text(
                            titulo,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(
                                horizontal = 14.dp,
                                vertical = 9.dp
                            )
                        )
                    }
                }
            }

            if (pacientes.isEmpty()) {

                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No se encontraron pacientes",
                        color = GrisPacientes
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 22.dp,
                        end = 22.dp,
                        top = 3.dp,
                        bottom = 20.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(
                        10.dp
                    )
                ) {

                    items(
                        pacientes,
                        key = { it.datos.mascota.id }
                    ) { paciente ->

                        TarjetaPaciente(
                            paciente = paciente,
                            onClick = {
                                onPaciente(
                                    paciente.datos.mascota.id
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaPaciente(
    paciente: PacienteResumen,
    onClick: () -> Unit
) {
    val mascota = paciente.datos.mascota
    val dueno = paciente.datos.dueno

    val especie = runCatching {
        Especie.valueOf(mascota.especie)
    }.getOrDefault(Especie.OTRO)

    val edad = (Year.now().value -
            mascota.anioNacimiento).coerceAtLeast(0)

    val nombreBreve = dueno.nombre
        .trim().split(" ").let { partes ->
            partes.firstOrNull().orEmpty() +
                    (partes.getOrNull(1)
                        ?.firstOrNull()
                        ?.let { " $it." } ?: "")
        }

    val fecha = paciente.ultimaVisita
        ?.let { fechaBreve(it) }
        ?: "Sin visitas"

    Surface(
        onClick = onClick,
        color = Color.White,
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(
            1.dp,
            BordePacientes
        ),
        modifier = Modifier.fillMaxWidth()
            .heightIn(min = 91.dp)
    ) {

        Row(
            Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            MascotaAvatar(
                especie = especie,
                tamano = 56.dp
            )

            Spacer(Modifier.width(12.dp))

            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(
                    3.dp
                )
            ) {

                Text(
                    mascota.nombre,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPacientes
                )

                Text(
                    "${mascota.raza} · $edad años",
                    fontSize = 12.sp,
                    color = GrisPacientes,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    "Dueño: $nombreBreve",
                    fontSize = 12.sp,
                    color = GrisPacientes
                )

                Text(
                    if (paciente.ultimaVisita != null)
                        "Última visita: $fecha"
                    else fecha,
                    fontSize = 11.sp,
                    color = GrisPacientes
                )
            }

            Icon(
                Icons.Filled.ChevronRight,
                "Ver ficha",
                tint = GrisPacientes,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

// Ficha clínica de solo lectura
@Composable
fun FichaPacienteScreen(
    vm: PacientesViewModel,
    mascotaId: Int,
    onVolver: () -> Unit
) {
    val pacientes by vm.todos.collectAsState()

    val paciente = pacientes.firstOrNull {
        it.datos.mascota.id == mascotaId
    }

    val fuente = remember(vm, mascotaId) {
        vm.historial(mascotaId)
    }

    val historial by fuente.collectAsState(
        initial = emptyList()
    )

    Scaffold(
        containerColor = FondoPacientes,
        topBar = {
            CabeceraPacientes(
                "Ficha del paciente",
                onVolver
            )
        }
    ) { inner ->

        Column(
            Modifier.fillMaxSize()
                .padding(inner)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 22.dp,
                    vertical = 12.dp
                ),
            verticalArrangement = Arrangement.spacedBy(
                14.dp
            )
        ) {

            if (paciente == null) {

                Text(
                    "Cargando paciente...",
                    color = GrisPacientes
                )

            } else {

                val mascota = paciente.datos.mascota
                val dueno = paciente.datos.dueno

                val especie = runCatching {
                    Especie.valueOf(mascota.especie)
                }.getOrDefault(Especie.OTRO)

                val edad = (Year.now().value -
                        mascota.anioNacimiento).coerceAtLeast(0)

                Surface(
                    color = Color.White,
                    border = BorderStroke(
                        1.dp,
                        BordePacientes
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {

                    Row(
                        Modifier.fillMaxWidth().padding(
                            15.dp
                        ),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        MascotaAvatar(
                            especie = especie,
                            tamano = 70.dp
                        )

                        Spacer(Modifier.width(12.dp))

                        Column(
                            verticalArrangement =
                                Arrangement.spacedBy(5.dp)
                        ) {

                            Text(
                                mascota.nombre,
                                fontWeight = FontWeight.Bold,
                                color = TextoPacientes,
                                fontSize = 19.sp
                            )

                            Text(
                                "${mascota.raza} · $edad años",
                                color = GrisPacientes,
                                fontSize = 13.sp
                            )

                            Text(
                                "Dueño: ${dueno.nombre}",
                                color = GrisPacientes,
                                fontSize = 13.sp
                            )

                            Text(
                                "Peso actual: ${
                                    "%.1f".format(
                                        Locale.US,
                                        mascota.peso
                                    )
                                } kg",
                                color = AzulPacientes,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Text(
                    "Historial clínico",
                    color = TextoPacientes,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )

                if (historial.isEmpty()) {

                    Text(
                        "Aún no tiene atenciones",
                        color = GrisPacientes,
                        fontSize = 14.sp
                    )

                } else {

                    historial.forEach { detalle ->

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.White,
                            shape = RoundedCornerShape(15.dp),
                            border = BorderStroke(
                                1.dp,
                                BordePacientes
                            )
                        ) {

                            Column(
                                Modifier.padding(15.dp)
                            ) {

                                HistorialItem(
                                    fecha = detalle.cita.fecha,
                                    servicio = detalle.servicio.nombre,
                                    diagnostico =
                                        detalle.cita.diagnostico.orEmpty(),
                                    peso =
                                        detalle.cita.pesoRegistrado
                                )

                                if (!detalle.cita.tratamiento.isNullOrBlank()) {

                                    Spacer(
                                        Modifier.height(8.dp)
                                    )

                                    Text(
                                        "Tratamiento: ${detalle.cita.tratamiento}",
                                        fontSize = 12.sp,
                                        color = GrisPacientes
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CabeceraPacientes(
    titulo: String,
    onVolver: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth()
            .height(61.dp)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = onVolver) {
            Icon(
                Icons.Filled.ArrowBack,
                "Volver",
                tint = TextoPacientes
            )
        }

        Box(
            Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                titulo,
                color = TextoPacientes,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.width(48.dp))
    }
}

private fun fechaBreve(fecha: String): String =
    runCatching {
        LocalDate.parse(fecha).format(
            DateTimeFormatter.ofPattern(
                "dd MMM",
                Locale("es", "PE")
            )
        )
    }.getOrDefault(fecha)
