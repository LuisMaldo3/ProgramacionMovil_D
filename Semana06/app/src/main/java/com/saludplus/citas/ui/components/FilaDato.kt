package com.saludplus.citas.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala

@Composable
fun FilaDato(etiqueta: String, valor: String) {
    val e = rememberEscala()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = e.d(6)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(etiqueta, fontSize = e.s(17), color = TextoSecundario)
        Text(valor, fontSize = e.s(17), fontWeight = FontWeight.Bold, color = TextoPrincipal)
    }
}
