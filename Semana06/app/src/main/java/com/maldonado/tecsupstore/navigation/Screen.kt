package com.maldonado.tecsupstore.navigation

sealed class Screen(val route: String) {

    object Home : Screen("home")

    object Productos : Screen("productos")

    object Favoritos : Screen("favoritos")
}