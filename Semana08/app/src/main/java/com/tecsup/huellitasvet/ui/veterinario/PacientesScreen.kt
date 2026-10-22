package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.foundation.verticalScroll
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.navigation.Rutas
import com.tecsup.huellitasvet.viewmodel.PacienteResumen
import com.tecsup.huellitasvet.viewmodel.PacientesViewModel
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.Locale

private val Azul = Color(0xFF2458DE)
private val Fondo = Color(0xFFF3F6FC)
private val Texto = Color(0xFF172B3A)
private val Gris = Color(0xFF8A94A2)
private val Borde = Color(0xFFE2E8F0)

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
        topBar = { CabeceraPacientes("Pacientes", onVolver) },
        bottomBar = { BarraNavegacionVeterinario(Rutas.PACIENTES, onSeleccionarTab) }
    ) { inner ->
        Column(Modifier.fillMaxSize().padding(inner)) {
            OutlinedTextField(
                value = texto, onValueChange = vm::buscar,
                placeholder = { Text("Buscar mascota o dueño...", color = Gris, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Filled.Search, null, tint = Gris) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp).heightIn(min = 58.dp),
                shape = RoundedCornerShape(19.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Azul,
                    unfocusedBorderColor = Borde
                )
            )
            Row(
                Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("TODOS" to "Todos", "PERRO" to "Perros", "GATO" to "Gatos", "OTRO" to "Otros")
                    .forEach { (valor, titulo) ->
                        val activo = filtro == valor
                        Surface(
                            onClick = { vm.filtrarPorEspecie(valor) },
                            color = if (activo) Azul else Color.White,
                            contentColor = if (activo) Color.White else Texto,
                            shape = RoundedCornerShape(25.dp),
                            border = if (activo) null else BorderStroke(1.dp, Borde)
                        ) {
                            Text(
                                titulo, fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp)
                            )
                        }
                    }
            }
            if (pacientes.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(Icons.Filled.Search, contentDescription = null,
                            tint = Azul, modifier = Modifier.size(39.dp))
                        Text("No se encontraron pacientes", color = Texto,
                            fontWeight = FontWeight.SemiBold)
                        Text("Prueba otro nombre o filtro de especie", color = Gris,
                            fontSize = 13.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 22.dp, end = 22.dp, top = 3.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
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
    val especie = runCatching { Especie.valueOf(mascota.especie) }.getOrDefault(Especie.OTRO)
    val edad = (Year.now().value - mascota.anioNacimiento).coerceAtLeast(0)
    val nombreBreve = dueno.nombre.trim().split(" ").let { partes ->
        partes.firstOrNull().orEmpty() + (partes.getOrNull(1)?.firstOrNull()?.let { " $it." } ?: "")
    }
    val fecha = paciente.ultimaVisita?.let { fechaBreve(it) } ?: "Sin visitas"
    Surface(
        onClick = onClick,
        color = Color.White,
        shape = RoundedCornerShape(19.dp),
        border = BorderStroke(1.dp, Borde),
        modifier = Modifier.fillMaxWidth().heightIn(min = 91.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MascotaAvatar(especie = especie, tamano = 56.dp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(mascota.nombre, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Texto)
                Text("${mascota.raza} · $edad años", fontSize = 14.sp, color = Gris,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("Responsable: $nombreBreve", fontSize = 14.sp, color = Gris)
                Text(if (paciente.ultimaVisita != null) "Última visita: $fecha" else fecha,
                    fontSize = 14.sp, color = Gris)
            }
            Icon(Icons.Filled.ChevronRight, "Ver ficha", tint = Gris, modifier = Modifier.size(22.dp))
        }
    }
}

// Ficha clínica de solo lectura para el veterinario. No permite editar ni borrar mascotas.
@Composable
fun FichaPacienteScreen(vm: PacientesViewModel, mascotaId: Int, onVolver: () -> Unit) {
    val pacientes by vm.todos.collectAsState()
    val paciente = pacientes.firstOrNull { it.datos.mascota.id == mascotaId }
    val fuente = remember(vm, mascotaId) { vm.historial(mascotaId) }
    val historial by fuente.collectAsState(initial = emptyList())

    Scaffold(containerColor = MaterialTheme.colorScheme.background, topBar = { CabeceraPacientes("Ficha del paciente", onVolver) }) { inner ->
        Column(
            Modifier.fillMaxSize().padding(inner).verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (paciente == null) {
                Text("Cargando paciente...", color = Gris)
            } else {
                val mascota = paciente.datos.mascota
                val dueno = paciente.datos.dueno
                val especie = runCatching { Especie.valueOf(mascota.especie) }.getOrDefault(Especie.OTRO)
                val edad = (Year.now().value - mascota.anioNacimiento).coerceAtLeast(0)
                Surface(color = MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, Borde), shape = RoundedCornerShape(16.dp)) {
                    Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                        MascotaAvatar(especie = especie, tamano = 70.dp)
                        Spacer(Modifier.width(12.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(mascota.nombre, fontWeight = FontWeight.Bold, color = Texto, fontSize = 21.sp)
                            Text("${mascota.raza} · $edad años", color = Gris, fontSize = 14.sp)
                            Text("Responsable: ${dueno.nombre}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 15.sp)
                            Text("Peso actual: ${"%.1f".format(Locale.US, mascota.peso)} kg", color = Azul,
                                fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                    }
                }
                Text("Historial clínico", color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.ExtraBold, fontSize = 21.sp)
                Text("${historial.size} atenciones registradas", fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (historial.isEmpty()) {
                    Text("Aún no tiene atenciones", color = Gris, fontSize = 15.sp)
                } else {
                    historial.forEach { detalle ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(), color = Color.White,
                            shape = RoundedCornerShape(19.dp), border = BorderStroke(1.dp, Borde)
                        ) {
                            HistorialItem(
                                fecha = detalle.cita.fecha,
                                servicio = detalle.servicio.nombre,
                                diagnostico = detalle.cita.diagnostico.orEmpty(),
                                peso = detalle.cita.pesoRegistrado,
                                tratamiento = detalle.cita.tratamiento
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CabeceraPacientes(titulo: String, onVolver: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().height(61.dp).padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onVolver) {
            Icon(Icons.Filled.ArrowBack, "Volver", tint = Texto)
        }
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Text(titulo, color = Texto, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(48.dp))
    }
}

private fun fechaBreve(fecha: String): String = runCatching {
    LocalDate.parse(fecha).format(DateTimeFormatter.ofPattern("dd MMM", Locale("es", "PE")))
}.getOrDefault(fecha)
