package com.maldonado.navlab.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.maldonado.navlab.screens.DetailScreen
import com.maldonado.navlab.screens.HomeScreen
import com.maldonado.navlab.screens.ListScreen
import com.maldonado.navlab.screens.LoginScreen
import com.maldonado.navlab.screens.ProfileScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val handleLogout = {
        password = ""
        navController.navigate(Screen.Login.route) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                onLogout = handleLogout
            )
        }

        composable(Screen.List.route) {
            ListScreen(navController = navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                navController = navController,
                onLogout = handleLogout
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.IntType
                    defaultValue = 1
                }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 1
            DetailScreen(
                navController = navController,
                itemId = itemId
            )
        }
    }
}
