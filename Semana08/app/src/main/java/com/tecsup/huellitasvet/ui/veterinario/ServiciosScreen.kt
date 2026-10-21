package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.ui.navigation.Rutas
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel
import java.util.Locale

private val azul = Color(0xFF1D4ED8)
private val fondo = Color(0xFFF7F9FF)
private val borde = Color(0xFFE2E8F0)

@Composable
fun ServiciosScreen(
    vm: ServiciosViewModel,
    onEditar: (Int) -> Unit,
    onAgregar: () -> Unit,
    onVolver: () -> Unit,
    onSeleccionarTab: (String) -> Unit
) {
    val servicios by vm.servicios.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(mensaje) {
        if (!mensaje.isNullOrBlank()) {
            snackbar.showSnackbar(mensaje.orEmpty())
            vm.limpiarMensaje()
        }
    }

    Scaffold(
        containerColor = fondo,
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = { BarraNavegacionVeterinario(Rutas.SERVICIOS, onSeleccionarTab) },
        topBar = {
            Row(Modifier.fillMaxWidth().height(62.dp).padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onVolver) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                }
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("Servicios", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(48.dp))
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAgregar,
                containerColor = azul,
                contentColor = Color.White,
                shape = RoundedCornerShape(19.dp),
                modifier = Modifier.size(66.dp)
            ) { Icon(Icons.Filled.Add, "Nuevo servicio", Modifier.size(31.dp)) }
        }
    ) { inner ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(inner),
            contentPadding = PaddingValues(start = 15.dp, end = 15.dp, top = 12.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(servicios, key = { it.id }) { servicio ->
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(15.dp),
                    border = BorderStroke(1.dp, borde),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(49.dp),
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFDBEAFE)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(iconoServicio(servicio.icono), null, tint = azul,
                                    modifier = Modifier.size(25.dp))
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(servicio.nombre, fontWeight = FontWeight.Bold,
                                fontSize = 14.sp, maxLines = 1)
                            Text("${servicio.duracionMin} min · S/ ${String.format(Locale.US, "%.2f", servicio.precio)}",
                                color = Color(0xFF7E8796), fontSize = 12.sp)
                        }
                        Switch(
                            checked = servicio.activo,
                            onCheckedChange = { vm.alternar(servicio) },
                            colors = SwitchDefaults.colors(checkedTrackColor = azul)
                        )
                        IconButton(onClick = { onEditar(servicio.id) }) {
                            Icon(Icons.Filled.Edit, "Editar ${servicio.nombre}", tint = azul,
                                modifier = Modifier.size(21.dp))
                        }
                    }
                }
            }
        }
    }
}

internal fun iconoServicio(valor: String): ImageVector = when(valor) {
    "syringe" -> Icons.Filled.Vaccines
    "scissors" -> Icons.Filled.ContentCut
    else -> Icons.Filled.Favorite
}
