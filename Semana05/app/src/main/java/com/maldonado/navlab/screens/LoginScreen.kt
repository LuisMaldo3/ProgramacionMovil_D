package com.maldonado.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maldonado.navlab.ui.theme.*

@Composable
fun LoginScreen(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onLoginSuccess: () -> Unit
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(GradientLoginTop, GradientLoginBottom)
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 340.dp)
                .width(304.dp)
                .padding(vertical = 24.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = TarjetasDirectorio),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Portal Académico",
                    style = TextStyle(
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoradoPrincipal
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Accede a tu cuenta",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = TextoSecundario
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(34.dp))

                CustomInputField(
                    value = email,
                    onValueChange = onEmailChange,
                    placeholder = "Correo Institucional",
                    leadingIcon = Icons.Filled.Email,
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomInputField(
                    value = password,
                    onValueChange = onPasswordChange,
                    placeholder = "Contraseña",
                    leadingIcon = Icons.Filled.Lock,
                    trailingIcon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    isPassword = true,
                    passwordVisible = passwordVisible,
                    onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
                    keyboardType = KeyboardType.Password
                )

                Spacer(modifier = Modifier.height(28.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = if (email.isNotBlank() && password.isNotBlank()) MoradoPrincipal else MoradoPrincipal.copy(alpha = 0.6f),
                    shadowElevation = 3.dp,
                    onClick = {
                        if (email.isNotBlank() && password.isNotBlank()) {
                            onLoginSuccess()
                        }
                    }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "INICIAR SESIÓN",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "¿Olvidaste tu contraseña?",
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = TextoSecundario
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun CustomInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    trailingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePasswordVisibility: (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .border(1.dp, BordesCampos, RoundedCornerShape(8.dp))
            .background(Color.Transparent, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = TextoSecundario,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = TextoSecundario
                        )
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 12.sp,
                        color = TextoPrincipal,
                        fontFamily = FontFamily.Default
                    ),
                    visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    cursorBrush = SolidColor(MoradoPrincipal),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (trailingIcon != null && onTogglePasswordVisibility != null) {
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onTogglePasswordVisibility,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = TextoSecundario,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
