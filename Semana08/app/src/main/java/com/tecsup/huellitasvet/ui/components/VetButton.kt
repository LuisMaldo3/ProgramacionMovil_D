package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VetButton(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, habilitado: Boolean = true) {
    Button(onClick = onClick, modifier = modifier.fillMaxWidth(), enabled = habilitado) { Text(texto) }
}
