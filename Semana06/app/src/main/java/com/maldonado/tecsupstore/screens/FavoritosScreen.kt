package com.maldonado.tecsupstore.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maldonado.tecsupstore.components.TarjetaProducto
import com.maldonado.tecsupstore.model.Producto

@Composable
fun FavoritosScreen(
    productos: List<Producto>,
    favoritos: List<Int>,
    onToggleFavorito: (Producto) -> Unit
) {

    val productosFavoritos =
        productos.filter {
            favoritos.contains(it.id)
        }

    if (productosFavoritos.isEmpty()) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "No tienes favoritos",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Marca productos desde el menú de 3 puntos",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

    } else {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(productosFavoritos, key = { it.id }) { producto ->

                // Misma tarjeta con ⋮ en todas las secciones
                TarjetaProducto(
                    producto = producto,
                    esFavorito = true,
                    onToggleFavorito = {
                        onToggleFavorito(producto)
                    }
                )
            }
        }
    }
}
