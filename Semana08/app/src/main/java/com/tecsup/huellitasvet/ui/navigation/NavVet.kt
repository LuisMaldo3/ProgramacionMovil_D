package com.tecsup.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.tecsup.huellitasvet.ui.dueno.ReprogramarCitaScreen
import com.tecsup.huellitasvet.ui.dueno.MisCitasScreen
import com.tecsup.huellitasvet.ui.dueno.PerfilScreen
import com.tecsup.huellitasvet.ui.dueno.PedirCitaScreen
import com.tecsup.huellitasvet.ui.dueno.MascotasScreen
import com.tecsup.huellitasvet.ui.dueno.SplashScreen
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.ui.veterinario.ServiciosScreen
import com.tecsup.huellitasvet.ui.veterinario.FormularioServicioScreen
import com.tecsup.huellitasvet.ui.veterinario.AjustesScreen
import com.tecsup.huellitasvet.ui.veterinario.PanelScreen
import com.tecsup.huellitasvet.ui.veterinario.AgendaScreen
import com.tecsup.huellitasvet.ui.veterinario.AtenderCitaScreen
import com.tecsup.huellitasvet.ui.veterinario.PacientesScreen
import com.tecsup.huellitasvet.ui.veterinario.FichaPacienteScreen
import com.tecsup.huellitasvet.ui.veterinario.BarraNavegacionVeterinario
import com.tecsup.huellitasvet.viewmodel.InicioViewModel
import com.tecsup.huellitasvet.viewmodel.PanelViewModel
import com.tecsup.huellitasvet.viewmodel.AgendaViewModel
import com.tecsup.huellitasvet.viewmodel.AtenderCitaViewModel
import com.tecsup.huellitasvet.viewmodel.PacientesViewModel
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel
import com.tecsup.huellitasvet.viewmodel.ReprogramarCitaViewModel
import com.tecsup.huellitasvet.viewmodel.MisCitasViewModel
import com.tecsup.huellitasvet.viewmodel.PedirCitaViewModel
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel
import com.tecsup.huellitasvet.viewmodel.SesionViewModel
import com.tecsup.huellitasvet.viewmodel.VetViewModelFactory
import kotlinx.coroutines.launch

