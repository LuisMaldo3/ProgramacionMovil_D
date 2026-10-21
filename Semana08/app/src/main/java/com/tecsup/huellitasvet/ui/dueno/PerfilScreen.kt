package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pets
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
import com.tecsup.huellitasvet.viewmodel.PerfilViewModel
import com.tecsup.huellitasvet.ui.components.*
import com.tecsup.huellitasvet.ui.navigation.Rutas
import kotlinx.coroutines.launch

@Composable
fun PerfilScreen(
    vm: PerfilViewModel,
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
    val avisos by preferencias.notificaciones.collectAsState(initial = true)
    val resumen by vm.resumen.collectAsState()
    val verde = PremiumVerde

    Scaffold(modifier = Modifier.fillMaxSize(), containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 13.dp),
                verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onVolver) { Icon(Icons.Filled.ArrowBack, "Volver") }
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("Mi perfil", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(48.dp))
            }
        },
        bottomBar = { BarraNavegacionDueno(Rutas.PERFIL, onSeleccionarTab) }
    ) { inner ->
        Column(Modifier.fillMaxSize().padding(inner).verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(19.dp)) {
            EncabezadoPersonaPremium(nombre, correo, veterinario = false)

            Column(verticalArrangement = Arrangement.spacedBy(11.dp)) {
                TituloSeccionPremium("Tu actividad", "El bienestar de tus mascotas, en un vistazo")
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    IndicadorPremium(resumen.mascotas, "Mascotas", Icons.Filled.Pets, verde, Modifier.weight(1f))
                    IndicadorPremium(resumen.proximas, "Próximas", Icons.Filled.CalendarMonth, Color(0xFFCC8200), Modifier.weight(1f))
                    IndicadorPremium(resumen.atenciones, "Atenciones", Icons.Filled.Favorite, Color(0xFF2458DE), Modifier.weight(1f))
                }
            }

            Surface(onClick = onCambiarPerfil, shape = RoundedCornerShape(21.dp),
                color = verde, shadowElevation = 4.dp) {
                Row(Modifier.fillMaxWidth().padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(13.dp)) {
                    Surface(color = Color.White.copy(alpha=.18f), shape = RoundedCornerShape(14.dp)) {
                        Icon(Icons.Filled.SwapHoriz, null, tint = Color.White,
                            modifier = Modifier.padding(11.dp).size(26.dp))
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Cambiar de perfil", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        Text("Modo práctica · Vista veterinario", color = Color.White.copy(alpha=.88f), fontSize = 13.sp)
                    }
                    Icon(Icons.Filled.ChevronRight, null, tint = Color.White)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(11.dp)) {
                TituloSeccionPremium("Preferencias", "Personaliza tu experiencia")
                ElevatedCard(shape = RoundedCornerShape(21.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        PreferenciaPremium(Icons.Filled.DarkMode, "Modo oscuro", "Diseño cómodo en cualquier momento",
                            oscuro, verde) { scope.launch { preferencias.guardarModoOscuro(it) } }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        PreferenciaPremium(Icons.Filled.NotificationsActive, "Recordatorios", "Avisos de citas y controles", avisos,
                            verde) { scope.launch { preferencias.guardarNotificaciones(it) } }
                    }
                }
            }

            Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.primaryContainer.copy(alpha=.50f)) {
                Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Filled.Info, null, tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp))
                    Column {
                        Text("Preferencias protegidas", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Se guardan en este dispositivo con DataStore", fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            OutlinedButton(onClick = onCerrarSesion, modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(17.dp), border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.error)) {
                Icon(Icons.Filled.ExitToApp, null, tint = MaterialTheme.colorScheme.error)
                Spacer(Modifier.width(10.dp))
                Text("Cerrar sesión", color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}
