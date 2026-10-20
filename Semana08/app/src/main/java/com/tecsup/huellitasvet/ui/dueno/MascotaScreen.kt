package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.navigation.Rutas
import com.tecsup.huellitasvet.viewmodel.InicioViewModel
import java.time.LocalDate

@Composable
fun MascotasScreen(
    vm: InicioViewModel,
    onMascota: (Int) -> Unit,
    onAgregarMascota: () -> Unit,
    onSeleccionarTab: (String) -> Unit
) {
    val mascotas by vm.mascotas.collectAsState()
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BarraNavegacionDueno(Rutas.MASCOTAS, onSeleccionarTab) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregarMascota, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.Add, contentDescription = "Agregar mascota",
                    tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { inner ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(inner),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Mis mascotas", style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold)
                    Text("Tus mejores amigos en un solo lugar",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            if (mascotas.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 65.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.Pets, null, modifier = Modifier.size(50.dp),
                            tint = MaterialTheme.colorScheme.primary)
                        Text("Todavía no has registrado mascotas")
                        Button(onClick = onAgregarMascota) { Text("Agregar mascota") }
                    }
                }
            } else {
                items(mascotas, key = { it.id }) { mascota ->
                    ElevatedCard(
                        onClick = { onMascota(mascota.id) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(13.dp)
                        ) {
                            MascotaAvatar(especieDe(mascota.especie), tamano = 64.dp)
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Text(mascota.nombre, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                                Text("${mascota.especie.lowercase().replaceFirstChar { it.uppercase() }} · ${mascota.raza}",
                                    style = MaterialTheme.typography.bodySmall)
                                val edad = (LocalDate.now().year - mascota.anioNacimiento).coerceAtLeast(0)
                                Text("$edad ${if (edad == 1) "año" else "años"}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Icon(Icons.Default.ChevronRight, "Ver mascota",
                                tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(55.dp)) }
        }
    }
}
