package com.maldonado.tecsupstore.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maldonado.tecsupstore.model.Producto
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
@Composable
fun ProductosScreen() {

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
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(productos) { producto ->

            TarjetaProducto(
                producto = producto
            )
        }
    }
}

@Composable
fun TarjetaProducto(
    producto: Producto
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
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
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Box {

                IconButton(
                    onClick = {
                        expanded = true
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opciones"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {

                    DropdownMenuItem(
                        text = {
                            Text("Ver producto")
                        },
                        onClick = {
                            expanded = false
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text("Eliminar")
                        },
                        onClick = {
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}