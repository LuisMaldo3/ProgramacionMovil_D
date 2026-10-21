package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.navigation.Rutas
import com.tecsup.huellitasvet.viewmodel.InicioViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val Verde = Color(0xFF059669)
private val Fondo = Color(0xFFF6FDF9)
private val Mint = Color(0xFFECFDF5)
private val Borde = Color(0xFFDAE8E3)
private val Oscuro = Color(0xFF1F2937)
private val Gris = Color(0xFF929AA6)
private val Amarillo = Color(0xFFFFF2C6)

/** Mantiene exactamente la firma usada por NavVet del Paso 7. */
@Composable
fun InicioScreen(
    vm: InicioViewModel,
    nombreDueno: String,
    onVerMascotas: () -> Unit,
    onMascota: (Int) -> Unit,
    onAgregarMascota: () -> Unit,
    onPedirCita: () -> Unit,
    onSeleccionarTab: (String) -> Unit
) {
    val mascotas by vm.mascotas.collectAsState()
    val cita by vm.proximaCita.collectAsState()

    Scaffold(
        containerColor = Fondo,
        bottomBar = {
            BarraNavegacionDueno(
                seleccionado = Rutas.INICIO,
                onSeleccionar = onSeleccionarTab
            )
        }
    ) { insets ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(insets)
        ) {
            // En teléfonos más altos, repartimos el espacio para evitar
            // el hueco enorme bajo «Pedir una cita».
            val separacion = ((maxHeight - 538.dp) / 6f)
                .coerceIn(10.dp, 62.dp)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 26.dp, end = 26.dp, top = 14.dp, bottom = 14.dp)
            ) {
                // CABECERA
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Hola, ${nombreDueno.ifBlank { "bienvenido" }}",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold,
                            color = Oscuro,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "¿Cómo están tus mascotas hoy?",
                            fontSize = 13.sp,
                            color = Gris
                        )
                    }
                    IconButton(onClick = { onSeleccionarTab(Rutas.MIS_CITAS) }) {
                        Icon(
                            Icons.Filled.Notifications,
                            contentDescription = "Avisos",
                            modifier = Modifier.size(21.dp),
                            tint = Oscuro
                        )
                    }
                }

                Spacer(Modifier.height(separacion))
                TarjetaCitaFiel(cita)
                Spacer(Modifier.height(separacion))

                // TITULO Y MASCOTAS
                Row(
                    modifier = Modifier.fillMaxWidth().height(23.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Mis mascotas", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Oscuro)
                    Text(
                        "Ver todas", fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold, color = Verde,
                        modifier = Modifier.clickable(onClick = onVerMascotas)
                    )
                }
                Spacer(Modifier.height(9.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    val visibles = mascotas.take(2)
                    visibles.forEach { mascota ->
                        TarjetaMascotaFiel(
                            mascota = mascota,
                            modifier = Modifier.weight(1f),
                            onClick = { onMascota(mascota.id) }
                        )
                    }
                    repeat((2 - visibles.size).coerceAtLeast(0)) {
                        Spacer(Modifier.weight(1f))
                    }
                    Surface(
                        onClick = onAgregarMascota,
                        modifier = Modifier.weight(1f).height(105.dp),
                        color = Color.White,
                        shape = RoundedCornerShape(15.dp),
                        border = BorderStroke(1.5.dp, Verde)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Filled.Add, "Agregar mascota", tint = Verde, modifier = Modifier.size(26.dp))
                            Spacer(Modifier.height(10.dp))
                            Text("Agregar", color = Verde, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(Modifier.height(separacion))
                Text("Servicios", fontSize = 15.sp, color = Oscuro, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(11.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    ServicioFiel("Consulta", Icons.Filled.Favorite, Modifier.weight(1f), onPedirCita)
                    ServicioFiel("Vacunas", Icons.Filled.Vaccines, Modifier.weight(1f), onPedirCita)
                    ServicioFiel("Baño", Icons.Filled.ContentCut, Modifier.weight(1f), onPedirCita)
                }

                Spacer(Modifier.height(separacion))
                // AVISO AMARILLO DEL MOCKUP (ejemplo de texto de la guía)
                Surface(
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    color = Amarillo,
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Notifications, contentDescription = null,
                            tint = Color(0xFFD97706), modifier = Modifier.size(19.dp))
                        Spacer(Modifier.width(12.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text("Recordatorio", fontSize = 12.sp,
                                fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                            Text("Max: refuerzo de vacuna en 5 días", fontSize = 12.sp,
                                color = Color(0xFF97501F), maxLines = 1,
                                overflow = TextOverflow.Ellipsis)
                        }
                    }
                }

                Spacer(Modifier.height(separacion))
                Button(
                    onClick = onPedirCita,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(11.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Verde, contentColor = Color.White)
                ) {
                    Text("Pedir una cita", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun TarjetaCitaFiel(detalle: CitaConDetalle?) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(112.dp),
        color = Verde,
        shape = RoundedCornerShape(19.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 17.dp, end = 16.dp, top = 12.dp, bottom = 11.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Próxima cita", color = Color(0xFFD3F4E8), fontSize = 12.sp)
                Spacer(Modifier.height(5.dp))
                if (detalle == null) {
                    Text("Aún no tienes citas", color = Color.White,
                        fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(Modifier.height(7.dp))
                    Text("Reserva una consulta para tu mascota", color = Color.White,
                        fontSize = 12.sp, maxLines = 2)
                } else {
                    val c = detalle.cita
                    Text("${detalle.mascota.mascota.nombre} · ${detalle.servicio.nombre}",
                        fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp,
                        maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Spacer(Modifier.height(5.dp))
                    val fecha = runCatching {
                        LocalDate.parse(c.fecha).format(
                            DateTimeFormatter.ofPattern("EEE d MMM", Locale("es", "PE"))
                        ).replaceFirstChar { it.titlecase(Locale("es", "PE")) }
                    }.getOrDefault(c.fecha)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Schedule, null, tint = Color.White,
                            modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(5.dp))
                        Text("$fecha · ${c.hora}", fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                    Spacer(Modifier.height(4.dp))
                    Surface(color = Color.White, shape = RoundedCornerShape(50.dp)) {
                        Text(if (c.estado == "CONFIRMADA") "Confirmada" else "Pendiente",
                            color = Verde, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp))
                    }
                }
            }
            Box(
                modifier = Modifier.size(52.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                if (detalle == null) {
                    Icon(Icons.Filled.CalendarMonth, contentDescription = null,
                        tint = Verde, modifier = Modifier.size(27.dp))
                } else {
                    MascotaAvatar(
                        especie = especieDe(detalle.mascota.mascota.especie),
                        tamano = 44.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaMascotaFiel(mascota: Mascota, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(105.dp),
        color = Color.White,
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, Borde)
    ) {
        Column(
            modifier = Modifier.padding(top = 9.dp, bottom = 6.dp, start = 4.dp, end = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MascotaAvatar(especieDe(mascota.especie), tamano = 55.dp)
            Spacer(Modifier.height(3.dp))
            Text(mascota.nombre, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Oscuro,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("Ver ficha", fontSize = 12.sp, color = Gris)
        }
    }
}

@Composable
private fun ServicioFiel(titulo: String, icono: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(70.dp),
        color = Mint,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFD1FAE5))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icono, contentDescription = titulo, tint = Verde, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(7.dp))
            Text(titulo, fontSize = 12.sp, color = Oscuro, fontWeight = FontWeight.SemiBold)
        }
    }
}

/** Una sola barra compartida: NO usar NavigationBarItem porque genera óvalos. */
@Composable
fun BarraNavegacionDueno(seleccionado: String, onSeleccionar: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .navigationBarsPadding()
    ) {
        HorizontalDivider(thickness = 1.dp, color = Color(0xFFE7ECEB))
        Row(
            modifier = Modifier.fillMaxWidth().height(61.dp).padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val opciones = listOf(
                Triple(Rutas.INICIO, "Inicio", Icons.Filled.Home),
                Triple(Rutas.MASCOTAS, "Mascotas", Icons.Filled.Pets),
                Triple(Rutas.MIS_CITAS, "Citas", Icons.Filled.CalendarMonth),
                Triple(Rutas.PERFIL, "Perfil", Icons.Filled.PersonOutline)
            )
            opciones.forEach { (ruta, etiqueta, icono) ->
                val activo = seleccionado == ruta
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight()
                        .clickable { onSeleccionar(ruta) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(icono, contentDescription = etiqueta, modifier = Modifier.size(22.dp),
                        tint = if (activo) Verde else Color(0xFF9CA3AF))
                    Spacer(Modifier.height(4.dp))
                    Text(etiqueta, fontSize = 12.sp,
                        fontWeight = if (activo) FontWeight.Bold else FontWeight.Normal,
                        color = if (activo) Verde else Color(0xFF9CA3AF),
                        textAlign = TextAlign.Center)
                }
            }
        }
    }
}

// También se utiliza en MascotasScreen.kt; debe estar definido solo aquí.
internal fun especieDe(valor: String): Especie =
    runCatching { Especie.valueOf(valor) }.getOrDefault(Especie.OTRO)
