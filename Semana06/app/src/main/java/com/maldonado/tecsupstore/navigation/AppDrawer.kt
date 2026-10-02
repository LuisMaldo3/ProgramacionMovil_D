package com.maldonado.tecsupstore.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maldonado.tecsupstore.model.Usuario

private class Destino(
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector
)

/**
 * Contenido del NavigationDrawer: encabezado con iniciales y datos del usuario,
 * destinos de navegación (el activo se resalta) y el contador de Favoritos.
 */
@Composable
fun AppDrawer(
    usuario: Usuario,
    rutaActual: String?,
    totalFavoritos: Int,
    onNavegar: (String) -> Unit,
    onCerrarSesion: () -> Unit
) {

    val destinos = listOf(
        Destino(Screen.Home.route, "Inicio", Icons.Default.Home),
        Destino(Screen.Productos.route, "Productos", Icons.Default.ShoppingCart),
        Destino(Screen.Pedidos.route, "Mis pedidos", Icons.AutoMirrored.Filled.List),
        Destino(Screen.Favoritos.route, "Favoritos", Icons.Default.Favorite),
        Destino(Screen.Perfil.route, "Perfil", Icons.Default.Person)
    )

    ModalDrawerSheet {

        // Encabezado: avatar con iniciales + nombre y correo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = usuario.iniciales,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Column {

                Text(
                    text = usuario.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = usuario.correo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        HorizontalDivider()

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        destinos.forEach { destino ->

            NavigationDrawerItem(
                label = {
                    Text(destino.etiqueta)
                },
                icon = {
                    Icon(
                        imageVector = destino.icono,
                        contentDescription = null
                    )
                },
                // Contador de Favoritos: se actualiza solo porque lee la misma lista que el DropdownMenu
                badge = {
                    if (
                        destino.ruta == Screen.Favoritos.route &&
                        totalFavoritos > 0
                    ) {
                        Badge {
                            Text(totalFavoritos.toString())
                        }
                    }
                },
                // El destino activo se resalta con color de fondo
                selected = rutaActual == destino.ruta,
                onClick = {
                    onNavegar(destino.ruta)
                },
                modifier = Modifier.padding(
                    NavigationDrawerItemDefaults.ItemPadding
                )
            )
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        HorizontalDivider()

        NavigationDrawerItem(
            label = {
                Text("Cerrar sesión")
            },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null
                )
            },
            selected = false,
            onClick = onCerrarSesion,
            modifier = Modifier.padding(
                NavigationDrawerItemDefaults.ItemPadding
            )
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )
    }
}
