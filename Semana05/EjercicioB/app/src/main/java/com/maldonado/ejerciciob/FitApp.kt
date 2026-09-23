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

    // Compartimos las reservas entre las pantallas y observamos sus cambios.
    val reservas = remember {
        mutableStateListOf<Reserva>().apply {
            addAll(reservasIniciales)
        }
    }

    // Continuamos la numeración después de las reservas iniciales.
    var siguienteId by remember {
        mutableStateOf(
            (reservasIniciales.maxOfOrNull { it.id } ?: 0) + 1
        )
    }

    // Consultamos la ruta para actualizar el título y la pestaña activa.
    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route ?: "inicio"

    val destinos = listOf(
        "inicio" to "Inicio",
        "reservas" to "Reservas",
        "rutinas" to "Rutinas",
        "perfil" to "Perfil"
    )

    val esPrincipal = destinos.any {
        it.first == rutaActual
    }

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
                    // La confirmación respeta la barra del sistema sin mostrar título.
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
                            .padding(
                                horizontal = 18.dp,
                                vertical = 12.dp
                            )
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
                        // Las pantallas internas incluyen una flecha para regresar.
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
            // Las pestañas aparecen en las cuatro secciones principales.
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
                                    // Conservamos Inicio como base y evitamos duplicar destinos.
                                    navController.navigate(ruta) {
                                        popUpTo("inicio") {
                                            inclusive = false
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
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
        // El contenido respeta el espacio del encabezado y de las pestañas.
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
                // Recuperamos la clase mediante el identificador recibido.
                val claseId = entrada.arguments?.getInt("claseId")

                val claseBase = clasesEjemplo.firstOrNull {
                    it.id == claseId
                }

                if (claseBase != null) {
                    // Calculamos los cupos restantes sin modificar los datos originales.
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
                            // Comprobamos si ya existe una reserva del mismo horario.
                            val existente = reservas.firstOrNull {
                                it.clase.id == claseBase.id &&
                                        it.horario.id == horarioElegido.id &&
                                        it.estado == "Confirmada"
                            }

                            // Usamos el horario original para no descontar cupos dos veces.
                            val horarioBase = claseBase.horarios.firstOrNull {
                                it.id == horarioElegido.id
                            }

                            // Revisamos la disponibilidad antes de guardar el registro.
                            val ocupados = reservas.count {
                                it.clase.id == claseBase.id &&
                                        it.horario.id == horarioElegido.id &&
                                        it.estado == "Confirmada"
                            }

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

                                    // Guardamos la reserva y preparamos el siguiente ID.
                                    reservas.add(nuevaReserva)
                                    siguienteId++

                                    nuevaReserva
                                }

                                else -> null
                            }

                            if (reservaConfirmada != null) {
                                // La confirmación recibe el identificador del registro.
                                navController.navigate(
                                    "confirmacion/${reservaConfirmada.id}"
                                ) {
                                    // Retiramos el detalle al completar la reserva.
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
                // Buscamos la reserva guardada para mostrar sus datos.
                val reservaId = entrada.arguments?.getInt("reservaId")

                val reserva = reservas.firstOrNull {
                    it.id == reservaId
                }

                if (reserva != null) {
                    ConfirmacionScreen(
                        reserva = reserva,
                        alVerReservas = {
                            // Abrimos el listado sin registrar nuevamente la clase.
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
                ReservasScreen(
                    reservas = reservas
                )
            }

            // Conectamos las pantallas completas con sus pestañas.
            composable("rutinas") {
                RutinasScreen()
            }

            composable("perfil") {
                PerfilScreen(
                    usuario = usuarioEjemplo
                )
            }
        }
    }
}

// Mostramos un mensaje cuando faltan datos o una sección está vacía.
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