package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.HistorialItem
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetEmptyState
import com.tecsup.huellitasvet.ui.navigation.Rutas
import com.tecsup.huellitasvet.viewmodel.PacienteResumen
import com.tecsup.huellitasvet.viewmodel.PacientesViewModel
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.Locale

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
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                titulo = "Pacientes",
                onVolver = onVolver
            )
        },
        bottomBar = { BarraNavegacionVeterinario(Rutas.PACIENTES, onSeleccionarTab) }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = texto,
                    onValueChange = vm::buscar,
                    placeholder = { Text("Buscar mascota o dueño...") },
                    leadingIcon = { Icon(Icons.Filled.Search, null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("TODOS" to "Todos", "PERRO" to "Perros", "GATO" to "Gatos", "OTRO" to "Otros")
                        .forEach { (valor, titulo) ->
                            val activo = filtro == valor
                            FilterChip(
                                selected = activo,
                                onClick = { vm.filtrarPorEspecie(valor) },
                                label = { Text(titulo, fontWeight = FontWeight.SemiBold) },
                                shape = RoundedCornerShape(20.dp)
                            )
                        }
                }
            }

            if (pacientes.isEmpty()) {
                VetEmptyState(
                    titulo = "No se encontraron pacientes",
                    descripcion = "Prueba realizando una búsqueda por otro nombre o cambiando el filtro de especie.",
                    icono = Icons.Default.Groups,
                    modifier = Modifier.padding(top = 30.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pacientes, key = { it.datos.mascota.id }) { paciente ->
                        TarjetaPaciente(paciente) { onPaciente(paciente.datos.mascota.id) }
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaPaciente(paciente: PacienteResumen, onClick: () -> Unit) {
    val mascota = paciente.datos.mascota
    val dueno = paciente.datos.dueno
    val especie = runCatching { Especie.valueOf(mascota.especie.uppercase()) }.getOrDefault(Especie.OTRO)
    val edad = (Year.now().value - mascota.anioNacimiento).coerceAtLeast(0)
    val fecha = paciente.ultimaVisita?.let { fechaBreve(it) } ?: "Sin visitas"

    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MascotaAvatar(especie = especie, tamano = 60.dp)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = mascota.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${mascota.raza} · $edad años",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Dueño: ${dueno.nombre}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Última visita: $fecha",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Ver ficha",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun FichaPacienteScreen(vm: PacientesViewModel, mascotaId: Int, onVolver: () -> Unit) {
    val pacientes by vm.todos.collectAsState()
    val paciente = pacientes.firstOrNull { it.datos.mascota.id == mascotaId }
    val fuente = remember(vm, mascotaId) { vm.historial(mascotaId) }
    val historial by fuente.collectAsState(initial = emptyList())

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                titulo = "Ficha clínica del paciente",
                onVolver = onVolver
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            if (paciente == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                val mascota = paciente.datos.mascota
                val dueno = paciente.datos.dueno
                val especie = runCatching { Especie.valueOf(mascota.especie.uppercase()) }.getOrDefault(Especie.OTRO)
                val edad = (Year.now().value - mascota.anioNacimiento).coerceAtLeast(0)

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        MascotaAvatar(especie = especie, tamano = 72.dp)

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = mascota.nombre,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${mascota.raza} · $edad años",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Dueño: ${dueno.nombre}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Peso registrado: ${"%.1f".format(Locale.US, mascota.peso)} kg",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Text(
                    text = "Historial clínico",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                if (historial.isEmpty()) {
                    VetEmptyState(
                        titulo = "Sin atenciones registradas",
                        descripcion = "Este paciente aún no registra historial de consultas completadas.",
                        modifier = Modifier.padding(top = 10.dp)
                    )
                } else {
                    historial.forEach { detalle ->
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                HistorialItem(
                                    fecha = detalle.cita.fecha,
                                    servicio = detalle.servicio.nombre,
                                    diagnostico = detalle.cita.diagnostico.orEmpty(),
                                    peso = detalle.cita.pesoRegistrado
                                )
                                if (!detalle.cita.tratamiento.isNullOrBlank()) {
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        text = "Tratamiento: ${detalle.cita.tratamiento}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
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

private fun fechaBreve(fecha: String): String = runCatching {
    LocalDate.parse(fecha).format(DateTimeFormatter.ofPattern("dd MMM", Locale("es", "PE")))
}.getOrDefault(fecha)
