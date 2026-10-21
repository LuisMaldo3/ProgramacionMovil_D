
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
import com.tecsup.huellitasvet.ui.dueno.MisCitasScreen
import com.tecsup.huellitasvet.ui.dueno.PedirCitaScreen
import com.tecsup.huellitasvet.ui.dueno.SplashScreen

import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.ui.veterinario.ServiciosScreen

import com.tecsup.huellitasvet.viewmodel.InicioViewModel
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel
import com.tecsup.huellitasvet.viewmodel.MisCitasViewModel
import com.tecsup.huellitasvet.viewmodel.PedirCitaViewModel
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel
import com.tecsup.huellitasvet.viewmodel.SesionViewModel
import com.tecsup.huellitasvet.viewmodel.VetViewModelFactory

import kotlinx.coroutines.launch

private const val FICHA_RUTA =
    "ficha_mascota/{mascotaId}"

private const val FORMULARIO_RUTA =
    "formulario_mascota"

private const val EDITAR_RUTA =
    "editar_mascota/{mascotaId}"

private const val PEDIR_CITA_RUTA =
    "pedir_cita?mascotaId={mascotaId}"


@Composable
fun NavVet(app: VetApp) {

    val navController = rememberNavController()

    val scope = rememberCoroutineScope()

    val preferencias = remember(app) {
        PreferenciasStore(app.applicationContext)
    }

    // FACTORY DE SESION

    val sesionFactory = remember(app) {

        object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {

                if (
                    modelClass.isAssignableFrom(
                        SesionViewModel::class.java
                    )
                ) {

                    return SesionViewModel(
                        app.repository,
                        preferencias
                    ) as T
                }

                throw IllegalArgumentException(
                    "ViewModel no registrado: ${modelClass.name}"
                )
            }
        }
    }

    val sesionVm: SesionViewModel = viewModel(
        factory = sesionFactory
    )

    val usuarioActual by
    sesionVm.usuarioActual.collectAsState()

    val usuarioGuardado by
    preferencias.usuarioId.collectAsState(
        initial = 0
    )

    val nombreGuardado by
    preferencias.nombreUsuario.collectAsState(
        initial = ""
    )

    val idDueno = usuarioActual?.id ?: usuarioGuardado

    // RUTAS SEGUN ROL

    fun rutaPerfil(rol: Rol): String {

        return if (rol == Rol.CLIENTE) {
            Rutas.INICIO
        } else {
            Rutas.PANEL
        }
    }

    // FACTORY DE INICIO

    fun crearFactoryDueno(
        duenoId: Int
    ): ViewModelProvider.Factory {

        return object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {

                if (
                    modelClass.isAssignableFrom(
                        InicioViewModel::class.java
                    )
                ) {

                    return InicioViewModel(
                        app.repository,
                        duenoId
                    ) as T
                }

                throw IllegalArgumentException(
                    "ViewModel no registrado: ${modelClass.name}"
                )
            }
        }
    }

    // FACTORY DE MASCOTAS

    fun crearFactoryMascota(
        duenoId: Int,
        mascotaId: Int? = null
    ): ViewModelProvider.Factory {

        return object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {

                if (
                    modelClass.isAssignableFrom(
                        MascotaViewModel::class.java
                    )
                ) {

                    return MascotaViewModel(
                        app.repository,
                        duenoId,
                        mascotaId
                    ) as T
                }

                throw IllegalArgumentException(
                    "ViewModel no registrado: ${modelClass.name}"
                )
            }
        }
    }

    // FACTORY DE MIS CITAS - PASO 10

    fun crearFactoryMisCitas(
        duenoId: Int
    ): ViewModelProvider.Factory {

        return object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {

                if (
                    modelClass.isAssignableFrom(
                        MisCitasViewModel::class.java
                    )
                ) {

                    return MisCitasViewModel(
                        repo = app.repository,
                        duenoId = duenoId
                    ) as T
                }

                throw IllegalArgumentException(
                    "ViewModel no registrado: ${modelClass.name}"
                )
            }
        }
    }

    // NAVEGACION POR PESTANAS

    fun irTab(ruta: String) {

        navController.navigate(ruta) {

            popUpTo(Rutas.INICIO) {
                saveState = true
            }

            launchSingleTop = true
            restoreState = true
        }
    }

    // REGRESAR A LISTA DE MASCOTAS

    fun irListaMascotas() {

        navController.navigate(Rutas.MASCOTAS) {

            popUpTo(Rutas.INICIO) {
                inclusive = false
            }

            launchSingleTop = true
        }
    }

    // CERRAR SESION

    fun salir() {

        scope.launch {

            sesionVm.cerrarSesion()

            navController.navigate(Rutas.LOGIN) {

                popUpTo(navController.graph.id) {
                    inclusive = false
                }

                launchSingleTop = true
            }
        }
    }

    // NAVEGACION PRINCIPAL

    NavHost(
        navController = navController,
        startDestination = Rutas.SPLASH,
        modifier = Modifier.fillMaxSize()
    ) {

        // SPLASH

        composable(Rutas.SPLASH) {

            VetTheme(rol = Rol.CLIENTE) {

                SplashScreen(
                    onContinuar = {

                        scope.launch {

                            val guardado =
                                sesionVm.recuperarRolGuardado()

                            val destino = guardado?.let {
                                rutaPerfil(it)
                            } ?: Rutas.LOGIN

                            navController.navigate(destino) {

                                popUpTo(Rutas.SPLASH) {
                                    inclusive = true
                                }

                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }

        // LOGIN

        composable(Rutas.LOGIN) {

            LoginScreen(
                vm = sesionVm,
                onAcceso = { rol ->

                    navController.navigate(
                        rutaPerfil(rol)
                    ) {

                        popUpTo(Rutas.LOGIN) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }

        // INICIO DEL DUENO

        composable(Rutas.INICIO) {

            VetTheme(rol = Rol.CLIENTE) {

                if (idDueno > 0) {

                    val inicioVm: InicioViewModel = viewModel(
                        key = "inicio_dueno_$idDueno",

                        factory = remember(app, idDueno) {
                            crearFactoryDueno(idDueno)
                        }
                    )

                    InicioScreen(
                        vm = inicioVm,

                        nombreDueno = (
                                usuarioActual?.nombre ?: nombreGuardado
                                ).substringBefore(' ')
                            .ifBlank { "bienvenido" },

                        onVerMascotas = {
                            irTab(Rutas.MASCOTAS)
                        },

                        onMascota = { mascotaId ->
                            navController.navigate(
                                "ficha_mascota/$mascotaId"
                            )
                        },

                        onAgregarMascota = {
                            navController.navigate(
                                FORMULARIO_RUTA
                            )
                        },

                        onPedirCita = {
                            navController.navigate(
                                Rutas.PEDIR_CITA
                            )
                        },

                        onSeleccionarTab = { ruta ->
                            irTab(ruta)
                        }
                    )

                } else {

                    EsperandoSesion()
                }
            }
        }

        // LISTA DE MASCOTAS

        composable(Rutas.MASCOTAS) {

            VetTheme(rol = Rol.CLIENTE) {

                if (idDueno > 0) {

                    val vm: InicioViewModel = viewModel(
                        key = "mascotas_dueno_$idDueno",

                        factory = remember(app, idDueno) {
                            crearFactoryDueno(idDueno)
                        }
                    )

                    MascotasScreen(
                        vm = vm,

                        onMascota = { mascotaId ->
                            navController.navigate(
                                "ficha_mascota/$mascotaId"
                            )
                        },

                        onAgregarMascota = {
                            navController.navigate(
                                FORMULARIO_RUTA
                            )
                        },

                        onSeleccionarTab = { ruta ->
                            irTab(ruta)
                        }
                    )

                } else {

                    EsperandoSesion()
                }
            }
        }

        // FICHA DE MASCOTA - PASO 8

        composable(
            route = FICHA_RUTA,

            arguments = listOf(
                navArgument("mascotaId") {
                    type = NavType.IntType
                }
            )
        ) { entry ->

            VetTheme(rol = Rol.CLIENTE) {

                val mascotaId =
                    entry.arguments?.getInt("mascotaId") ?: 0

                if (idDueno > 0 && mascotaId > 0) {

                    val vm: MascotaViewModel = viewModel(
                        key = "ficha_${idDueno}_$mascotaId",

                        factory = remember(
                            app,
                            idDueno,
                            mascotaId
                        ) {
                            crearFactoryMascota(
                                idDueno,
                                mascotaId
                            )
                        }
                    )

                    FichaMascotaScreen(
                        vm = vm,
                        mascotaId = mascotaId,

                        onVolver = {
                            navController.popBackStack()
                        },

                        onEditar = {
                            navController.navigate(
                                "editar_mascota/$mascotaId"
                            )
                        },

                        onPedirCita = {
                            navController.navigate(
                                "${Rutas.PEDIR_CITA}?mascotaId=$mascotaId"
                            )
                        },

                        onSeleccionarTab = { ruta ->
                            irTab(ruta)
                        },

                        onEliminada = {
                            irListaMascotas()
                        }
                    )

                } else {

                    EsperandoSesion()
                }
            }
        }

        // REGISTRAR MASCOTA - PASO 8

        composable(FORMULARIO_RUTA) {

            VetTheme(rol = Rol.CLIENTE) {

                if (idDueno > 0) {

                    val vm: MascotaViewModel = viewModel(
                        key = "nueva_mascota_$idDueno",

                        factory = remember(app, idDueno) {
                            crearFactoryMascota(idDueno)
                        }
                    )

                    FormularioMascotaScreen(
                        vm = vm,

                        onVolver = {
                            navController.popBackStack()
                        },

                        onGuardado = {
                            irListaMascotas()
                        }
                    )

                } else {

                    EsperandoSesion()
                }
            }
        }

        // EDITAR MASCOTA - PASO 8

        composable(
            route = EDITAR_RUTA,

            arguments = listOf(
                navArgument("mascotaId") {
                    type = NavType.IntType
                }
            )
        ) { entry ->

            VetTheme(rol = Rol.CLIENTE) {

                val mascotaId =
                    entry.arguments?.getInt("mascotaId") ?: 0

                if (idDueno > 0 && mascotaId > 0) {

                    val vm: MascotaViewModel = viewModel(
                        key = "editar_${idDueno}_$mascotaId",

                        factory = remember(
                            app,
                            idDueno,
                            mascotaId
                        ) {
                            crearFactoryMascota(
                                idDueno,
                                mascotaId
                            )
                        }
                    )

                    FormularioMascotaScreen(
                        vm = vm,

                        onVolver = {
                            navController.popBackStack()
                        },

                        onGuardado = {
                            navController.popBackStack()
                        }
                    )

                } else {

                    EsperandoSesion()
                }
            }
        }

        // PEDIR CITA - PASO 9

        composable(
            route = PEDIR_CITA_RUTA,

            arguments = listOf(
                navArgument("mascotaId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { entry ->

            VetTheme(rol = Rol.CLIENTE) {

                if (idDueno > 0) {

                    val inicialId =
                        entry.arguments?.getInt("mascotaId")
                            ?.takeIf { it > 0 }

                    val vm: PedirCitaViewModel = viewModel(
                        key = "pedir_cita_${idDueno}_${inicialId ?: 0}",

                        factory = remember(
                            app,
                            idDueno,
                            inicialId
                        ) {

                            object : ViewModelProvider.Factory {

                                @Suppress("UNCHECKED_CAST")
                                override fun <T : ViewModel> create(
                                    modelClass: Class<T>
                                ): T {

                                    if (
                                        modelClass.isAssignableFrom(
                                            PedirCitaViewModel::class.java
                                        )
                                    ) {

                                        return PedirCitaViewModel(
                                            app.repository,
                                            idDueno,
                                            inicialId
                                        ) as T
                                    }

                                    throw IllegalArgumentException(
                                        "ViewModel desconocido"
                                    )
                                }
                            }
                        }
                    )

                    PedirCitaScreen(
                        vm = vm,

                        onVolver = {
                            navController.popBackStack()
                        },

                        onConfirmada = {

                            navController.navigate(
                                Rutas.MIS_CITAS
                            ) {

                                popUpTo(Rutas.INICIO) {
                                    inclusive = false
                                }

                                launchSingleTop = true
                            }
                        }
                    )

                } else {

                    EsperandoSesion()
                }
            }
        }

        // MIS CITAS - PASO 10

        composable(Rutas.MIS_CITAS) {

            VetTheme(rol = Rol.CLIENTE) {

                if (idDueno > 0) {

                    val vm: MisCitasViewModel = viewModel(
                        key = "mis_citas_dueno_$idDueno",

                        factory = remember(app, idDueno) {
                            crearFactoryMisCitas(idDueno)
                        }
                    )

                    MisCitasScreen(
                        vm = vm,

                        onVolver = {

                            if (!navController.popBackStack()) {
                                irTab(Rutas.INICIO)
                            }
                        },

                        onPedirCita = {
                            navController.navigate(
                                Rutas.PEDIR_CITA
                            )
                        },

                        onSeleccionarTab = { ruta ->
                            irTab(ruta)
                        }
                    )

                } else {

                    EsperandoSesion()
                }
            }
        }

        // PERFIL DEL DUENO
        // SE COMPLETARA EN EL PASO 13

        composable(Rutas.PERFIL) {

            VetTheme(rol = Rol.CLIENTE) {

                PantallaProximoPaso(
                    titulo = "Mi perfil",
                    descripcion = "Se completará en el Paso 13",

                    onVolver = {
                        navController.popBackStack()
                    },

                    onCerrarSesion = {
                        salir()
                    }
                )
            }
        }

        // PANEL VETERINARIO
        // SE COMPLETARA EN EL PASO 11

        composable(Rutas.PANEL) {

            VetTheme(rol = Rol.ADMIN) {

                PantallaProximoPaso(
                    titulo = "Panel veterinario",
                    descripcion = "Se completará en el Paso 11",

                    onVolver = {
                        salir()
                    },

                    onServicios = {
                        navController.navigate(
                            Rutas.SERVICIOS
                        )
                    },

                    onCerrarSesion = {
                        salir()
                    }
                )
            }
        }

        // SERVICIOS DEL VETERINARIO

        composable(Rutas.SERVICIOS) {

            VetTheme(rol = Rol.ADMIN) {

                val vm: ServiciosViewModel = viewModel(
                    factory = VetViewModelFactory(
                        app.repository
                    )
                )

                ServiciosScreen(
                    vm = vm,

                    onEditar = {
                        // Paso 13
                    }
                )
            }
        }
    }
}

// PANTALLA DE ESPERA

@Composable
private fun EsperandoSesion() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        CircularProgressIndicator()
    }
}

// PANTALLA PROVISIONAL DE PASOS FUTUROS

@Composable
private fun PantallaProximoPaso(
    titulo: String,
    descripcion: String,
    onVolver: () -> Unit,
    onServicios: (() -> Unit)? = null,
    onCerrarSesion: (() -> Unit)? = null
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = descripcion,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (onServicios != null) {

            Button(
                onClick = onServicios,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Ver servicios")
            }
        }

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Volver")
        }

        if (onCerrarSesion != null) {

            TextButton(
                onClick = onCerrarSesion
            ) {

                Text("Cerrar sesión")
            }
        }
    }
}
