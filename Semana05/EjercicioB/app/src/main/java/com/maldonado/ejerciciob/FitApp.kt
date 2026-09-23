package com.maldonado.ejerciciob

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

@Composable
fun FitApp() {
    val navController = rememberNavController()

    // Todas las pantallas consultan la misma lista observable.
    val reservas = remember {
        mutableStateListOf<Reserva>().apply {
            addAll(reservasIniciales)
        }
    }

    var siguienteId by remember {
        mutableStateOf(
            (reservasIniciales.maxOfOrNull { it.id } ?: 0) + 1
        )
    }

    // Reemplazamos el registro para que Compose actualice la interfaz.
    val cancelarReserva: (Int) -> Unit = { reservaId ->
        val indice = reservas.indexOfFirst {
            it.id == reservaId && it.estado == "Confirmada"
        }

        if (indice >= 0) {
            reservas[indice] = reservas[indice].copy(
                estado = "Cancelada"
            )
        }
    }

    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route ?: "inicio"

    val destinos = listOf(
        "inicio" to "Inicio",
        "reservas" to "Reservas",
        "rutinas" to "Rutinas",
        "perfil" to "Perfil"
    )

    val esPrincipal = destinos.any { it.first == rutaActual }
    val esConfirmacion = rutaActual == "confirmacion/{reservaId}"

    val titulo = when (rutaActual) {
        "reservas" -> "Mis reservas"
        "rutinas" -> "Mis rutinas"
        "perfil" -> "Mi perfil"
        "detalle/{claseId}" -> "Detalle de clase"
        else -> "TECSUP Fit"
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            when {
                esConfirmacion -> {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsTopHeight(WindowInsets.statusBars)
                    )
                }

                rutaActual == "inicio" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(VerdeFit)
                            .windowInsetsPadding(WindowInsets.statusBars)
                            .padding(horizontal = 18.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "TECSUP Fit",
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(Modifier.height(3.dp))

                        Text(
                            text = "Hola, Diego",
                            fontSize = 12.sp,
                            color = Color(0xFFD8EEE7)
                        )
                    }
                }

                else -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .windowInsetsPadding(WindowInsets.statusBars)
                            .heightIn(min = 56.dp)
                            .padding(end = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // La flecha regresa desde el detalle de la clase.
                        if (!esPrincipal) {
                            IconButton(
                                onClick = {
                                    navController.popBackStack()
                                }
                            ) {
                                Text(
                                    text = "←",
                                    fontSize = 24.sp,
                                    color = TextoFit
                                )
                            }
                        } else {
                            Spacer(Modifier.width(24.dp))
                        }

                        Text(
                            text = titulo,
                            fontSize = if (esPrincipal) 22.sp else 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoFit
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (esPrincipal) {
                Column {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 18.dp),
                        color = Color(0xFFE3E3E3)
                    )

                    NavigationBar(
                        containerColor = Color.White,
                        tonalElevation = 0.dp
                    ) {
                        destinos.forEach { (ruta, nombre) ->
                            val seleccionada = rutaActual == ruta

                            NavigationBarItem(
                                selected = seleccionada,
                                onClick = {
                                    // Cambiamos de sección sin recuperar detalles anteriores.
                                    navController.navigate(ruta) {
                                        popUpTo("inicio") {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                },
                                icon = {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .border(
                                                width = 1.5.dp,
                                                color = if (seleccionada) {
                                                    VerdeFit
                                                } else {
                                                    TextoSecundarioFit
                                                },
                                                shape = CircleShape
                                            )
                                    )
                                },
                                label = {
                                    Text(
                                        text = nombre,
                                        fontSize = 11.sp,
                                        fontWeight = if (seleccionada) {
                                            FontWeight.Bold
                                        } else {
                                            FontWeight.Normal
                                        }
                                    )
                                },
                                alwaysShowLabel = true,
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = VerdeFit,
                                    selectedTextColor = VerdeFit,
                                    unselectedIconColor = TextoSecundarioFit,
                                    unselectedTextColor = TextoSecundarioFit,
                                    indicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        // Respetamos el espacio ocupado por el encabezado y la barra inferior.
        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            composable("inicio") {
                InicioScreen(
                    alElegirClase = { clase ->
                        navController.navigate("detalle/${clase.id}")
                    }
                )
            }

            composable(
                route = "detalle/{claseId}",
                arguments = listOf(
                    navArgument("claseId") {
                        type = NavType.IntType
                    }
                )
            ) { entrada ->
                val claseId = entrada.arguments?.getInt("claseId")
                val claseBase = clasesEjemplo.firstOrNull {
                    it.id == claseId
                }

                if (claseBase != null) {
                    // Las reservas canceladas dejan de ocupar un cupo.
                    val claseActualizada = claseBase.copy(
                        horarios = claseBase.horarios.map { horario ->
                            val ocupados = reservas.count {
                                it.clase.id == claseBase.id &&
                                        it.horario.id == horario.id &&
                                        it.estado == "Confirmada"
                            }

                            horario.copy(
                                cuposDisponibles =
                                    (horario.cuposDisponibles - ocupados)
                                        .coerceAtLeast(0)
                            )
                        }
                    )

                    DetalleClaseScreen(
                        clase = claseActualizada,
                        alReservar = { horarioElegido ->
                            // Evitamos registrar dos reservas activas del mismo horario.
                            val existente = reservas.firstOrNull {
                                it.clase.id == claseBase.id &&
                                        it.horario.id == horarioElegido.id &&
                                        it.estado == "Confirmada"
                            }

                            val horarioBase = claseBase.horarios.firstOrNull {
                                it.id == horarioElegido.id
                            }

                            val ocupados = reservas.count {
                                it.clase.id == claseBase.id &&
                                        it.horario.id == horarioElegido.id &&
                                        it.estado == "Confirmada"
                            }

                            // Partimos del dato original para descontar una sola vez.
                            val disponibles =
                                (horarioBase?.cuposDisponibles ?: 0) - ocupados

                            val reservaConfirmada = when {
                                existente != null -> existente

                                horarioBase != null && disponibles > 0 -> {
                                    val nuevaReserva = Reserva(
                                        id = siguienteId,
                                        clase = claseBase,
                                        horario = horarioBase
                                    )

                                    reservas.add(nuevaReserva)
                                    siguienteId++
                                    nuevaReserva
                                }

                                else -> null
                            }

                            if (reservaConfirmada != null) {
                                navController.navigate(
                                    "confirmacion/${reservaConfirmada.id}"
                                ) {
                                    // Quitamos el detalle después de completar la reserva.
                                    popUpTo("inicio") {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                } else {
                    MensajeFit(
                        titulo = "Clase no encontrada",
                        descripcion = "Regresa al inicio y selecciona otra clase."
                    )
                }
            }

            composable(
                route = "confirmacion/{reservaId}",
                arguments = listOf(
                    navArgument("reservaId") {
                        type = NavType.IntType
                    }
                )
            ) { entrada ->
                val reservaId = entrada.arguments?.getInt("reservaId")
                val reserva = reservas.firstOrNull {
                    it.id == reservaId
                }

                if (reserva != null) {
                    ConfirmacionScreen(
                        reserva = reserva,
                        alVerReservas = {
                            // Abrimos el listado sin volver a registrar la reserva.
                            navController.navigate("reservas") {
                                popUpTo("inicio") {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    )
                } else {
                    MensajeFit(
                        titulo = "Reserva no encontrada",
                        descripcion = "Regresa al inicio para continuar."
                    )
                }
            }

            composable("reservas") {
                FiltrosReservasScreen(
                    reservas = reservas,
                    alCancelar = cancelarReserva
                )
            }

            composable("rutinas") {
                RutinasScreen()
            }

            composable("perfil") {
                PerfilScreen(usuario = usuarioEjemplo)
            }
        }
    }
}

@Composable
fun MensajeFit(
    titulo: String,
    descripcion: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = titulo,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextoFit,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = descripcion,
            fontSize = 14.sp,
            color = TextoSecundarioFit,
            textAlign = TextAlign.Center
        )
    }
}