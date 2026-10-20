
package com.tecsup.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.huellitasvet.VetApp
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.dueno.LoginScreen
import com.tecsup.huellitasvet.ui.dueno.SplashScreen
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.ui.veterinario.ServiciosScreen
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel
import com.tecsup.huellitasvet.viewmodel.SesionViewModel
import com.tecsup.huellitasvet.viewmodel.VetViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun NavVet(app: VetApp) {

    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    val factorySesion = remember(app) {
        object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {

                if (modelClass.isAssignableFrom(
                        SesionViewModel::class.java
                    )
                ) {
                    return SesionViewModel(
                        repo = app.repository,
                        preferencias = PreferenciasStore(
                            app.applicationContext
                        )
                    ) as T
                }

                throw IllegalArgumentException(
                    "ViewModel no registrado"
                )
            }
        }
    }

    val sesionVm: SesionViewModel = viewModel(
        factory = factorySesion
    )

    fun rutaDe(rol: Rol): String {
        return if (rol == Rol.CLIENTE) {
            Rutas.INICIO
        } else {
            Rutas.PANEL
        }
    }

    NavHost(
        navController = navController,
        startDestination = Rutas.SPLASH
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
                                rutaDe(it)
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
                        rutaDe(rol)
                    ) {
                        popUpTo(Rutas.LOGIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // INICIO DEL DUEÑO
        composable(Rutas.INICIO) {

            VetTheme(rol = Rol.CLIENTE) {

                PantallaTemporal(
                    titulo = "Inicio del dueño",
                    descripcion = "Se completará en el Paso 7",
                    onSalir = {
                        scope.launch {

                            sesionVm.cerrarSesion()

                            navController.navigate(Rutas.LOGIN) {
                                popUpTo(Rutas.INICIO) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                )
            }
        }

        // PANEL DEL VETERINARIO
        composable(Rutas.PANEL) {

            VetTheme(rol = Rol.ADMIN) {

                PantallaTemporal(
                    titulo = "Panel veterinario",
                    descripcion = "Se completará en el Paso 11",
                    onSalir = {
                        scope.launch {

                            sesionVm.cerrarSesion()

                            navController.navigate(Rutas.LOGIN) {
                                popUpTo(Rutas.PANEL) {
                                    inclusive = true
                                }
                            }
                        }
                    },
                    onServicios = {
                        navController.navigate(Rutas.SERVICIOS)
                    }
                )
            }
        }

        // SERVICIOS
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

@Composable
private fun PantallaTemporal(
    titulo: String,
    descripcion: String,
    onSalir: () -> Unit,
    onServicios: (() -> Unit)? = null
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
            text = descripcion
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
            onClick = onSalir,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}
