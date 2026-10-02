package com.maldonado.tecsupstore.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maldonado.tecsupstore.screens.HomeScreen
import com.maldonado.tecsupstore.screens.ProductosScreen

@Composable
fun AppNavigation() {

    val navController =
        rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
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