private const val FICHA_RUTA = "ficha_mascota/{mascotaId}"
private const val FORMULARIO_RUTA = "formulario_mascota"
private const val EDITAR_RUTA = "editar_mascota/{mascotaId}"
private const val REPROGRAMAR_CITA_RUTA = "reprogramar_cita/{citaId}"
private const val PEDIR_CITA_RUTA = "pedir_cita?mascotaId={mascotaId}"
private const val ATENDER_RUTA = "atender_cita/{citaId}"
private const val FICHA_PACIENTE_RUTA = "ficha_paciente/{mascotaId}"
private const val EDITAR_SERVICIO_RUTA = "editar_servicio/{servicioId}"
private const val NUEVO_SERVICIO_RUTA = "nuevo_servicio"

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
    val correoGuardado by preferencias.correoUsuario.collectAsState(initial = "")
    val oscuro by preferencias.modoOscuro.collectAsState(initial = false)
    var errorCambioPerfil by remember { mutableStateOf<String?>(null) }
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

    // Navegación independiente para el perfil veterinario.
    fun irTabVet(ruta: String) {
        navController.navigate(ruta) {
            popUpTo(Rutas.PANEL) { saveState = true }
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

    // Cambio directo solo en el modo práctica: usa las cuentas ficticias del PDF.
    fun cambiarPerfil(destino: Rol) {
        scope.launch {
            if (sesionVm.cambiarPerfilPractica(destino)) {
                navController.navigate(rutaPerfil(destino)) {
                    popUpTo(navController.graph.id) { inclusive = false }
                    launchSingleTop = true
                }
            } else {
                errorCambioPerfil = sesionVm.error.value ?: "No se pudo cambiar el perfil"
            }
        }
    }

    NavHost(navController = navController, startDestination = Rutas.SPLASH, modifier = Modifier.fillMaxSize()) {
        composable(Rutas.SPLASH) {
            VetTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
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
            VetTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
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
            VetTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
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
            VetTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
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
                        onPedirCita = { navController.navigate("${Rutas.PEDIR_CITA}?mascotaId=$mascotaId") },
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
            VetTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
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
            VetTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
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

        // PASO 9: seleccion, horarios de Room y confirmacion real.
        composable(
            route = PEDIR_CITA_RUTA,
            arguments = listOf(navArgument("mascotaId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { entry ->
            VetTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
                if (idDueno > 0) {
                    val inicialId = entry.arguments?.getInt("mascotaId")
                        ?.takeIf { it > 0 }
                    val vm: PedirCitaViewModel = viewModel(
                        key = "pedir_cita_${idDueno}_${inicialId ?: 0}",
                        factory = remember(app, idDueno, inicialId) {
                            object : ViewModelProvider.Factory {
                                @Suppress("UNCHECKED_CAST")
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    if (modelClass.isAssignableFrom(PedirCitaViewModel::class.java)) {
                                        return PedirCitaViewModel(
                                            app.repository, idDueno, inicialId, preferencias
                                        ) as T
                                    }
                                    throw IllegalArgumentException("ViewModel desconocido")
                                }
                            }
                        }
                    )
                    PedirCitaScreen(
                        vm = vm,
                        onVolver = { navController.popBackStack() },
                        onConfirmada = {
                            navController.navigate(Rutas.MIS_CITAS) {
                                popUpTo(Rutas.INICIO) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    )
                } else {
                    EsperandoSesion()
                }
            }
        }
        // PASO 10: Mis citas reales, historial y cancelacion con regla de 2 horas.
        composable(Rutas.MIS_CITAS) {
            VetTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
                if (idDueno > 0) {
                    val vm: MisCitasViewModel = viewModel(
                        key = "mis_citas_dueno_$idDueno",
                        factory = remember(app, idDueno) {
                            object : ViewModelProvider.Factory {
                                @Suppress("UNCHECKED_CAST")
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    if (modelClass.isAssignableFrom(MisCitasViewModel::class.java)) {
                                        return MisCitasViewModel(app.repository, idDueno) as T
                                    }
                                    throw IllegalArgumentException("ViewModel desconocido")
                                }
                            }
                        }
                    )
                    MisCitasScreen(
                        vm = vm,
                        onVolver = {
                            if (!navController.popBackStack()) irTab(Rutas.INICIO)
                        },
                        onPedirCita = { navController.navigate(Rutas.PEDIR_CITA) },
                        onSeleccionarTab = { irTab(it) },
                        onReprogramar = { citaId -> navController.navigate("reprogramar_cita/$citaId") }
                    )
                } else {
                    EsperandoSesion()
                }
            }
        }
        // FASE 2 - reprogramacion: nuevo destino, preservando ID, precio y estado de la cita.
        composable(
            route = REPROGRAMAR_CITA_RUTA,
            arguments = listOf(navArgument("citaId") { type = NavType.IntType })
        ) { entry ->
            VetTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
                val citaId = entry.arguments?.getInt("citaId") ?: 0
                if (idDueno > 0 && citaId > 0) {
                    val vm: ReprogramarCitaViewModel = viewModel(
                        key = "reprogramar_${idDueno}_$citaId",
                        factory = remember(app, idDueno, citaId) {
                            object : ViewModelProvider.Factory {
                                @Suppress("UNCHECKED_CAST")
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    if (modelClass.isAssignableFrom(ReprogramarCitaViewModel::class.java)) {
                                        return ReprogramarCitaViewModel(app.repository, idDueno, citaId) as T
                                    }
                                    throw IllegalArgumentException("ViewModel desconocido")
                                }
                            }
                        }
                    )
                    ReprogramarCitaScreen(
                        vm = vm,
                        onVolver = { navController.popBackStack() },
                        onGuardada = { navController.popBackStack() }
                    )
                } else EsperandoSesion()
            }
        }
        // PASO 13: perfil y preferencias del dueño.
        composable(Rutas.PERFIL) {
            VetTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
                if (idDueno > 0) {
                    PerfilScreen(
                        duenoId = idDueno,
                        nombre = usuarioActual?.nombre ?: nombreGuardado,
                        correo = usuarioActual?.correo ?: correoGuardado,
                        repo = app.repository,
                        preferencias = preferencias,
                        onCambiarPerfil = { cambiarPerfil(Rol.ADMIN) },
                        onCerrarSesion = { salir() },
                        onVolver = { if (!navController.popBackStack()) irTab(Rutas.INICIO) },
                        onSeleccionarTab = { irTab(it) }
                    )
                } else EsperandoSesion()
            }
        }
        // PASO 11 - PANEL ADMINISTRADOR
        composable(Rutas.PANEL) {
            VetTheme(rol = Rol.ADMIN, oscuro = oscuro) {
                val vm: PanelViewModel = viewModel(
                    factory = remember(app) {
                        object : ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return PanelViewModel(app.repository) as T
                            }
                        }
                    }
                )
                PanelScreen(
                    vm = vm,
                    onVerAgenda = { navController.navigate(Rutas.AGENDA) },
                    onSeleccionarTab = { irTabVet(it) }
                )
            }
        }
        // PASO 11 - AGENDA VETERINARIO
        composable(Rutas.AGENDA) {
            VetTheme(rol = Rol.ADMIN, oscuro = oscuro) {
                val vm: AgendaViewModel = viewModel(
                    factory = remember(app) {
                        object : ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return AgendaViewModel(app.repository) as T
                            }
                        }
                    }
                )
                AgendaScreen(
                    vm = vm,
                    onVolver = { if (!navController.popBackStack()) irTabVet(Rutas.PANEL) },
                    onAtender = { citaId -> navController.navigate("atender_cita/$citaId") },
                    onSeleccionarTab = { irTabVet(it) }
                )
            }
        }
        // PASO 12: registro de atención clínica y actualización de peso.
        composable(
            route = ATENDER_RUTA,
            arguments = listOf(navArgument("citaId") { type = NavType.IntType })
        ) { entry ->
            VetTheme(rol = Rol.ADMIN, oscuro = oscuro) {
                val citaId = entry.arguments?.getInt("citaId") ?: 0
                if (citaId > 0) {
                    val vm: AtenderCitaViewModel = viewModel(
                        key = "atender_cita_$citaId",
                        factory = remember(app, citaId) {
                            object : ViewModelProvider.Factory {
                                @Suppress("UNCHECKED_CAST")
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    if (modelClass.isAssignableFrom(AtenderCitaViewModel::class.java)) {
                                        return AtenderCitaViewModel(app.repository, citaId) as T
                                    }
                                    throw IllegalArgumentException("ViewModel no registrado")
                                }
                            }
                        }
                    )
                    AtenderCitaScreen(
                        vm = vm,
                        onVolver = { navController.popBackStack() },
                        onFinalizada = {
                            navController.navigate(Rutas.AGENDA) {
                                popUpTo(Rutas.AGENDA) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                } else {
                    EsperandoSesion()
                }
            }
        }

        // PASO 12: listado de pacientes con búsqueda y filtros.
        composable(Rutas.PACIENTES) {
            VetTheme(rol = Rol.ADMIN, oscuro = oscuro) {
                val vm: PacientesViewModel = viewModel(
                    key = "pacientes_veterinario",
                    factory = remember(app) {
                        object : ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                if (modelClass.isAssignableFrom(PacientesViewModel::class.java)) {
                                    return PacientesViewModel(app.repository) as T
                                }
                                throw IllegalArgumentException("ViewModel no registrado")
                            }
                        }
                    }
                )
                PacientesScreen(
                    vm = vm,
                    onVolver = { if (!navController.popBackStack()) irTabVet(Rutas.PANEL) },
                    onPaciente = { mascotaId ->
                        navController.navigate("ficha_paciente/$mascotaId")
                    },
                    onSeleccionarTab = { irTabVet(it) }
                )
            }
        }

        // Ficha clínica de consulta exclusiva para el veterinario.
        composable(
            route = FICHA_PACIENTE_RUTA,
            arguments = listOf(navArgument("mascotaId") { type = NavType.IntType })
        ) { entry ->
            VetTheme(rol = Rol.ADMIN, oscuro = oscuro) {
                val mascotaId = entry.arguments?.getInt("mascotaId") ?: 0
                val vm: PacientesViewModel = viewModel(
                    key = "ficha_paciente_vet_$mascotaId",
                    factory = remember(app, mascotaId) {
                        object : ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                if (modelClass.isAssignableFrom(PacientesViewModel::class.java)) {
                                    return PacientesViewModel(app.repository) as T
                                }
                                throw IllegalArgumentException("ViewModel no registrado")
                            }
                        }
                    }
                )
                FichaPacienteScreen(
                    vm = vm,
                    mascotaId = mascotaId,
                    onVolver = { navController.popBackStack() }
                )
            }
        }
        // PASO 13: ajustes del veterinario.
        composable(Rutas.AJUSTES) {
            VetTheme(rol = Rol.ADMIN, oscuro = oscuro) {
                AjustesScreen(
                    nombre = usuarioActual?.nombre ?: nombreGuardado,
                    correo = usuarioActual?.correo ?: correoGuardado,
                    preferencias = preferencias,
                    onCambiarPerfil = { cambiarPerfil(Rol.CLIENTE) },
                    onCerrarSesion = { salir() },
                    onVolver = { if (!navController.popBackStack()) irTabVet(Rutas.PANEL) },
                    onSeleccionarTab = { irTabVet(it) }
                )
            }
        }
        // PASO 13: listado de servicios con agregar y editar.
        composable(Rutas.SERVICIOS) {
            VetTheme(rol = Rol.ADMIN, oscuro = oscuro) {
                val vm: ServiciosViewModel = viewModel(factory = VetViewModelFactory(app.repository))
                ServiciosScreen(
                    vm = vm,
                    onEditar = { id -> navController.navigate("editar_servicio/$id") },
                    onAgregar = { navController.navigate(NUEVO_SERVICIO_RUTA) },
                    onVolver = { if (!navController.popBackStack()) irTabVet(Rutas.PANEL) },
                    onSeleccionarTab = { irTabVet(it) }
                )
            }
        }

        composable(NUEVO_SERVICIO_RUTA) {
            VetTheme(rol = Rol.ADMIN, oscuro = oscuro) {
                val vm: ServiciosViewModel = viewModel(factory = VetViewModelFactory(app.repository))
                FormularioServicioScreen(
                    vm = vm,
                    servicioId = null,
                    onVolver = { navController.popBackStack() },
                    onGuardado = { navController.popBackStack() }
                )
            }
        }
        composable(
            route = EDITAR_SERVICIO_RUTA,
            arguments = listOf(navArgument("servicioId") { type = NavType.IntType })
        ) { entry ->
            VetTheme(rol = Rol.ADMIN, oscuro = oscuro) {
                val servicioId = entry.arguments?.getInt("servicioId") ?: 0
                val vm: ServiciosViewModel = viewModel(factory = VetViewModelFactory(app.repository))
                FormularioServicioScreen(
                    vm = vm,
                    servicioId = servicioId,
                    onVolver = { navController.popBackStack() },
                    onGuardado = { navController.popBackStack() }
                )
            }
        }

    }
    errorCambioPerfil?.let { aviso ->
        AlertDialog(
            onDismissRequest = { errorCambioPerfil = null },
            title = { Text("Cambiar de perfil") },
            text = { Text(aviso) },
            confirmButton = {
                TextButton(onClick = { errorCambioPerfil = null }) { Text("Entendido") }
            }
        )
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

