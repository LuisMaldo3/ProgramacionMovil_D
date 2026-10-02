package com.maldonado.tecsupstore.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.maldonado.tecsupstore.model.Producto
import com.maldonado.tecsupstore.model.Usuario
import com.maldonado.tecsupstore.screens.FavoritosScreen
import com.maldonado.tecsupstore.screens.HomeScreen
import com.maldonado.tecsupstore.screens.PedidosScreen
import com.maldonado.tecsupstore.screens.PerfilScreen
import com.maldonado.tecsupstore.screens.ProductosScreen
import kotlinx.coroutines.launch

/**
 * Navegación principal: envuelve todo con ModalNavigationDrawer.
 * Aquí vive el estado compartido de favoritos, así el DropdownMenu de las tarjetas
 * y el contador (badge) del drawer usan exactamente la misma lista.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {

    val navController = rememberNavController()

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    val usuario = remember {
        Usuario(
            nombre = "Maria Rojas",
            correo = "maria@tecsup.edu.pe"
        )
    }

    // ids de los productos marcados como favoritos (solo en memoria)
    val favoritos = remember {
        mutableStateListOf<Int>()
    }

    val productos = remember {
        listOf(
            Producto(
                id = 1,
                nombre = "Mouse inalámbrico",
                precio = 49.90,
                categoria = "Accesorios"
            ),
            Producto(
                id = 2,
                nombre = "Teclado mecánico",
                precio = 129.90,
                categoria = "Tecnología"
            ),
            Producto(
                id = 3,
                nombre = "Audífonos Bluetooth",
                precio = 89.90,
                categoria = "Tecnología"
            ),
            Producto(
                id = 4,
                nombre = "Cuaderno TECSUP",
                precio = 18.50,
                categoria = "Oficina"
            )
        )
    }

    val backStackEntry by
    navController.currentBackStackEntryAsState()

    val rutaActual =
        backStackEntry?.destination?.route

    fun irA(ruta: String) {

        scope.launch {
            drawerState.close()
        }

        navController.navigate(ruta) {
            popUpTo(Screen.Home.route)
            launchSingleTop = true
        }
    }

    fun alternarFavorito(producto: Producto) {

        if (favoritos.contains(producto.id)) {

            favoritos.remove(producto.id)

        } else {

            favoritos.add(producto.id)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,

        drawerContent = {

            AppDrawer(
                usuario = usuario,
                rutaActual = rutaActual,
                totalFavoritos = favoritos.size,
                onNavegar = { ruta ->
                    irA(ruta)
                },
                onCerrarSesion = {
                    // El laboratorio no tiene login: solo cierra el drawer y vuelve al inicio
                    irA(Screen.Home.route)
                }
            )
        }
    ) {

        Scaffold(
            topBar = {

                TopAppBar(
                    title = {

                        Text(
                            text = when (rutaActual) {

                                Screen.Productos.route ->
                                    "Productos"

                                Screen.Pedidos.route ->
                                    "Mis pedidos"

                                Screen.Favoritos.route ->
                                    "Favoritos"

                                Screen.Perfil.route ->
                                    "Perfil"

                                else ->
                                    "TECSUP Store"
                            }
                        )
                    },

                    navigationIcon = {

                        IconButton(
                            onClick = {

                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Abrir menú"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->

            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(paddingValues)
            ) {

                composable(
                    Screen.Home.route
                ) {

                    HomeScreen(
                        productos = productos,
                        favoritos = favoritos,
                        onToggleFavorito = { producto ->
                            alternarFavorito(producto)
                        },
                        onVerTodos = {
                            irA(Screen.Productos.route)
                        }
                    )
                }

                composable(
                    Screen.Productos.route
                ) {

                    ProductosScreen(
                        productos = productos,
                        favoritos = favoritos,
                        onToggleFavorito = { producto ->
                            alternarFavorito(producto)
                        }
                    )
                }

                composable(
                    Screen.Pedidos.route
                ) {

                    PedidosScreen()
                }

                composable(
                    Screen.Favoritos.route
                ) {

                    FavoritosScreen(
                        productos = productos,
                        favoritos = favoritos,
                        onToggleFavorito = { producto ->
                            alternarFavorito(producto)
                        }
                    )
                }

                composable(
                    Screen.Perfil.route
                ) {

                    PerfilScreen(
                        usuario = usuario
                    )
                }
            }
        }
    }
}
