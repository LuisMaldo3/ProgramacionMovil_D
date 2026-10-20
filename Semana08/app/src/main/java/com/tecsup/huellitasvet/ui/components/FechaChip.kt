package com.tecsup.huellitasvet.ui.components

import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FechaChip(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    FilterChip(selected = seleccionado, onClick = onClick, label = { Text(texto) })
}
