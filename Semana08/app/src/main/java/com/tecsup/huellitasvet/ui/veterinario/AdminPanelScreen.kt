package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.theme.VetTheme

private val AdminPrimary = Color(0xFF1B3B36)
private val AdminSurface = Color(0xFFF4F7F6)
private val AdminCard = Color(0xFFFFFFFF)
private val AdminAccent = Color(0xFF0F766E)

@Composable
fun AdminPanelScreen(
    nombreAdmin: String,
    onServicios: () -> Unit,
    onAgenda: () -> Unit,
    onPacientes: () -> Unit,
    onSalir: () -> Unit
) {
    VetTheme(rol = Rol.ADMIN, oscuro = false) {
        Scaffold(
            containerColor = AdminSurface,
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF0F3E37), Color(0xFF1B5E55), Color(0xFF267D71))
                            )
                        )
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(46.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Filled.AdminPanelSettings,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    "Panel de administración",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    "Hola, ${nombreAdmin.ifBlank { "Administrador" }}",
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 13.sp
                                )
                            }
                        }
                        IconButton(
                            onClick = onSalir,
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.15f), CircleShape)
                        ) {
                            Icon(
                                Icons.Filled.ExitToApp,
                                contentDescription = "Cerrar sesión",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Banner Informativo
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFD9E2DE)),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = AdminAccent.copy(alpha = 0.12f),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Filled.LocalHospital,
                                    contentDescription = null,
                                    tint = AdminAccent,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                "Control total del sistema",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = AdminPrimary
                            )
                            Text(
                                "Gestiona servicios, agenda, personal y pacientes de HuellitasVet.",
                                fontSize = 12.sp,
                                color = Color(0xFF5A6B66)
                            )
                        }
                    }
                }

                Text(
                    "Módulos de Gestión",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = AdminPrimary,
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Tarjetas de Módulos
                AdminModuloCard(
                    titulo = "Gestión de Servicios",
                    subtitulo = "Crear, editar, activar o desactivar servicios y tipos",
                    icono = Icons.Filled.RoomService,
                    colorFondo = Color(0xFFE8F5E9),
                    colorIcono = Color(0xFF2E7D32),
                    onClick = onServicios
                )

                AdminModuloCard(
                    titulo = "Agenda y Citas",
                    subtitulo = "Supervisar citas programadas, atendidas y horarios",
                    icono = Icons.Filled.EventNote,
                    colorFondo = Color(0xFFE3F2FD),
                    colorIcono = Color(0xFF1565C0),
                    onClick = onAgenda
                )

                AdminModuloCard(
                    titulo = "Pacientes y Mascotas",
                    subtitulo = "Consultar registros clínicos y propietarios",
                    icono = Icons.Filled.Pets,
                    colorFondo = Color(0xFFFFF3E0),
                    colorIcono = Color(0xFFEF6C00),
                    onClick = onPacientes
                )
            }
        }
    }
}

@Composable
private fun AdminModuloCard(
    titulo: String,
    subtitulo: String,
    icono: ImageVector,
    colorFondo: Color,
    colorIcono: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = AdminCard,
        border = BorderStroke(1.dp, Color(0xFFD9E2DE)),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = colorFondo,
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = colorIcono,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = AdminPrimary
                )
                Text(
                    text = subtitulo,
                    fontSize = 12.sp,
                    color = Color(0xFF5A6B66)
                )
            }
        }
    }
}
