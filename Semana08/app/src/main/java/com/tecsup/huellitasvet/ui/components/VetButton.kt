package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VetButton(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, habilitado: Boolean = true) {
    Button(onClick = onClick, modifier = modifier.fillMaxWidth().heightIn(min = 52.dp), enabled = habilitado, shape = RoundedCornerShape(14.dp)) { Text(texto, fontWeight = FontWeight.SemiBold) }
}
