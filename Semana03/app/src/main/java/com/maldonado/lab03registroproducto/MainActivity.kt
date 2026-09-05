package com.maldonado.lab03registroproducto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maldonado.lab03registroproducto.ui.theme.Lab03RegistroProductoTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Lab03RegistroProductoTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    PantallaRegistro(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PantallaRegistro(modifier: Modifier = Modifier) {

    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }

    var mostrarResumen by remember { mutableStateOf(false) }

    // MEJORA 1: estados de error por campo
    var errorNombre by remember { mutableStateOf(false) }
    var errorPrecio by remember { mutableStateOf(false) }
    var errorCantidad by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),

        ) {

        Text(
            text = "Nuevo producto",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Completa los datos y preciona Agregar",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Campo Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                errorNombre = false   // limpia error al escribir
            },
            label = { Text("Nombre del Producto") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorNombre
        )
        // MEJORA 1: mensaje de error nombre
        if (errorNombre) {
            Text(
                text = "El nombre es obligatorio",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Precio y Cantidad
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = precio,
                    onValueChange = {
                        precio = it
                        errorPrecio = false   // limpia error al escribir
                    },
                    label = { Text("Precio S/") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorPrecio
                )
                // MEJORA 1: mensaje de error precio
                if (errorPrecio) {
                    Text(
                        text = "El precio es obligatorio",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = {
                        cantidad = it
                        errorCantidad = false   // limpia error al escribir
                    },
                    label = { Text("Cantidad") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorCantidad
                )
                // MEJORA 1: mensaje de error cantidad
                if (errorCantidad) {
                    Text(
                        text = "La cantidad es obligatoria",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Button(
            onClick = {
                // MEJORA 1: validar campos vacíos antes de mostrar
                errorNombre = nombre.isBlank()
                errorPrecio = precio.isBlank()
                errorCantidad = cantidad.isBlank()

                if (!errorNombre && !errorPrecio && !errorCantidad) {
                    mostrarResumen = true
                } else {
                    mostrarResumen = false
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("AGREGAR PRODUCTO")
        }

        // MEJORA 2: botón LIMPIAR
        Button(
            onClick = {
                nombre = ""
                precio = ""
                cantidad = ""
                mostrarResumen = false
                errorNombre = false
                errorPrecio = false
                errorCantidad = false
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("LIMPIAR")
        }

        if (mostrarResumen) {

            val precioNumero = precio.toDoubleOrNull() ?: 0.0
            val cantidadNumero = cantidad.toIntOrNull() ?: 0
            val importe = precioNumero * cantidadNumero

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Resumen del producto",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text("Nombre: $nombre")
                    Text("Precio: S/ " + String.format("%.2f", precioNumero))
                    Text("Cantidad: $cantidadNumero")
                    Text("Importe: S/ %.2f".format(importe))
                }
            }

            Text(
                "Producto registrado correctamente",
                color = Color(0xFF2E7D32)
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Text(
            "Desarrollado por Luis Maldonado",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}