package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.Arrangement
import compose.icons.fontawesomeicons.Solid
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.solid.Lock
import compose.icons.fontawesomeicons.solid.Envelope
import compose.icons.fontawesomeicons.solid.PhoneVolume
import compose.icons.fontawesomeicons.solid.User
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.saludplus.citas.data.model.Usuario
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.CampoDiseno
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.ErrorRojo
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala

/** Clave con la que Términos le avisa a Registro que el paciente pulsó "Aceptar y continuar". */
internal const val CLAVE_TERMINOS = "terminos_aceptados"

@Composable
fun RegistroScreen(navController: NavController) {
    val e = rememberEscala()

    // rememberSaveable: lo escrito no se pierde al abrir Términos y volver
    var nombre by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var aceptaTerminos by rememberSaveable { mutableStateOf(false) }

    // Si el paciente acepta desde la pantalla de Términos, la casilla se marca sola
    val entrada = remember { navController.getBackStackEntry(Rutas.REGISTRO) }
    val aceptadoEnTerminos by entrada.savedStateHandle
        .getStateFlow(CLAVE_TERMINOS, false).collectAsState()
    LaunchedEffect(aceptadoEnTerminos) {
        if (aceptadoEnTerminos) {
            aceptaTerminos = true
            entrada.savedStateHandle[CLAVE_TERMINOS] = false
        }
    }

    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorTelefono by remember { mutableStateOf<String?>(null) }
    var errorCorreo by remember { mutableStateOf<String?>(null) }
    var errorContrasena by remember { mutableStateOf<String?>(null) }
    var errorTerminos by remember { mutableStateOf<String?>(null) }
    var errorGeneral by remember { mutableStateOf<String?>(null) }

    fun validar(): Boolean {
        errorNombre = if (nombre.isBlank()) "Ingresa tu nombre" else null
        errorTelefono =
            if (telefono.count { it.isDigit() } != 9) "Ingresa un teléfono de 9 dígitos" else null
        // El correo es opcional: si se escribe, debe ser válido
        errorCorreo =
            if (correo.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches())
                "Correo no válido" else null
        errorContrasena = if (contrasena.length < 6) "Mínimo 6 caracteres" else null
        errorTerminos =
            if (!aceptaTerminos) "Debes aceptar los Términos y la Política de Privacidad" else null
        return errorNombre == null && errorTelefono == null &&
                errorCorreo == null && errorContrasena == null && errorTerminos == null
    }

    fun registrar() {
        errorGeneral = null
        if (!validar()) return
        val tel = telefono.filter { it.isDigit() }
        val usuario = Usuario(
            id = Repositorio.siguienteIdUsuario(),
            nombre = nombre.trim(),
            correo = correo.trim(),
            contrasena = contrasena,
            telefono = tel
        )
        if (Repositorio.registrarUsuario(usuario)) {
            navController.navigate(Rutas.LOGIN) {
                popUpTo(Rutas.SPLASH)
            }
        } else {
            errorGeneral = "Ya existe una cuenta con ese correo o teléfono"
        }
    }

    // Estructura: [contenido con scroll] + [pie fijo]. El pie siempre queda arriba de la barra
    // de navegación del teléfono, y el contenido se desplaza si la pantalla es corta.
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
                "Crear cuenta",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = e.s(28),
                lineHeight = e.s(34),
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal
            )
            Spacer(Modifier.height(e.d(6)))
            Text(
                "Regístrate para agendar tus citas",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = e.s(18),
                lineHeight = e.s(22),
                color = TextoSecundario
            )
            Spacer(Modifier.height(e.d(30)))

            CampoDiseno(
                etiqueta = "Nombre",
                valor = nombre,
                onCambio = { nombre = it },
                ejemplo = "Tu nombre completo",
                icono = FontAwesomeIcons.Solid.User,
                error = errorNombre,
                modifier = Modifier.padding(horizontal = e.d(17))
            )
            Spacer(Modifier.height(e.d(20)))
            CampoDiseno(
                etiqueta = "Teléfono",
                valor = telefono,
                onCambio = { texto -> telefono = texto.filter { c -> c.isDigit() || c == ' ' } },
                ejemplo = "Tu teléfono de 9 dígitos",
                icono = FontAwesomeIcons.Solid.PhoneVolume,
                error = errorTelefono,
                teclado = KeyboardType.Phone,
                modifier = Modifier.padding(horizontal = e.d(17))
            )
            Spacer(Modifier.height(e.d(20)))
            CampoDiseno(
                etiqueta = "Correo (opcional)",
                valor = correo,
                onCambio = { correo = it },
                ejemplo = "Escribe tu correo",
                icono = FontAwesomeIcons.Solid.Envelope,
                error = errorCorreo,
                teclado = KeyboardType.Email,
                modifier = Modifier.padding(horizontal = e.d(17))
            )
            Spacer(Modifier.height(e.d(20)))
            CampoDiseno(
                etiqueta = "Contraseña",
                valor = contrasena,
                onCambio = { contrasena = it },
                ejemplo = "Mínimo 6 caracteres",
                icono = FontAwesomeIcons.Solid.Lock,
                error = errorContrasena,
                esPassword = true,
                modifier = Modifier.padding(horizontal = e.d(17))
            )

            Spacer(Modifier.height(e.d(14)))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = e.d(11)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = aceptaTerminos,
                    onCheckedChange = {
                        aceptaTerminos = it
                        if (it) errorTerminos = null
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Azul,
                        uncheckedColor = if (errorTerminos != null) ErrorRojo else TextoSecundario
                    )
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "He leído y acepto los",
                        fontSize = e.s(17),
                        lineHeight = e.s(22),
                        color = TextoSecundario
                    )
                    Text(
                        "Términos y Condiciones y la Política de Privacidad",
                        modifier = Modifier.clickable { navController.navigate(Rutas.TERMINOS) },
                        fontSize = e.s(17),
                        lineHeight = e.s(22),
                        fontWeight = FontWeight.SemiBold,
                        color = Azul
                    )
                }
            }
            if (errorTerminos != null) {
                Text(
                    errorTerminos ?: "",
                    modifier = Modifier.padding(start = e.d(17), top = e.d(2)),
                    color = ErrorRojo,
                    fontSize = e.s(14)
                )
            }

            Spacer(Modifier.height(e.d(20)))
            BotonPrincipal(
                "Registrarme",
                onClick = { registrar() },
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
                "¿Ya tienes cuenta? ",
                fontSize = e.s(18),
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal
            )
            Text(
                "Iniciar sesión",
                modifier = Modifier.clickable { navController.navigate(Rutas.LOGIN) },
                fontSize = e.s(18),
                fontWeight = FontWeight.Bold,
                color = Azul
            )
        }
    }
}
