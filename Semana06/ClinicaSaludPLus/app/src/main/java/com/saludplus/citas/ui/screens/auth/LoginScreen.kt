package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.Arrangement
import compose.icons.fontawesomeicons.Solid
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.solid.Lock
import compose.icons.fontawesomeicons.solid.Envelope
import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.CampoDiseno
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.ErrorRojo
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala

@Composable
fun LoginScreen(navController: NavController) {
    val e = rememberEscala()

    var identificador by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorIdentificador by remember { mutableStateOf<String?>(null) }
    var errorContrasena by remember { mutableStateOf<String?>(null) }
    var errorGeneral by remember { mutableStateOf<String?>(null) }

    fun validar(): Boolean {
        val dato = identificador.trim()
        val esCorreo = Patterns.EMAIL_ADDRESS.matcher(dato).matches()
        val esTelefono = dato.count { it.isDigit() } == 9 && dato.all { it.isDigit() || it == ' ' }
        errorIdentificador =
            if (dato.isEmpty() || !(esCorreo || esTelefono)) "Ingresa un correo o teléfono válido" else null
        errorContrasena = if (contrasena.isEmpty()) "Ingresa tu contraseña" else null
        return errorIdentificador == null && errorContrasena == null
    }

    fun ingresar() {
        errorGeneral = null
        if (!validar()) return
        val dato = identificador.trim()
        val limpio = if (dato.contains("@")) dato else dato.filter { it.isDigit() }
        if (Repositorio.iniciarSesion(limpio, contrasena)) {
            navController.navigate(Rutas.HOME) {
                popUpTo(Rutas.SPLASH) { inclusive = true }
            }
        } else {
            errorGeneral = "Correo, teléfono o contraseña incorrectos"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(e.d(20)))
            Text(
                "Iniciar sesión",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = e.s(28),
                lineHeight = e.s(34),
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal
            )
            Spacer(Modifier.height(e.d(6)))
            Text(
                "Ingresa para gestionar tus citas",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = e.s(18),
                lineHeight = e.s(22),
                color = TextoSecundario
            )
            Spacer(Modifier.height(e.d(30)))

            CampoDiseno(
                etiqueta = "Correo o teléfono",
                valor = identificador,
                onCambio = { identificador = it },
                ejemplo = "juan@correo.com",
                icono = FontAwesomeIcons.Solid.Envelope,
                error = errorIdentificador,
                teclado = KeyboardType.Email,
                modifier = Modifier.padding(horizontal = e.d(17))
            )
            Spacer(Modifier.height(e.d(20)))
            CampoDiseno(
                etiqueta = "Contraseña",
                valor = contrasena,
                onCambio = { contrasena = it },
                ejemplo = "••••••••",
                icono = FontAwesomeIcons.Solid.Lock,
                error = errorContrasena,
                esPassword = true,
                modifier = Modifier.padding(horizontal = e.d(17))
            )

            Spacer(Modifier.height(e.d(26)))
            BotonPrincipal(
                "Iniciar sesión",
                onClick = { ingresar() },
                modifier = Modifier.padding(horizontal = e.d(17))
            )
            if (errorGeneral != null) {
                Text(
                    errorGeneral ?: "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = e.d(8)),
                    textAlign = TextAlign.Center,
                    color = ErrorRojo,
                    fontSize = e.s(14)
                )
            }
            Spacer(Modifier.height(e.d(12)))
        }

        // Pie fijo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = e.d(8), bottom = e.d(14)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "¿No tienes cuenta? ",
                fontSize = e.s(18),
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal
            )
            Text(
                "Regístrate",
                modifier = Modifier.clickable { navController.navigate(Rutas.REGISTRO) },
                fontSize = e.s(18),
                fontWeight = FontWeight.Bold,
                color = Azul
            )
        }
    }
}
