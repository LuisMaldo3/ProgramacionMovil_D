package com.saludplus.citas.ui.components

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.AzulDeshabilitado
import com.saludplus.citas.ui.theme.BordeCampo
import com.saludplus.citas.ui.theme.BordeTarjetaIcono
import com.saludplus.citas.ui.theme.ErrorRojo
import com.saludplus.citas.ui.theme.EtiquetaCampo
import com.saludplus.citas.ui.theme.GrisClaro
import com.saludplus.citas.ui.theme.RellenoBuscador
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala

/** Campo de texto estándar de Material (se conserva por compatibilidad). */
@Composable
fun CampoTexto(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    esPassword: Boolean = false,
    teclado: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        singleLine = true,
        isError = error != null,
        supportingText = if (error != null) { { Text(error) } } else null,
        visualTransformation = if (esPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (esPassword) KeyboardType.Password else teclado
        ),
        modifier = modifier.fillMaxWidth()
    )
}

/** Botón azul del diseño: ancho completo, 62 dp de alto, esquinas de 14 dp. */
@Composable
fun BotonPrincipal(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true
) {
    val e = rememberEscala()
    Button(
        onClick = onClick,
        enabled = habilitado,
        modifier = modifier
            .fillMaxWidth()
            .height(e.d(62)),
        shape = RoundedCornerShape(e.d(14)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Azul,
            contentColor = Color.White,
            disabledContainerColor = AzulDeshabilitado,
            disabledContentColor = Color.White
        )
    ) {
        Text(texto, fontSize = e.s(20), fontWeight = FontWeight.SemiBold)
    }
}

/**
 * Fila de formulario del diseño: tarjeta con el ícono a la izquierda y, a su derecha,
 * la etiqueta pequeña con la caja de entrada debajo.
 */
@Composable
fun CampoDiseno(
    etiqueta: String,
    valor: String,
    onCambio: (String) -> Unit,
    ejemplo: String,
    icono: ImageVector,
    modifier: Modifier = Modifier,
    error: String? = null,
    esPassword: Boolean = false,
    teclado: KeyboardType = KeyboardType.Text
) {
    val e = rememberEscala()
    val formaTarjeta = RoundedCornerShape(e.d(14))
    val formaCaja = RoundedCornerShape(e.d(10))
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(e.d(65), e.d(61))
                    .shadow(2.dp, formaTarjeta)
                    .clip(formaTarjeta)
                    .background(Color.White)
                    .border(1.dp, BordeTarjetaIcono, formaTarjeta),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = Azul,
                    modifier = Modifier.size(e.d(28))
                )
            }
            Spacer(Modifier.width(e.d(3)))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .offset(y = e.d(-4))
            ) {
                Text(
                    etiqueta,
                    fontSize = e.s(17),
                    lineHeight = e.s(20),
                    color = EtiquetaCampo,
                    modifier = Modifier.padding(start = e.d(7))
                )
                Spacer(Modifier.height(e.d(3)))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(e.d(46))
                        .clip(formaCaja)
                        .background(Color.White)
                        .border(1.dp, BordeCampo, formaCaja)
                        .padding(horizontal = e.d(14)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = valor,
                        onValueChange = onCambio,
                        singleLine = true,
                        textStyle = TextStyle(fontSize = e.s(23), color = Color.Black),
                        cursorBrush = SolidColor(Azul),
                        visualTransformation = if (esPassword) PasswordVisualTransformation() else VisualTransformation.None,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = if (esPassword) KeyboardType.Password else teclado
                        ),
                        decorationBox = { campo ->
                            Box(contentAlignment = Alignment.CenterStart) {
                                if (valor.isEmpty()) {
                                    Text(ejemplo, fontSize = e.s(23), color = GrisClaro, maxLines = 1)
                                }
                                campo()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        if (error != null) {
            Text(
                error,
                color = ErrorRojo,
                fontSize = e.s(13),
                modifier = Modifier.padding(start = e.d(88), top = e.d(6))
            )
        }
    }
}

/** Buscador del diseño: 48 dp de alto, fondo gris claro, lupa a la izquierda. */
@Composable
fun CampoBusqueda(
    valor: String,
    onCambio: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    val e = rememberEscala()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(e.d(48))
            .clip(RoundedCornerShape(e.d(12)))
            .background(RellenoBuscador)
            .padding(horizontal = e.d(14)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.Search,
            contentDescription = null,
            tint = TextoSecundario,
            modifier = Modifier.size(e.d(24))
        )
        Spacer(Modifier.width(e.d(10)))
        BasicTextField(
            value = valor,
            onValueChange = onCambio,
            singleLine = true,
            textStyle = TextStyle(fontSize = e.s(17), color = Color.Black),
            cursorBrush = SolidColor(Azul),
            decorationBox = { campo ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (valor.isEmpty()) {
                        Text(placeholder, fontSize = e.s(17), color = TextoSecundario, maxLines = 1)
                    }
                    campo()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
