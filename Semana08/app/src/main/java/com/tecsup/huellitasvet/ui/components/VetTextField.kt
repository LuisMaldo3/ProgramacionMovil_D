
package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun VetTextField(
    valor: String,
    onValorCambio: (String) -> Unit,
    etiqueta: String,
    error: String? = null,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    ocultar: Boolean = false,
    tipoTeclado: KeyboardType = KeyboardType.Text
) {

    OutlinedTextField(
        value = valor,
        onValueChange = onValorCambio,

        label = {
            Text(etiqueta)
        },

        leadingIcon = if (icono != null) {
            {
                Icon(
                    imageVector = icono,
                    contentDescription = null
                )
            }
        } else null,

        isError = error != null,

        supportingText = if (error != null) {
            {
                Text(error)
            }
        } else null,

        visualTransformation = if (ocultar) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },

        keyboardOptions = KeyboardOptions(
            keyboardType = tipoTeclado
        ),

        shape = RoundedCornerShape(15.dp),

        modifier = modifier.fillMaxWidth(),

        singleLine = true
    )
}
