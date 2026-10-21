
package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.ui.components.CitaCard
import com.tecsup.huellitasvet.ui.navigation.Rutas
import com.tecsup.huellitasvet.viewmodel.MisCitasViewModel

private val VerdeCitas = Color(0xFF059669)
private val FondoCitas = Color(0xFFF6FDF9)
private val TextoCitas = Color(0xFF1F2937)
private val GrisCitas = Color(0xFF7C8795)

@Composable
fun MisCitasScreen(
    vm: MisCitasViewModel,
    onVolver: () -> Unit,
    onPedirCita: () -> Unit,
    onSeleccionarTab: (String) -> Unit
) {

    val proximas by vm.proximas.collectAsState()
    val historial by vm.historial.collectAsState()
    val cancelandoId by vm.cancelandoId.collectAsState()
    val mensaje by vm.mensaje.collectAsState()

    var pestana by rememberSaveable {
        mutableIntStateOf(0)
    }

    var confirmar by remember {
        mutableStateOf<CitaConDetalle?>(null)
    }

    val snackbar = remember {
        SnackbarHostState()
    }

    LaunchedEffect(mensaje) {
        if (!mensaje.isNullOrBlank()) {
            snackbar.showSnackbar(mensaje.orEmpty())
            vm.limpiarMensaje()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = FondoCitas,

        // ENCABEZADO
        topBar = {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp)
                    .padding(horizontal = 13.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = onVolver,
                    modifier = Modifier.size(42.dp)
                ) {

                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = TextoCitas,
                        modifier = Modifier.size(21.dp)
                    )
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Mis citas",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoCitas
                    )
                }

                Spacer(Modifier.width(42.dp))
            }
        },

        // BARRA INFERIOR
        bottomBar = {

            BarraNavegacionDueno(
                seleccionado = Rutas.MIS_CITAS,
                onSeleccionar = onSeleccionarTab
            )
        },

        // BOTÓN FLOTANTE VERDE
        floatingActionButton = {

            FloatingActionButton(
                onClick = onPedirCita,
                modifier = Modifier.size(70.dp),
                containerColor = VerdeCitas,
                contentColor = Color.White,
                shape = RoundedCornerShape(23.dp),
                elevation =
                    FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp
                    )
            ) {

                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Pedir una cita",
                    modifier = Modifier.size(34.dp)
                )
            }
        },

        snackbarHost = {
            SnackbarHost(hostState = snackbar)
        }

    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(
                start = 21.dp,
                end = 21.dp,
                top = 12.dp,
                bottom = 100.dp
            ),
            verticalArrangement =
                Arrangement.spacedBy(11.dp)
        ) {

            // PESTAÑAS PRÓXIMAS / HISTORIAL
            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .background(
                            Color(0xFFE8F3EE),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(4.dp),
                    horizontalArrangement =
                        Arrangement.spacedBy(2.dp)
                ) {

                    listOf(
                        "Próximas",
                        "Historial"
                    ).forEachIndexed { indice, nombre ->

                        val activo = pestana == indice

                        Surface(
                            onClick = {
                                pestana = indice
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            shape = RoundedCornerShape(13.dp),
                            color = if (activo) {
                                Color.White
                            } else {
                                Color.Transparent
                            },
                            shadowElevation = 0.dp
                        ) {

                            Box(
                                contentAlignment = Alignment.Center
                            ) {

                                Text(
                                    text = nombre,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (activo) {
                                        VerdeCitas
                                    } else {
                                        GrisCitas
                                    }
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(6.dp))
            }

            // LISTADO DE CITAS
            val visibles = if (pestana == 0) {
                proximas
            } else {
                historial
            }

            if (visibles.isEmpty()) {

                item {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 45.dp,
                                bottom = 30.dp
                            ),
                        horizontalAlignment =
                            Alignment.CenterHorizontally,
                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Filled.EventNote,
                            contentDescription = null,
                            tint = Color(0xFF9BA7A2),
                            modifier = Modifier.size(45.dp)
                        )

                        Text(
                            text = if (pestana == 0) {
                                "Aún no tienes próximas citas"
                            } else {
                                "Todavía no hay citas en el historial"
                            },
                            fontSize = 15.sp,
                            color = GrisCitas,
                            textAlign = TextAlign.Center
                        )
                    }
                }

            } else {

                items(
                    items = visibles,
                    key = { it.cita.id }
                ) { detalle ->

                    CitaCard(
                        cita = detalle,
                        mostrarCancelar =
                            pestana == 0 &&
                                    vm.puedeCancelar(detalle),
                        cancelando =
                            cancelandoId == detalle.cita.id,
                        onCancelar = {
                            confirmar = detalle
                        }
                    )
                }
            }

            // AVISO AMARILLO
            if (pestana == 0) {

                item {

                    Spacer(Modifier.height(3.dp))

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 53.dp),
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFFFFF2C6)
                    ) {

                        Row(
                            modifier = Modifier.padding(
                                horizontal = 15.dp,
                                vertical = 13.dp
                            ),
                            verticalAlignment =
                                Alignment.CenterVertically,
                            horizontalArrangement =
                                Arrangement.spacedBy(12.dp)
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Filled.Schedule,
                                contentDescription = null,
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(20.dp)
                            )

                            Text(
                                text = "Puedes cancelar hasta 2 horas antes",
                                color = Color(0xFF92541A),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }

    // DIÁLOGO PARA CONFIRMAR CANCELACIÓN
    confirmar?.let { detalle ->

        AlertDialog(
            onDismissRequest = {
                confirmar = null
            },

            title = {
                Text("Cancelar cita")
            },

            text = {
                Text(
                    "¿Deseas cancelar la cita de " +
                            "${detalle.mascota.mascota.nombre} " +
                            "para ${detalle.servicio.nombre}?"
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        confirmar = null
                        vm.cancelar(detalle)
                    }
                ) {

                    Text(
                        text = "Sí, cancelar",
                        color = Color(0xFFDC2626)
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        confirmar = null
                    }
                ) {

                    Text("Volver")
                }
            }
        )
    }
}
