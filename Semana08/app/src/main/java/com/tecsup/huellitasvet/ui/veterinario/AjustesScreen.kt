package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.ui.components.*
import com.tecsup.huellitasvet.ui.navigation.Rutas
import kotlinx.coroutines.launch

@Composable
fun AjustesScreen(
    nombre: String,
    correo: String,
    preferencias: PreferenciasStore,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit,
    onVolver: () -> Unit,
    onSeleccionarTab: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val oscuro by preferencias.modoOscuro.collectAsState(initial = false)
    val notificaciones by preferencias.notificaciones.collectAsState(initial = true)
    val aceptarCitas by preferencias.aceptarCitas.collectAsState(initial = true)
    val azul = PremiumAzul

    Scaffold(modifier = Modifier.fillMaxSize(), containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 13.dp),
                verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onVolver) { Icon(Icons.Filled.ArrowBack, "Volver") }
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("Mi espacio", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(48.dp))
            }
        }, bottomBar = { BarraNavegacionVeterinario(Rutas.AJUSTES, onSeleccionarTab) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(19.dp)) {
            EncabezadoPersonaPremium(nombre, correo, veterinario = true)

            Surface(onClick = onCambiarPerfil, shape = RoundedCornerShape(21.dp),
                color = azul, shadowElevation = 4.dp) {
                Row(Modifier.fillMaxWidth().padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(13.dp)) {
                    Surface(shape = RoundedCornerShape(14.dp), color = Color.White.copy(alpha=.18f)) {
                        Icon(Icons.Filled.SwapHoriz, contentDescription = null, tint = Color.White,
                            modifier = Modifier.padding(11.dp).size(26.dp))
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Cambiar de perfil", fontSize = 17.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Modo práctica · Vista dueño", fontSize = 13.sp, color = Color.White.copy(alpha=.9f))
                    }
                    Icon(Icons.Filled.ChevronRight, null, tint = Color.White)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(11.dp)) {
                TituloSeccionPremium("Configuración", "Controla tu experiencia y la agenda clínica")
                ElevatedCard(shape = RoundedCornerShape(21.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        PreferenciaPremium(Icons.Filled.DarkMode, "Modo oscuro", "Reduce el brillo de la interfaz",
                            oscuro, azul) { scope.launch { preferencias.guardarModoOscuro(it) } }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        PreferenciaPremium(Icons.Filled.NotificationsActive, "Notificaciones", "Alertas de citas nuevas",
                            notificaciones, azul) { scope.launch { preferencias.guardarNotificaciones(it) } }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        PreferenciaPremium(Icons.Filled.EventAvailable, "Aceptar citas", "Habilita o pausa las nuevas reservas",
                            aceptarCitas, azul) { scope.launch { preferencias.guardarAceptarCitas(it) } }
                    }
                }
            }

            Surface(shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha=.48f)) {
                Row(Modifier.fillMaxWidth().padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Filled.Info, null, modifier = Modifier.size(22.dp), tint = MaterialTheme.colorScheme.primary)
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("Configuración guardada", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Los ajustes se conservan con DataStore", fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            OutlinedButton(onClick = onCerrarSesion, modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(17.dp), border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.error)) {
                Icon(Icons.Filled.ExitToApp, null, tint = MaterialTheme.colorScheme.error)
                Spacer(Modifier.width(10.dp))
                Text("Cerrar sesión", color = MaterialTheme.colorScheme.error, fontSize = 16.sp,
                    fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}
