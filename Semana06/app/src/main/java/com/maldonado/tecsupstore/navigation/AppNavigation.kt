package com.maldonado.tecsupstore.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maldonado.tecsupstore.screens.HomeScreen
import com.maldonado.tecsupstore.screens.ProductosScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    fun irA(ruta: String) {

        scope.launch {
            drawerState.close()
        }

        navController.navigate(ruta) {
            popUpTo(Screen.Home.route)
            launchSingleTop = true
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,

        drawerContent = {

            ModalDrawerSheet {

                Text(
                    text = "TECSUP Store",
                    modifier = Modifier.padding(20.dp)
                )

                HorizontalDivider()

                NavigationDrawerItem(
                    label = {
                        Text("Inicio")
                    },

                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null
                        )
                    },

                    selected = false,

                    onClick = {
                        irA(Screen.Home.route)
                    }
                )

                NavigationDrawerItem(
                    label = {
                        Text("Productos")
                    },

                    icon = {
                        Icon(
                            Icons.Default.ShoppingBag,
                            contentDescription = null
                        )
                    },

                    selected = false,

                    onClick = {
                        irA(Screen.Productos.route)
                    }
                )
            }
        }
    ) {

        Scaffold(
            topBar = {

                TopAppBar(
                    title = {
                        Text("TECSUP Store")
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
                    route = Screen.Home.route
                ) {

                    HomeScreen(
                        navController = navController
                    )
                }

                composable(
                    route = Screen.Productos.route
                ) {

                    ProductosScreen()
                }
            }
        }
    }
}