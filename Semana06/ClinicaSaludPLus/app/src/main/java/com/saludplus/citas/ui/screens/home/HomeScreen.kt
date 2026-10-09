package com.saludplus.citas.ui.screens.home

import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.fontawesomeicons.Solid
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.solid.FileAlt
import compose.icons.fontawesomeicons.solid.User
import compose.icons.fontawesomeicons.solid.CalendarCheck
import compose.icons.fontawesomeicons.solid.CalendarAlt
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.clickable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraInferior
import com.saludplus.citas.ui.components.IconoEspecialidad
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.BordeTarjeta
import com.saludplus.citas.ui.theme.RellenoTarjetaEspecialidad
import com.saludplus.citas.ui.theme.TarjetaAgendarFondo
import com.saludplus.citas.ui.theme.TarjetaCitasFondo
import com.saludplus.citas.ui.theme.TarjetaCitasTexto
import com.saludplus.citas.ui.theme.TarjetaDatosFondo
import com.saludplus.citas.ui.theme.TarjetaDatosTexto
import com.saludplus.citas.ui.theme.TarjetaResultadosFondo
import com.saludplus.citas.ui.theme.TarjetaResultadosTexto
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala

import androidx.compose.runtime.LaunchedEffect

@Composable
fun HomeScreen(navController: NavController) {
    val e = rememberEscala()

    LaunchedEffect(Unit) {
        if (Repositorio.usuarioActual == null) {
            navController.navigate(Rutas.LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    val nombre = Repositorio.usuarioActual?.nombre
        ?.trim()?.split(" ")?.firstOrNull()?.takeIf { it.isNotEmpty() } ?: "Paciente"
    val destacadas = Repositorio.especialidadesDestacadas()

    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = { BarraInferior(Rutas.HOME, navController) }
    ) { interior ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = interior.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            // Saludo y campana (y 56–137)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = e.d(56), start = e.d(18), end = e.d(14)),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = e.d(10))
                ) {
                    Text(
                        "¡Hola, $nombre!",
                        fontSize = e.s(31),
                        lineHeight = e.s(38),
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal
                    )
                    Spacer(Modifier.height(e.d(6)))
                    Text(
                        "¿Qué deseas hacer hoy?",
                        fontSize = e.s(21),
                        lineHeight = e.s(26),
                        color = TextoSecundario
                    )
                }
                Icon(
                    Icons.Filled.Notifications,
                    contentDescription = "Notificaciones",
                    tint = TextoPrincipal,
                    modifier = Modifier
                        .padding(top = e.d(2))
                        .size(e.d(32))
                        .clickable { navController.navigate(Rutas.NOTIFICACIONES) }
                )
            }

            Spacer(Modifier.height(e.d(27)))

            // Tarjetas 2 x 2 (y 163–434)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = e.d(16)),
                horizontalArrangement = Arrangement.spacedBy(e.d(12))
            ) {
                AccesoRapido(
                    texto = "Agendar cita",
                    icono = FontAwesomeIcons.Solid.CalendarAlt,
                    fondo = TarjetaAgendarFondo,
                    colorTexto = Azul,
                    onClick = {
                        if (Repositorio.localSeleccionado == null) {
                            navController.navigate(Rutas.LOCALES)
                        } else {
                            navController.navigate(Rutas.ESPECIALIDADES)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                AccesoRapido(
                    texto = "Mis citas",
                    icono = FontAwesomeIcons.Solid.CalendarCheck,
                    fondo = TarjetaCitasFondo,
                    colorTexto = TarjetaCitasTexto,
                    onClick = { navController.navigate(Rutas.MIS_CITAS) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(e.d(14)))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = e.d(16)),
                horizontalArrangement = Arrangement.spacedBy(e.d(12))
            ) {
                AccesoRapido(
                    texto = "Mis datos",
                    icono = FontAwesomeIcons.Solid.User,
                    fondo = TarjetaDatosFondo,
                    colorTexto = TarjetaDatosTexto,
                    onClick = { navController.navigate(Rutas.PERFIL) },
                    modifier = Modifier.weight(1f)
                )
                AccesoRapido(
                    texto = "Resultados",
                    icono = FontAwesomeIcons.Solid.FileAlt,
                    fondo = TarjetaResultadosFondo,
                    colorTexto = TarjetaResultadosTexto,
                    onClick = { navController.navigate(Rutas.RESULTADOS) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(e.d(32)))

            // Especialidades destacadas (y 468–639)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = e.d(16)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Especialidades destacadas",
                    fontSize = e.s(18),
                    lineHeight = e.s(24),
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal
                )
                Text(
                    "Ver todas",
                    modifier = Modifier.clickable {
                        if (Repositorio.localSeleccionado == null) {
                            navController.navigate(Rutas.LOCALES)
                        } else {
                            navController.navigate(Rutas.ESPECIALIDADES)
                        }
                    },
                    fontSize = e.s(18),
                    lineHeight = e.s(24),
                    fontWeight = FontWeight.Bold,
                    color = Azul
                )
            }
            Spacer(Modifier.height(e.d(14)))
            // Cada tarjeta mide lo mismo y las tres llenan todo el ancho de la pantalla
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val separacion = e.d(8)
                val margen = e.d(14)
                val cantidad = destacadas.size.coerceAtLeast(1)
                val anchoTarjeta = (maxWidth - margen * 2 - separacion * (cantidad - 1)) / cantidad
                LazyRow(
                    contentPadding = PaddingValues(horizontal = margen),
                    horizontalArrangement = Arrangement.spacedBy(separacion)
                ) {
                    items(destacadas) { especialidad ->
                        EspecialidadDestacada(
                            especialidad = especialidad,
                            ancho = anchoTarjeta,
                            onClick = {
                                if (Repositorio.localSeleccionado == null) {
                                    navController.navigate(Rutas.LOCALES)
                                } else {
                                    navController.navigate(Rutas.medicos(especialidad.id))
                                }
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(e.d(24)))
        }
    }
}

@Composable
private fun AccesoRapido(
    texto: String,
    icono: ImageVector,
    fondo: Color,
    colorTexto: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val e = rememberEscala()
    Card(
        onClick = onClick,
        modifier = modifier.height(e.d(128)),
        shape = RoundedCornerShape(e.d(16)),
        colors = CardDefaults.cardColors(containerColor = fondo),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(e.d(19)))
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorTexto,
                modifier = Modifier.size(e.d(54))
            )
            Spacer(Modifier.height(e.d(12)))
            Text(
                texto,
                fontSize = e.s(19),
                lineHeight = e.s(24),
                fontWeight = FontWeight.Bold,
                color = colorTexto
            )
        }
    }
}

@Composable
private fun EspecialidadDestacada(especialidad: Especialidad, ancho: Dp, onClick: () -> Unit) {
    val e = rememberEscala()
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(ancho)
            .height(e.d(176)),
        shape = RoundedCornerShape(e.d(16)),
        colors = CardDefaults.cardColors(containerColor = RellenoTarjetaEspecialidad),
        border = BorderStroke(1.dp, BordeTarjeta),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = e.d(2)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(e.d(16)))
            IconoEspecialidad(especialidad = especialidad, tamano = e.d(76))
            Spacer(Modifier.height(e.d(12)))
            NombreEspecialidad(texto = especialidad.nombre, anchoMax = ancho - e.d(10))
        }
    }
}

