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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicaApp() {
    // Administramos la navegación y el menú lateral.
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Las pantallas comparten esta lista observable.
    val citas = remember {
        mutableStateListOf(
            Cita(
                id = 1,
                medico = medicosEjemplo[1],
                fecha = "Miércoles 15",
                hora = "3:00 pm",
                estado = "Completada"
            )
        )
    }

    var siguienteId by remember { mutableStateOf(2) }

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
        "confirmacion/{citaId}" -> "Confirmación"
        else -> "Clínica Salud+"
    }

    // El menú envuelve al Scaffold para aparecer sobre la pantalla.
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
                    HorizontalDivider(color = Color(0xFFE4E0E7))
                    Spacer(Modifier.height(12.dp))

                    destinos.forEach { (ruta, nombre) ->
                        OpcionMenu(
                            texto = nombre,
                            seleccionada = rutaActual == ruta,
                            alPulsar = {
                                scope.launch {
                                    drawerState.close()
                                }

                                // Evitamos acumular copias de las secciones.
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
                if (rutaActual == "confirmacion/{citaId}") {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsTopHeight(WindowInsets.statusBars)
                    )
                } else if (rutaActual == "inicio") {
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
                        // Las secciones abren el menú y los pasos internos permiten volver.
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
            // Aplicamos el espacio del Scaffold a todas las pantallas.
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

                    if (medico != null) {
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
                        AgendarScreen(
                            alConfirmar = { fecha, hora ->
                                // Unimos el médico elegido con los datos del formulario.
                                val nuevaCita = Cita(
                                    id = siguienteId,
                                    medico = medico,
                                    fecha = fecha,
                                    hora = hora
                                )

                                citas.add(nuevaCita)
                                siguienteId++

                                // Retiramos el formulario para evitar enviarlo otra vez al volver.
                                navController.navigate("confirmacion/${nuevaCita.id}") {
                                    popUpTo("inicio") {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            }
                        )
                    } else {
                        MensajeSeccion(
                            titulo = "Médico no encontrado",
                            descripcion = "Regresa al inicio y selecciona un médico."
                        )
                    }
                }

                composable(
                    route = "confirmacion/{citaId}",
                    arguments = listOf(
                        navArgument("citaId") {
                            type = NavType.IntType
                        }
                    )
                ) { entrada ->
                    val citaId = entrada.arguments?.getInt("citaId")
                    val cita = citas.firstOrNull {
                        it.id == citaId
                    }

                    if (cita != null) {
                        ConfirmacionScreen(
                            cita = cita,
                            alVerCitas = {
                                navController.navigate("citas") {
                                    popUpTo("inicio") {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            },
                            alInicio = {
                                navController.navigate("inicio") {
                                    popUpTo("inicio") {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            }
                        )
                    } else {
                        MensajeSeccion(
                            titulo = "Cita no encontrada",
                            descripcion = "Regresa al inicio para continuar."
                        )
                    }
                }

                composable("citas") {
                    CitasScreen(
                        citas = citas,
                        alCambiarEstado = { citaId, nuevoEstado ->
                            val indice = citas.indexOfFirst {
                                it.id == citaId
                            }

                            if (indice != -1) {
                                val estadoActual = citas[indice].estado

                                // Las citas completadas nunca cambian mediante estas acciones.
                                val cambioPermitido =
                                    (estadoActual == "Confirmada" &&
                                            nuevoEstado == "Cancelada") ||
                                            (estadoActual == "Cancelada" &&
                                                    nuevoEstado == "Confirmada")

                                if (cambioPermitido) {
                                    // Reemplazamos el elemento para que Compose actualice la pantalla.
                                    citas[indice] = citas[indice].copy(
                                        estado = nuevoEstado
                                    )
                                }
                            }
                        }
                    )
                }

                composable("historial") {
                    CitasScreen(
                        citas = citas.filter {
                            it.estado == "Completada"
                        },
                        esHistorial = true
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

// Reutilizamos este contenido para mostrar estados vacíos y errores.
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