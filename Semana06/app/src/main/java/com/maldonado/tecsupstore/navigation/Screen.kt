package com.maldonado.tecsupstore.navigation

sealed class Screen(val route: String) {

    object Home : Screen("home")

    object Productos : Screen("productos")

    object Pedidos : Screen("pedidos")

    object Favoritos : Screen("favoritos")

    object Perfil : Screen("perfil")
}
