package com.maldonado.navlab.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.maldonado.navlab.components.AcademicTopBar
import com.maldonado.navlab.components.ProfileHeader
import com.maldonado.navlab.components.ProfileInformationRow
import com.maldonado.navlab.components.ProfileLogoutButton
import com.maldonado.navlab.model.AlumnoRepository
import com.maldonado.navlab.ui.theme.EtiquetasSecciones
import com.maldonado.navlab.ui.theme.FondoPrincipal
import com.maldonado.navlab.ui.theme.TextoPrincipal

@Composable
fun ProfileScreen(
    navController: NavController,
    onLogout: () -> Unit
) {
    val miPerfil = AlumnoRepository.getAlumnoById(1)

    Scaffold(
        containerColor = FondoPrincipal,
        topBar = {
            AcademicTopBar(
                title = "Configuración de Perfil",
                onBackClick = { navController.popBackStack() },
                containerColor = FondoPrincipal,
                contentColor = TextoPrincipal
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                ProfileHeader(
                    fullName = miPerfil.nombreCompleto,
                    photoResId = miPerfil.fotoResId
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "INFORMACIÓN PERSONAL",
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EtiquetasSecciones,
                            letterSpacing = 1.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ProfileInformationRow(
                        icon = Icons.Filled.Person,
                        label = "Nombre Completo",
                        value = miPerfil.nombreCompleto
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileInformationRow(
                        icon = Icons.Filled.Email,
                        label = "Correo",
                        value = miPerfil.correoPerfil
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileInformationRow(
                        icon = Icons.Filled.Phone,
                        label = "Teléfono",
                        value = miPerfil.telefono
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "ACADÉMICO",
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EtiquetasSecciones,
                            letterSpacing = 1.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ProfileInformationRow(
                        icon = Icons.Filled.School,
                        label = "Carrera",
                        value = miPerfil.carreraPerfil
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileInformationRow(
                        icon = Icons.Filled.CalendarMonth,
                        label = "Ciclo Actual",
                        value = miPerfil.cicloActual
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            ProfileLogoutButton(
                onLogout = onLogout,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 20.dp)
            )
        }
    }
}
