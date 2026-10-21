package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.ui.navigation.Rutas
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDateTime

private val PerfilVerde = Color(0xFF059669)
private val PerfilFondo = Color(0xFFF6FDF9)
private val PerfilTexto = Color(0xFF1F2937)
private val PerfilGris = Color(0xFF88919D)
private val PerfilBorde = Color(0xFFE1E8E5)
private val PerfilRojo = Color(0xFFDC2626)

private data class ResumenPerfil(
    val mascotas: Int = 0,
    val proximas: Int = 0,
    val atenciones: Int = 0
)

@Composable
fun PerfilScreen(
    duenoId: Int,
    nombre: String,
    correo: String,
    repo: VetRepository,
    preferencias: PreferenciasStore,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit,
    onVolver: () -> Unit,
    onSeleccionarTab: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val modoOscuro by preferencias.modoOscuro.collectAsState(initial = false)
    val recordatorios by preferencias.notificaciones.collectAsState(initial = true)

    val resumenFlow = remember(repo, duenoId) {
        combine(
            repo.observarMascotasDeDueno(duenoId),
            repo.observarCitasDeDueno(duenoId)
        ) { mascotas, citas ->
            val ahora = LocalDateTime.now()
            ResumenPerfil(
                mascotas = mascotas.size,
                proximas = citas.count { detalle ->
                    detalle.cita.estado in setOf("PENDIENTE", "CONFIRMADA") &&
                            runCatching {
                                LocalDateTime.parse(
                                    "${detalle.cita.fecha}T${detalle.cita.hora}"
                                ).isAfter(ahora)
                            }.getOrDefault(false)
                },
                atenciones = citas.count { it.cita.estado == "ATENDIDA" }
            )
        }
    }
    val resumen by resumenFlow.collectAsState(initial = ResumenPerfil())

    val fondo = if (modoOscuro) MaterialTheme.colorScheme.background else PerfilFondo
    val superficie = MaterialTheme.colorScheme.surface
    val texto = if (modoOscuro) MaterialTheme.colorScheme.onSurface else PerfilTexto
    val gris = if (modoOscuro) MaterialTheme.colorScheme.onSurfaceVariant else PerfilGris
    val borde = if (modoOscuro) MaterialTheme.colorScheme.outlineVariant else PerfilBorde

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = fondo,
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().height(58.dp)
                    .padding(horizontal = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVolver) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = texto)
                }
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("Perfil", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = texto)
                }
                Spacer(Modifier.width(48.dp))
            }
        },
        bottomBar = {
            BarraNavegacionDueno(
                seleccionado = Rutas.PERFIL,
                onSeleccionar = onSeleccionarTab
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Perfil: avatar de PERSONA, no de mascota.
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = superficie,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, borde)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(58.dp),
                        color = if (modoOscuro) Color(0xFF16473C) else Color(0xFFD1FAE5),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.PersonOutline,
                                contentDescription = "Perfil de persona",
                                tint = if (modoOscuro) Color(0xFF6EE7B7) else PerfilVerde,
                                modifier = Modifier.size(33.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(13.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(
                            nombre.ifBlank { "Usuario" },
                            color = texto,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            correo.ifBlank { "Cuenta local" },
                            color = gris,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Surface(
                            color = if (modoOscuro) Color(0xFF16473C) else Color(0xFFD1FAE5),
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Text(
                                // Ana es el usuario de ejemplo del laboratorio.
                                text = if (nombre.trim().substringBefore(' ').equals("Ana", true))
                                    "Dueña" else "Dueño",
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 2.dp),
                                color = if (modoOscuro) Color(0xFF6EE7B7) else PerfilVerde,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Tres contadores con los colores distintos del mockup.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ResumenColor(
                    numero = resumen.mascotas,
                    titulo = "Mascotas",
                    fondo = if (modoOscuro) Color(0xFF16473C) else Color(0xFFD1FAE5),
                    color = if (modoOscuro) Color(0xFF6EE7B7) else PerfilVerde,
                    modifier = Modifier.weight(1f)
                )
                ResumenColor(
                    numero = resumen.proximas,
                    titulo = "Próximas",
                    fondo = if (modoOscuro) Color(0xFF483918) else Color(0xFFFFF2C6),
                    color = if (modoOscuro) Color(0xFFFCD34D) else Color(0xFFD97706),
                    modifier = Modifier.weight(1f)
                )
                ResumenColor(
                    numero = resumen.atenciones,
                    titulo = "Atenciones",
                    fondo = if (modoOscuro) Color(0xFF1D355B) else Color(0xFFDBEAFE),
                    color = if (modoOscuro) Color(0xFF93C5FD) else Color(0xFF1D4ED8),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(14.dp))

            Button(
                onClick = onCambiarPerfil,
                modifier = Modifier.fillMaxWidth().height(62.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PerfilVerde),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Icon(Icons.Filled.SwapHoriz, contentDescription = null,
                    modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text("Cambiar de perfil", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text("Modo práctica: ir a vista Veterinario", fontSize = 11.sp,
                        color = Color.White.copy(alpha = .82f))
                }
                Icon(Icons.Filled.ChevronRight, contentDescription = null,
                    modifier = Modifier.size(22.dp))
            }

            Spacer(Modifier.height(18.dp))
            Text("Preferencias", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = texto)
            Spacer(Modifier.height(9.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                color = superficie,
                border = BorderStroke(1.dp, borde)
            ) {
                Column(Modifier.padding(horizontal = 13.dp)) {
                    AjustePerfil(
                        titulo = "Modo oscuro",
                        subtitulo = "Cambia el tema de la app",
                        valor = modoOscuro,
                        verde = PerfilVerde
                    ) { scope.launch { preferencias.guardarModoOscuro(it) } }

                    HorizontalDivider(color = borde)

                    AjustePerfil(
                        titulo = "Recordatorios",
                        subtitulo = "Avisar citas y vacunas",
                        valor = recordatorios,
                        verde = PerfilVerde
                    ) { scope.launch { preferencias.guardarNotificaciones(it) } }
                }
            }

            Spacer(Modifier.height(17.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(13.dp),
                color = if (modoOscuro) Color(0xFF15372F) else Color(0xFFECFDF5)
            ) {
                Column(Modifier.padding(horizontal = 14.dp, vertical = 13.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Se guardan en el dispositivo", fontSize = 12.sp,
                        color = gris)
                    Text("con DataStore", fontSize = 12.sp,
                        color = if (modoOscuro) Color(0xFF6EE7B7) else PerfilVerde,
                        fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(25.dp))

            OutlinedButton(
                onClick = onCerrarSesion,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(11.dp),
                border = BorderStroke(1.5.dp, PerfilRojo),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = superficie,
                    contentColor = PerfilRojo
                )
            ) {
                Text("Cerrar sesión", color = PerfilRojo,
                    fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(14.dp))
        }
    }
}

@Composable
private fun ResumenColor(
    numero: Int,
    titulo: String,
    fondo: Color,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(72.dp),
        color = fondo,
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(numero.toString(), fontWeight = FontWeight.Bold,
                fontSize = 23.sp, color = color)
            Spacer(Modifier.height(3.dp))
            Text(titulo, fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp, color = color)
        }
    }
}

@Composable
private fun AjustePerfil(
    titulo: String,
    subtitulo: String,
    valor: Boolean,
    verde: Color,
    onCambio: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(66.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(titulo, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitulo, fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(
            checked = valor,
            onCheckedChange = onCambio,
            colors = SwitchDefaults.colors(
                checkedTrackColor = verde,
                checkedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFD1D5DB),
                uncheckedThumbColor = Color.White,
                uncheckedBorderColor = Color(0xFFD1D5DB)
            )
        )
    }
}
