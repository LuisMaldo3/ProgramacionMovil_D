package com.maldonado.tecsupstore.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maldonado.tecsupstore.components.TarjetaProducto
import com.maldonado.tecsupstore.model.Producto

@Composable
fun ProductosScreen(
    productos: List<Producto>,
    favoritos: List<Int>,
    onToggleFavorito: (Producto) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(productos, key = { it.id }) { producto ->

            TarjetaProducto(
                producto = producto,
                esFavorito = favoritos.contains(producto.id),
                onToggleFavorito = {
                    onToggleFavorito(producto)
                }
            )
        }
    }
}
