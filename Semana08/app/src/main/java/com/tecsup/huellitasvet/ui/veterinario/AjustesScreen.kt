package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.ui.navigation.Rutas
import kotlinx.coroutines.launch

private val azulAjustes = Color(0xFF1D4ED8)

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

    // Separación adaptable sin cortar formularios en pantallas pequeñas.
    val gapPantalla = ((LocalConfiguration.current.screenHeightDp - 680) / 11).coerceIn(14, 27).dp

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BarraNavegacionVeterinario(Rutas.AJUSTES, onSeleccionarTab) },
        topBar = {
            Row(Modifier.fillMaxWidth().height(62.dp).padding(horizontal = 11.dp),
                verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onVolver) { Icon(Icons.Filled.ArrowBack, "Volver") }
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("Ajustes", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(48.dp))
            }
        }
    ) { inner ->
        Column(Modifier.fillMaxSize().padding(inner).verticalScroll(rememberScrollState())
            .padding(horizontal = 15.dp, vertical = 15.dp),
            verticalArrangement = Arrangement.spacedBy(gapPantalla)) {

            Surface(shape = RoundedCornerShape(17.dp), color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)) {
                Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = Color(0xFFDBEAFE), shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.size(63.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.PersonOutline, null, tint = azulAjustes,
                                modifier = Modifier.size(34.dp))
                        }
                    }
                    Spacer(Modifier.width(13.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(nombre.ifBlank { "Veterinario" }, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(correo.ifBlank { "Cuenta local" }, fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Surface(color = Color(0xFFDBEAFE), shape = RoundedCornerShape(30.dp)) {
                            Text("Perfil activo", color = azulAjustes, fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp))
                        }
                    }
                }
            }
            Button(onClick = onCambiarPerfil, modifier = Modifier.fillMaxWidth().height(70.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = azulAjustes)) {
                Icon(Icons.Filled.SwapHoriz, null, modifier = Modifier.size(27.dp))
                Spacer(Modifier.width(13.dp))
                Column(Modifier.weight(1f)) {
                    Text("Cambiar de perfil", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text("Modo práctica: ir a vista Dueño", fontSize = 12.sp,
                        color = Color.White.copy(alpha = .75f))
                }
                Text("›", fontSize = 25.sp)
            }
            Text("Preferencias", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Surface(shape = RoundedCornerShape(15.dp), color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)) {
                Column(Modifier.padding(horizontal = 14.dp)) {
                    AjusteSwitch("Modo oscuro", "Cambia el tema de la app", oscuro) {
                        scope.launch { preferencias.guardarModoOscuro(it) }
                    }
                    HorizontalDivider()
                    AjusteSwitch("Notificaciones", "Avisar citas nuevas", notificaciones) {
                        scope.launch { preferencias.guardarNotificaciones(it) }
                    }
                    HorizontalDivider()
                    AjusteSwitch("Aceptar citas", "Abrir o cerrar la agenda", aceptarCitas) {
                        scope.launch { preferencias.guardarAceptarCitas(it) }
                    }
                }
            }
            Surface(color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                shape = RoundedCornerShape(15.dp)) {
                Column(Modifier.fillMaxWidth().padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Se guardan en el dispositivo", fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("con DataStore", color = azulAjustes, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
            Spacer(Modifier.height(9.dp))
            OutlinedButton(onClick = onCerrarSesion,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(13.dp),
                border = BorderStroke(1.5.dp, Color(0xFFDC2626))) {
                Icon(Icons.Filled.ExitToApp, null, tint = Color(0xFFDC2626))
                Spacer(Modifier.width(10.dp))
                Text("Cerrar sesión", color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AjusteSwitch(
    titulo: String,
    subtitulo: String,
    valor: Boolean,
    onCambio: (Boolean) -> Unit
) {
    Row(Modifier.fillMaxWidth().height(68.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(titulo, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitulo, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = valor, onCheckedChange = onCambio,
            colors = SwitchDefaults.colors(checkedTrackColor = azulAjustes))
    }
}
