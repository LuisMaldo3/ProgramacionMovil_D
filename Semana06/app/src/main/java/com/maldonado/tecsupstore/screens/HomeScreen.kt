package com.maldonado.tecsupstore.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maldonado.tecsupstore.model.Producto
import com.maldonado.tecsupstore.navigation.Screen

@Composable
fun HomeScreen(
    navController: NavController
) {

    val categorias = listOf(
        "Todos",
        "Tecnología",
        "Accesorios",
        "Oficina"
    )

    val productos = listOf(

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

            Text(
                text = "Productos destacados",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 24.dp,
                    bottom = 8.dp
                )
            )
        }

        items(productos) { producto ->

            Card(
                onClick = {
                    navController.navigate(
                        Screen.Productos.route
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 6.dp
                    ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = producto.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = producto.categoria,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "S/ ${"%.2f".format(producto.precio)}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(
                            top = 8.dp
                        )
                    )
                }
            }
        }
    }
}