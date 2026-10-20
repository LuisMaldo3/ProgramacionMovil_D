package com.tecsup.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.huellitasvet.VetApp
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.dueno.FichaMascotaScreen
import com.tecsup.huellitasvet.ui.dueno.FormularioMascotaScreen
import com.tecsup.huellitasvet.ui.dueno.InicioScreen
import com.tecsup.huellitasvet.ui.dueno.LoginScreen
import com.tecsup.huellitasvet.ui.dueno.MascotasScreen
import com.tecsup.huellitasvet.ui.dueno.SplashScreen
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.ui.veterinario.ServiciosScreen
import com.tecsup.huellitasvet.viewmodel.InicioViewModel
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel
import com.tecsup.huellitasvet.viewmodel.SesionViewModel
import com.tecsup.huellitasvet.viewmodel.VetViewModelFactory
import kotlinx.coroutines.launch

private const val FICHA_RUTA = "ficha_mascota/{mascotaId}"
private const val FORMULARIO_RUTA = "formulario_mascota"
private const val EDITAR_RUTA = "editar_mascota/{mascotaId}"

@Composable
fun NavVet(app: VetApp) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val preferencias = remember(app) { PreferenciasStore(app.applicationContext) }

    val sesionFactory = remember(app) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SesionViewModel::class.java)) {
                    return SesionViewModel(app.repository, preferencias) as T
                }
                throw IllegalArgumentException("ViewModel no registrado: ${modelClass.name}")
            }
        }
    }
    val sesionVm: SesionViewModel = viewModel(factory = sesionFactory)
    val usuarioActual by sesionVm.usuarioActual.collectAsState()
    val usuarioGuardado by preferencias.usuarioId.collectAsState(initial = 0)
    val nombreGuardado by preferencias.nombreUsuario.collectAsState(initial = "")
    val idDueno = usuarioActual?.id ?: usuarioGuardado

    fun rutaPerfil(rol: Rol): String =
        if (rol == Rol.CLIENTE) Rutas.INICIO else Rutas.PANEL

    fun crearFactoryDueno(duenoId: Int): ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(InicioViewModel::class.java)) {
                    return InicioViewModel(app.repository, duenoId) as T
                }
                throw IllegalArgumentException("ViewModel no registrado: ${modelClass.name}")
            }
        }

    // Paso 8: para crear pasa mascotaId = null; para ficha o editar pasa su ID real.
    fun crearFactoryMascota(duenoId: Int, mascotaId: Int? = null): ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MascotaViewModel::class.java)) {
                    return MascotaViewModel(app.repository, duenoId, mascotaId) as T
                }
                throw IllegalArgumentException("ViewModel no registrado: ${modelClass.name}")
            }
        }

    fun irTab(ruta: String) {
        navController.navigate(ruta) {
            popUpTo(Rutas.INICIO) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    // Quita formularios del stack al guardar o eliminar; Room actualiza las listas con Flow.
    fun irListaMascotas() {
        navController.navigate(Rutas.MASCOTAS) {
            popUpTo(Rutas.INICIO) { inclusive = false }
            launchSingleTop = true
        }
    }

    fun salir() {
        scope.launch {
            sesionVm.cerrarSesion()
            navController.navigate(Rutas.LOGIN) {
                popUpTo(navController.graph.id) { inclusive = false }
                launchSingleTop = true
            }
        }
    }

    NavHost(navController = navController, startDestination = Rutas.SPLASH) {
        composable(Rutas.SPLASH) {
            VetTheme(rol = Rol.CLIENTE) {
                SplashScreen(onContinuar = {
                    scope.launch {
                        val guardado = sesionVm.recuperarRolGuardado()
                        navController.navigate(guardado?.let { rutaPerfil(it) } ?: Rutas.LOGIN) {
                            popUpTo(Rutas.SPLASH) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                })
            }
        }
        composable(Rutas.LOGIN) {
            LoginScreen(vm = sesionVm, onAcceso = { rol ->
                navController.navigate(rutaPerfil(rol)) {
                    popUpTo(Rutas.LOGIN) { inclusive = true }
                    launchSingleTop = true
                }
            })
        }
        composable(Rutas.INICIO) {
            VetTheme(rol = Rol.CLIENTE) {
                if (idDueno > 0) {
                    val inicioVm: InicioViewModel = viewModel(
                        key = "inicio_dueno_$idDueno",
                        factory = remember(app, idDueno) { crearFactoryDueno(idDueno) }
                    )
                    InicioScreen(
                        vm = inicioVm,
                        nombreDueno = (usuarioActual?.nombre ?: nombreGuardado)
                            .substringBefore(' ').ifBlank { "bienvenido" },
                        onVerMascotas = { irTab(Rutas.MASCOTAS) },
                        onMascota = { navController.navigate("ficha_mascota/$it") },
                        onAgregarMascota = { navController.navigate(FORMULARIO_RUTA) },
                        onPedirCita = { navController.navigate(Rutas.PEDIR_CITA) },
                        onSeleccionarTab = { irTab(it) }
                    )
                } else {
                    EsperandoSesion()
                }
            }
        }
        composable(Rutas.MASCOTAS) {
            VetTheme(rol = Rol.CLIENTE) {
                if (idDueno > 0) {
                    val vm: InicioViewModel = viewModel(
                        key = "mascotas_dueno_$idDueno",
                        factory = remember(app, idDueno) { crearFactoryDueno(idDueno) }
                    )
                    MascotasScreen(
                        vm = vm,
                        onMascota = { navController.navigate("ficha_mascota/$it") },
                        onAgregarMascota = { navController.navigate(FORMULARIO_RUTA) },
                        onSeleccionarTab = { irTab(it) }
                    )
                } else {
                    EsperandoSesion()
                }
            }
        }

        // Paso 8: ficha real de la mascota (antes era una pantalla provisional).
        composable(
            route = FICHA_RUTA,
            arguments = listOf(navArgument("mascotaId") { type = NavType.IntType })
        ) { entry ->
            VetTheme(rol = Rol.CLIENTE) {
                val mascotaId = entry.arguments?.getInt("mascotaId") ?: 0
                if (idDueno > 0 && mascotaId > 0) {
                    val vm: MascotaViewModel = viewModel(
                        key = "ficha_${idDueno}_$mascotaId",
                        factory = remember(app, idDueno, mascotaId) {
                            crearFactoryMascota(idDueno, mascotaId)
                        }
                    )
                    FichaMascotaScreen(
                        vm = vm,
                        mascotaId = mascotaId,
                        onVolver = { navController.popBackStack() },
                        onEditar = { navController.navigate("editar_mascota/$mascotaId") },
                        onPedirCita = { navController.navigate(Rutas.PEDIR_CITA) },
                        onSeleccionarTab = { irTab(it) },
                        onEliminada = { irListaMascotas() }
                    )
                } else {
                    EsperandoSesion()
                }
            }
        }

        // Paso 8: formulario REAL para crear una mascota.
        composable(FORMULARIO_RUTA) {
            VetTheme(rol = Rol.CLIENTE) {
                if (idDueno > 0) {
                    val vm: MascotaViewModel = viewModel(
                        key = "nueva_mascota_$idDueno",
                        factory = remember(app, idDueno) { crearFactoryMascota(idDueno) }
                    )
                    FormularioMascotaScreen(
                        vm = vm,
                        onVolver = { navController.popBackStack() },
                        onGuardado = { irListaMascotas() }
                    )
                } else {
                    EsperandoSesion()
                }
            }
        }

        // Paso 8: reutiliza el mismo formulario para editar (sin insertar duplicados).
        composable(
            route = EDITAR_RUTA,
            arguments = listOf(navArgument("mascotaId") { type = NavType.IntType })
        ) { entry ->
            VetTheme(rol = Rol.CLIENTE) {
                val mascotaId = entry.arguments?.getInt("mascotaId") ?: 0
                if (idDueno > 0 && mascotaId > 0) {
                    val vm: MascotaViewModel = viewModel(
                        key = "editar_${idDueno}_$mascotaId",
                        factory = remember(app, idDueno, mascotaId) {
                            crearFactoryMascota(idDueno, mascotaId)
                        }
                    )
                    FormularioMascotaScreen(
                        vm = vm,
                        onVolver = { navController.popBackStack() },
                        // Retorna a la ficha: esta observa los cambios de Room con Flow.
                        onGuardado = { navController.popBackStack() }
                    )
                } else {
                    EsperandoSesion()
                }
            }
        }

        // Destinos de los pasos siguientes, aún provisionales.
        composable(Rutas.PEDIR_CITA) {
            VetTheme(rol = Rol.CLIENTE) {
                PantallaProximoPaso("Pedir una cita", "Paso 9 · Calendario y horarios",
                    onVolver = { navController.popBackStack() })
            }
        }
        composable(Rutas.MIS_CITAS) {
            VetTheme(rol = Rol.CLIENTE) {
                PantallaProximoPaso("Mis citas", "Paso 10 · Próximas e historial",
                    onVolver = { navController.popBackStack() })
            }
        }
        composable(Rutas.PERFIL) {
            VetTheme(rol = Rol.CLIENTE) {
                PantallaProximoPaso("Mi perfil", "Se completará en el Paso 13",
                    onVolver = { navController.popBackStack() }, onCerrarSesion = { salir() })
            }
        }
        composable(Rutas.PANEL) {
            VetTheme(rol = Rol.ADMIN) {
                PantallaProximoPaso(
                    "Panel veterinario", "Se completará en el Paso 11",
                    onVolver = { salir() }, onServicios = { navController.navigate(Rutas.SERVICIOS) },
                    onCerrarSesion = { salir() }
                )
            }
        }
        composable(Rutas.SERVICIOS) {
            VetTheme(rol = Rol.ADMIN) {
                val vm: ServiciosViewModel = viewModel(factory = VetViewModelFactory(app.repository))
                ServiciosScreen(vm = vm, onEditar = { /* Paso 13 */ })
            }
        }
    }
}

@Composable
private fun EsperandoSesion() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun PantallaProximoPaso(
    titulo: String,
    descripcion: String,
    onVolver: () -> Unit,
    onServicios: (() -> Unit)? = null,
    onCerrarSesion: (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(titulo, style = MaterialTheme.typography.headlineMedium)
        Text(descripcion, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (onServicios != null) {
            Button(onClick = onServicios, modifier = Modifier.fillMaxWidth()) { Text("Ver servicios") }
        }
        OutlinedButton(onClick = onVolver, modifier = Modifier.fillMaxWidth()) { Text("Volver") }
        if (onCerrarSesion != null) {
            TextButton(onClick = onCerrarSesion) { Text("Cerrar sesión") }
        }
    }
}
