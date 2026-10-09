package com.saludplus.citas.ui.screens.locales

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.model.Local
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraInferior
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.BordeTarjeta
import com.saludplus.citas.ui.theme.ChipDisponibleFondo
import com.saludplus.citas.ui.theme.ChipDisponibleTexto
import com.saludplus.citas.ui.theme.RellenoIconoConfirmar
import com.saludplus.citas.ui.theme.RellenoTarjetaMedico
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Building
import compose.icons.fontawesomeicons.solid.Clock
import compose.icons.fontawesomeicons.solid.MapMarkerAlt
import compose.icons.fontawesomeicons.solid.PhoneVolume

@Composable
fun LocalesScreen(navController: NavController) {
    val e = rememberEscala()
    val locales = Repositorio.locales
    val localActual = Repositorio.localSeleccionado

    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = { BarraSuperior("Locales") },
        bottomBar = { BarraInferior(Rutas.LOCALES, navController) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = e.d(16), vertical = e.d(12)),
            verticalArrangement = Arrangement.spacedBy(e.d(16))
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = e.d(4))) {
                    Text(
                        "Nuestras Sedes",
                        fontSize = e.s(24),
                        lineHeight = e.s(30),
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal
                    )
                    Spacer(Modifier.height(e.d(4)))
                    Text(
                        "Selecciona el local de Clínica SaludPlus donde deseas atenderte para agendar tu cita.",
                        fontSize = e.s(16),
                        lineHeight = e.s(22),
                        color = TextoSecundario
                    )
                }
            }

            items(locales, key = { it.id }) { local ->
                val esSeleccionado = localActual?.id == local.id
                TarjetaLocal(
                    local = local,
                    esSeleccionado = esSeleccionado,
                    onSeleccionar = {
                        Repositorio.seleccionarLocal(local)
                        navController.navigate(Rutas.ESPECIALIDADES)
                    }
                )
            }
        }
    }
}

@Composable
private fun TarjetaLocal(
    local: Local,
    esSeleccionado: Boolean,
    onSeleccionar: () -> Unit
) {
    val e = rememberEscala()
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(e.d(16)),
        colors = CardDefaults.cardColors(
            containerColor = if (esSeleccionado) RellenoTarjetaMedico else Color.White
        ),
        border = BorderStroke(
            if (esSeleccionado) 2.dp else 1.dp,
            if (esSeleccionado) Azul else BordeTarjeta
        )
    ) {
        Column(
            modifier = Modifier.padding(e.d(16))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(e.d(52))
                        .clip(RoundedCornerShape(e.d(12)))
                        .background(RellenoIconoConfirmar),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        FontAwesomeIcons.Solid.Building,
                        contentDescription = null,
                        tint = Azul,
                        modifier = Modifier.size(e.d(26))
                    )
                }
                Spacer(Modifier.width(e.d(12)))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Local ${local.nombre}",
                        fontSize = e.s(21),
                        lineHeight = e.s(26),
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal
                    )
                    Text(
                        "Sede oficial SaludPlus",
                        fontSize = e.s(15),
                        color = TextoSecundario
                    )
                }
                if (esSeleccionado) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(e.d(8)))
                            .background(ChipDisponibleFondo)
                            .padding(horizontal = e.d(10), vertical = e.d(4))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = ChipDisponibleTexto,
                                modifier = Modifier.size(e.d(16))
                            )
                            Spacer(Modifier.width(e.d(4)))
                            Text(
                                "Seleccionado",
                                fontSize = e.s(14),
                                fontWeight = FontWeight.Bold,
                                color = ChipDisponibleTexto
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(e.d(14)))

            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    FontAwesomeIcons.Solid.MapMarkerAlt,
                    contentDescription = null,
                    tint = Azul,
                    modifier = Modifier
                        .padding(top = e.d(2))
                        .size(e.d(16))
                )
                Spacer(Modifier.width(e.d(10)))
                Text(
                    local.direccion,
                    fontSize = e.s(16),
                    lineHeight = e.s(21),
                    color = TextoPrincipal
                )
            }

            Spacer(Modifier.height(e.d(8)))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    FontAwesomeIcons.Solid.Clock,
                    contentDescription = null,
                    tint = TextoSecundario,
                    modifier = Modifier.size(e.d(16))
                )
                Spacer(Modifier.width(e.d(10)))
                Text(
                    local.horarioAtencion,
                    fontSize = e.s(15),
                    color = TextoSecundario
                )
            }

            if (local.telefono.isNotBlank()) {
                Spacer(Modifier.height(e.d(8)))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        FontAwesomeIcons.Solid.PhoneVolume,
                        contentDescription = null,
                        tint = TextoSecundario,
                        modifier = Modifier.size(e.d(16))
                    )
                    Spacer(Modifier.width(e.d(10)))
                    Text(
                        local.telefono,
                        fontSize = e.s(15),
                        color = TextoSecundario
                    )
                }
            }

            Spacer(Modifier.height(e.d(16)))

            if (esSeleccionado) {
                BotonPrincipal(
                    "Continuar agendamiento en este local",
                    onClick = onSeleccionar
                )
            } else {
                OutlinedButton(
                    onClick = onSeleccionar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(e.d(52)),
                    shape = RoundedCornerShape(e.d(12)),
                    border = BorderStroke(1.dp, Azul),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Azul)
                ) {
                    Text(
                        "Elegir local ${local.nombre}",
                        fontSize = e.s(18),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
