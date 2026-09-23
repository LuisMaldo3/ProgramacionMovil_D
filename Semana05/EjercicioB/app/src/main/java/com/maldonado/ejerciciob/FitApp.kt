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
    // El controlador administra las pantallas y el regreso al destino anterior.
    val navController = rememberNavController()

    // Consultamos la ruta actual para resaltar su pestaña.
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

    val titulo = when (rutaActual) {
        "reservas" -> "Mis reservas"
        "rutinas" -> "Mis rutinas"
        "perfil" -> "Mi perfil"
        "detalle/{claseId}" -> "Detalle de clase"
        "seleccion/{claseId}/{horarioId}" -> "Horario seleccionado"
        else -> "TECSUP Fit"
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            if (rutaActual == "inicio") {
                // El encabezado verde se utiliza únicamente en Inicio.
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
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .heightIn(min = 56.dp)
                        .padding(end = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Las pantallas internas permiten regresar con la flecha.
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
        },
        bottomBar = {
            // El detalle tiene su botón inferior; las secciones muestran pestañas.
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
                                    // Evitamos duplicar pantallas al pulsar una pestaña.
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
                                    // El círculo cambia a verde cuando la pestaña está activa.
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
        // Respetamos el espacio del encabezado y de las pestañas.
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
                        // La tarjeta envía el identificador de la clase elegida.
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

                // Recuperamos los datos de la clase recibida por navegación.
                val clase = clasesEjemplo.firstOrNull {
                    it.id == claseId
                }

                if (clase != null) {
                    DetalleClaseScreen(
                        clase = clase,
                        alReservar = { horario ->
                            navController.navigate(
                                "seleccion/${clase.id}/${horario.id}"
                            ) {
                                launchSingleTop = true
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

            // Este destino comprueba el envío de la clase y del horario.
            // El registro y la confirmación se incorporan en el siguiente commit.
            composable(
                route = "seleccion/{claseId}/{horarioId}",
                arguments = listOf(
                    navArgument("claseId") {
                        type = NavType.IntType
                    },
                    navArgument("horarioId") {
                        type = NavType.IntType
                    }
                )
            ) { entrada ->
                val claseId = entrada.arguments?.getInt("claseId")
                val horarioId = entrada.arguments?.getInt("horarioId")

                val clase = clasesEjemplo.firstOrNull {
                    it.id == claseId
                }

                val horario = clase?.horarios?.firstOrNull {
                    it.id == horarioId
                }

                if (clase != null && horario != null) {
                    MensajeFit(
                        titulo = clase.nombre,
                        descripcion = "${horario.dia}, ${horario.hora}" +
                                " · ${horario.sala}\n\n" +
                                "Horario seleccionado. La reserva todavía no se ha registrado."
                    )
                } else {
                    MensajeFit(
                        titulo = "Horario no encontrado",
                        descripcion = "Regresa al detalle y selecciona un horario."
                    )
                }
            }

            // Las rutas quedan disponibles para conectar sus pantallas completas.
            composable("reservas") {
                MensajeFit(
                    titulo = "Reservas",
                    descripcion = "El listado se incorporará en el avance de reservas."
                )
            }

            composable("rutinas") {
                MensajeFit(
                    titulo = "Rutinas",
                    descripcion = "El contenido se incorporará en el avance de secciones."
                )
            }

            composable("perfil") {
                MensajeFit(
                    titulo = usuarioEjemplo.nombre,
                    descripcion = usuarioEjemplo.plan
                )
            }
        }
    }
}

// Compartimos este mensaje entre destinos provisionales y errores.
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