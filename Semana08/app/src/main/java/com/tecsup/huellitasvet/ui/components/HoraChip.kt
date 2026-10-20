package com.tecsup.huellitasvet.ui.components

import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun HoraChip(hora: String, seleccionado: Boolean, ocupado: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        enabled = !ocupado,
        label = { Text(hora, textDecoration = if (ocupado) TextDecoration.LineThrough else null) }
    )
}
