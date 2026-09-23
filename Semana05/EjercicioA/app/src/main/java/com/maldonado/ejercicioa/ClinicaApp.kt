package com.maldonado.ejercicioa

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import kotlinx.coroutines.launch

@Composable
fun ClinicaApp() {

    // El controlador administra las pantallas y permite regresar a la anterior.
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Observamos la ruta para resaltar la opción correcta del menú.
    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route ?: "inicio"

    val destinos = listOf(
        "inicio" to "Inicio",
        "citas" to "Mis citas",
        "historial" to "Historial médico",
        "perfil" to "Perfil"
    )

    val esPrincipal = destinos.any { it.first == rutaActual }

    val titulo = when (rutaActual) {
        "citas" -> "Mis citas"
        "historial" -> "Historial médico"
        "perfil" -> "Perfil"
        "medico/{medicoId}" -> "Perfil del médico"
        "agendar/{medicoId}" -> "Agendar cita"
        else -> "Clínica Salud+"
    }

    // El menú envuelve al Scaffold para desplegarse sobre la pantalla.
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = esPrincipal,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = MoradoClinica,
                        shape = RoundedCornerShape(24.dp)
                    ),
                drawerShape = RoundedCornerShape(24.dp),
                drawerContainerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(18.dp)
                ) {

                    // Encabezado del paciente siguiendo el avatar y nombre del modelo.
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 6.dp,
                            vertical = 12.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    Color(0xFFEEE5F7),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "JP",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MoradoClinica
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Juan Pérez",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF292929)
                            )
                            Text(
                                text = "Paciente",
                                fontSize = 12.sp,
                                color = Color(0xFF817C86)
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    HorizontalDivider(
                        color = Color(0xFFE4E0E7)
                    )

                    Spacer(Modifier.height(12.dp))

                    // Cada fila muestra un círculo y se resalta cuando está activa.
                    destinos.forEach { (ruta, nombre) ->
                        OpcionMenu(
                            texto = nombre,
                            seleccionada = rutaActual == ruta,
                            alPulsar = {
                                scope.launch {
                                    drawerState.close()
                                }

                                // Conservamos Inicio como base y evitamos duplicados.
                                navController.navigate(ruta) {
                                    popUpTo("inicio") {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            }
                        )

                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    ) {
        Scaffold(
            containerColor = Color.White,
            topBar = {

                // Solo Inicio tiene encabezado morado; las otras pantallas son blancas.
                if (rutaActual == "inicio") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MoradoClinica)
                            .windowInsetsPadding(WindowInsets.statusBars)
                            .padding(
                                start = 18.dp,
                                end = 6.dp,
                                top = 12.dp,
                                bottom = 12.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = "Clínica Salud+",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(Modifier.height(3.dp))

                            Text(
                                text = "Hola, Juan",
                                fontSize = 12.sp,
                                color = Color(0xFFE5D5EF)
                            )
                        }

                        // La guía exige un botón de menú en la barra de Inicio.
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Text(
                                text = "☰",
                                fontSize = 24.sp,
                                color = Color.White
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .windowInsetsPadding(WindowInsets.statusBars)
                            .padding(end = 18.dp)
                            .heightIn(min = 56.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // Las secciones abren el menú; el perfil del médico permite volver.
                        IconButton(
                            onClick = {
                                if (esPrincipal) {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                } else {
                                    navController.popBackStack()
                                }
                            }
                        ) {
                            Text(
                                text = if (esPrincipal) "☰" else "←",
                                fontSize = 23.sp,
                                color = Color(0xFF292929)
                            )
                        }

                        Text(
                            text = titulo,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF292929)
                        )
                    }
                }
            }
        ) { padding ->

            // Registramos todas las rutas en un solo NavHost.
            // El padding mantiene el contenido fuera de las barras.
            NavHost(
                navController = navController,
                startDestination = "inicio",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                composable("inicio") {
                    InicioScreen(
                        alElegirMedico = { medico ->
                            navController.navigate("medico/${medico.id}")
                        }
                    )
                }

                // Pasamos el identificador y recuperamos al médico seleccionado.
                composable(
                    route = "medico/{medicoId}",
                    arguments = listOf(
                        navArgument("medicoId") {
                            type = NavType.IntType
                        }
                    )
                ) { entrada ->
                    val medicoId = entrada.arguments?.getInt("medicoId")
                    val medico = medicosEjemplo.firstOrNull {
                        it.id == medicoId
                    }

                    // En el siguiente avance construiremos el perfil completo.
                    if (medico != null) {
                        // Mostramos el perfil y conservamos el médico al continuar.
                        MedicoScreen(
                            medico = medico,
                            alAgendar = {
                                navController.navigate("agendar/${medico.id}")
                            }
                        )
                    } else {
                        MensajeSeccion(
                            titulo = "Médico no encontrado",
                            descripcion = "Regresa al inicio y selecciona un médico."
                        )
                    }
                }

                // Dejamos conectadas las secciones que completaremos después.
                // Preparamos el destino del formulario que construiremos en el siguiente avance.
                composable(
                    route = "agendar/{medicoId}",
                    arguments = listOf(
                        navArgument("medicoId") {
                            type = NavType.IntType
                        }
                    )
                ) { entrada ->
                    val medicoId = entrada.arguments?.getInt("medicoId")
                    val medico = medicosEjemplo.firstOrNull {
                        it.id == medicoId
                    }

                    if (medico != null) {
                        MensajeSeccion(
                            titulo = "Agendar con ${medico.nombre}",
                            descripcion = "Aquí seleccionaremos la fecha y la hora."
                        )
                    } else {
                        MensajeSeccion(
                            titulo = "Médico no encontrado",
                            descripcion = "Regresa al inicio y selecciona un médico."
                        )
                    }
                }

                // Dejamos disponible la sección donde mostraremos las citas registradas.
                composable("citas") {
                    MensajeSeccion(
                        titulo = "Todavía no hay citas para mostrar",
                        descripcion = "Aquí aparecerán tus reservas."
                    )
                }

                composable("historial") {
                    MensajeSeccion(
                        titulo = "Historial médico",
                        descripcion = "Aquí aparecerán las atenciones completadas."
                    )
                }

                composable("perfil") {
                    MensajeSeccion(
                        titulo = "Juan Pérez",
                        descripcion = "Paciente"
                    )
                }
            }
        }
    }
}

// Reutilizamos el diseño de cada opción para mantener iguales los espacios.
@Composable
private fun OpcionMenu(
    texto: String,
    seleccionada: Boolean,
    alPulsar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (seleccionada) Color(0xFFEEE5F7)
                else Color.Transparent
            )
            .clickable(onClick = alPulsar)
            .padding(horizontal = 12.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .border(
                    width = 1.5.dp,
                    color = if (seleccionada) MoradoClinica
                    else Color(0xFF55515B),
                    shape = CircleShape
                )
        )

        Spacer(Modifier.width(24.dp))

        Text(
            text = texto,
            fontSize = 15.sp,
            fontWeight = if (seleccionada) FontWeight.Bold
            else FontWeight.Normal,
            color = if (seleccionada) MoradoClinica
            else Color(0xFF444047)
        )
    }
}

// Mensaje temporal para comprobar que cada destino abre correctamente.
@Composable
fun MensajeSeccion(
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
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF292929)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = descripcion,
            fontSize = 14.sp,
            color = Color(0xFF817C86)
        )
    }
}