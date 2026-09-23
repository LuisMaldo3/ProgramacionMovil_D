package com.maldonado.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.maldonado.navlab.components.HomeLogoutAction
import com.maldonado.navlab.components.HomeOptionCard
import com.maldonado.navlab.navigation.Screen
import com.maldonado.navlab.ui.theme.GradientHomeCenter
import com.maldonado.navlab.ui.theme.GradientLoginBottom
import com.maldonado.navlab.ui.theme.MoradoPrincipal

@Composable
fun HomeScreen(
    navController: NavController,
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(MoradoPrincipal, GradientHomeCenter, GradientLoginBottom)
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = "Bienvenido,\nMaldonado",
                    style = TextStyle(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 30.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "¿Qué deseas gestionar hoy?",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                HomeOptionCard(
                    icon = Icons.Filled.Groups,
                    title = "Directorio de Alumnos",
                    subtitle = "Ver y gestionar estudiantes",
                    onClick = { navController.navigate(Screen.List.route) }
                )

                Spacer(modifier = Modifier.height(14.dp))

                HomeOptionCard(
                    icon = Icons.Filled.Person,
                    title = "Mi Perfil Académico",
                    subtitle = "Datos personales y progreso",
                    onClick = { navController.navigate(Screen.Profile.route) }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            HomeLogoutAction(
                onLogout = onLogout,
                modifier = Modifier
                    .padding(bottom = 28.dp)
            )
        }
    }
}
