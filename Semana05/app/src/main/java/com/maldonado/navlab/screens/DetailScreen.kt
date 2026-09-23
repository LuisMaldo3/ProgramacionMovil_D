package com.maldonado.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
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
import com.maldonado.navlab.components.AcademicTopBar
import com.maldonado.navlab.components.DetailInformationRow
import com.maldonado.navlab.components.StudentAvatar
import com.maldonado.navlab.model.AlumnoRepository
import com.maldonado.navlab.ui.theme.*

@Composable
fun DetailScreen(navController: NavController, itemId: Int) {
    val alumno = AlumnoRepository.getAlumnoById(itemId)

    Scaffold(
        containerColor = FondoPrincipal,
        topBar = {
            AcademicTopBar(
                title = "Expediente Académico",
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(204.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(148.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(MoradoPrincipal, GradientDetailBottom)
                            ),
                            shape = RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomStart = 26.dp,
                                bottomEnd = 26.dp
                            )
                        )
                )

                StudentAvatar(
                    size = 112.dp,
                    iconSize = 60.dp,
                    photoResId = alumno.fotoResId,
                    borderWidth = 3.dp,
                    borderColor = Color.White,
                    elevation = 3.dp,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = alumno.nombreCorto,
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = alumno.carrera,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = MoradoPrincipal
                ),
                textAlign = TextAlign.Center,
                maxLines = 2,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = TarjetaExpediente),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    DetailInformationRow(
                        icon = Icons.Filled.Badge,
                        label = "ID Estudiante",
                        value = alumno.idEstudiante
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    DetailInformationRow(
                        icon = Icons.Filled.Email,
                        label = "Correo Electrónico",
                        value = alumno.correo
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    DetailInformationRow(
                        icon = Icons.Filled.School,
                        label = "Facultad",
                        value = alumno.facultad
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Separadores
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Biografía",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoPrincipal
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = alumno.biografia,
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = TextoSecundario
                        ),
                        textAlign = TextAlign.Left
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
