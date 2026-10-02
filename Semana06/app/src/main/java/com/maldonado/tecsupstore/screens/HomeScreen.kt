package com.maldonado.tecsupstore.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maldonado.tecsupstore.components.TarjetaProducto
import com.maldonado.tecsupstore.model.Producto

@Composable
fun HomeScreen(
    productos: List<Producto>,
    favoritos: List<Int>,
    onToggleFavorito: (Producto) -> Unit,
    onVerTodos: () -> Unit
) {

    val categorias = listOf(
        "Todos",
        "Tecnología",
        "Accesorios",
        "Oficina"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = 24.dp
        )
    ) {

        item {

            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 20.dp,
                    end = 16.dp
                )
            ) {

                Text(
                    text = "Bienvenido a TECSUP Store",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Encuentra productos disponibles",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {

            Text(
                text = "Categorías",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 24.dp,
                    bottom = 8.dp
                )
            )
        }

        item {

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(
                    horizontal = 16.dp
                )
            ) {

                items(categorias) { categoria ->

                    AssistChip(
                        onClick = { },
                        label = {
                            Text(categoria)
                        }
                    )
                }
            }
        }

        item {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 8.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Productos destacados",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                TextButton(
                    onClick = onVerTodos
                ) {

                    Text("Ver todos")
                }
            }
        }

        items(productos, key = { it.id }) { producto ->

            // Misma tarjeta con ⋮ (DropdownMenu) también en Inicio
            TarjetaProducto(
                producto = producto,
                esFavorito = favoritos.contains(producto.id),
                onToggleFavorito = {
                    onToggleFavorito(producto)
                },
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 6.dp
                )
            )
        }
    }
}