/**
 * Nombre de la especialidad que SIEMPRE cabe en la tarjeta: se mide el texto y se baja el tamaño
 * hasta que la palabra más larga entre completa en una línea (nunca se parte a la mitad).
 * Los nombres con espacio ("Medicina General") pueden ir en dos líneas.
 */
@Composable
private fun NombreEspecialidad(texto: String, anchoMax: Dp) {
    val e = rememberEscala()
    val medidor = rememberTextMeasurer()
    val anchoPx = with(LocalDensity.current) { anchoMax.toPx() }
    val tamano = remember(texto, anchoPx, e.k) {
        var t = 17f
        val palabras = texto.split(" ")
        while (t > 9f) {
            val cabe = palabras.all { palabra ->
                medidor.measure(
                    text = palabra,
                    style = TextStyle(fontSize = e.s(t), fontWeight = FontWeight.Bold)
                ).size.width <= anchoPx
            }
            if (cabe) break
            t -= 0.5f
        }
        t
    }
    // Una palabra = una sola línea (sin salto); varias palabras = hasta dos líneas, cortando en el espacio
    val variasPalabras = texto.contains(" ")
    Text(
        texto,
        modifier = Modifier.fillMaxWidth(),
        fontSize = e.s(tamano),
        lineHeight = e.s(tamano * 1.25f),
        fontWeight = FontWeight.Bold,
        color = TextoPrincipal,
        textAlign = TextAlign.Center,
        maxLines = if (variasPalabras) 2 else 1,
        softWrap = variasPalabras,
        overflow = TextOverflow.Visible
    )
}
