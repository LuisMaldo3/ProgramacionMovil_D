package com.tecsup.huellitasvet.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tecsup.huellitasvet.R

/** Paleta compartida. Los colores de contenido real respetan MaterialTheme para modo oscuro. */
val PremiumVerde = Color(0xFF087F6D)
val PremiumAzul = Color(0xFF2458DE)
val PremiumTexto = Color(0xFF172B3A)

@Composable
fun AvatarPersonaPremium(veterinario: Boolean, tamano: Dp = 78.dp, correo: String = "") {
    val acento = if (veterinario) PremiumAzul else PremiumVerde
    val recurso = when {
        veterinario -> R.drawable.avatar_veterinario
        correo.equals("ana@correo.test", ignoreCase = true) -> R.drawable.avatar_duena
        correo.equals("luis@correo.test", ignoreCase = true) -> R.drawable.avatar_dueno
        else -> null
    }
    Surface(modifier = Modifier.size(tamano), shape = CircleShape,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(3.dp, Color.White.copy(alpha=.95f)), shadowElevation = 5.dp) {
        if (recurso != null) {
            Image(painter = painterResource(recurso),
                contentDescription = "Avatar ilustrativo del perfil",
                modifier = Modifier.fillMaxSize().background(acento.copy(alpha = .12f)),
                contentScale = ContentScale.Crop)
        } else {
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize().background(acento.copy(alpha=.11f))) {
                Icon(Icons.Filled.Person, "Icono de perfil", tint = acento,
                    modifier = Modifier.size(tamano * .52f))
            }
        }
    }
}

@Composable
fun EncabezadoPersonaPremium(
    nombre: String,
    correo: String,
    veterinario: Boolean
) {
    val color = if (veterinario) PremiumAzul else PremiumVerde
    val inicio = if (veterinario) Color(0xFF143D92) else Color(0xFF086A5C)
    val fin = if (veterinario) Color(0xFF3477F0) else Color(0xFF19AD91)
    Surface(shape = RoundedCornerShape(26.dp), shadowElevation = 5.dp) {
        Column(Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(inicio, fin)))
            .padding(horizontal = 20.dp, vertical = 23.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                AvatarPersonaPremium(veterinario, 84.dp, correo)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(if (veterinario) "MI ESPACIO PROFESIONAL" else "MI CUENTA",
                        color = Color.White.copy(alpha = .84f), fontSize = 12.sp, fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp)
                    Text(nombre.ifBlank { if (veterinario) "Veterinario" else "Dueño de mascota" },
                        color = Color.White, style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text(correo.ifBlank { "Cuenta local" }, color = Color.White.copy(alpha=.9f),
                        fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                Surface(shape = RoundedCornerShape(50.dp), color = Color.White.copy(alpha = .18f)) {
                    Row(Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(if (veterinario) Icons.Filled.Person else Icons.Filled.CheckCircle,
                            contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Text(if (veterinario) "Veterinario activo" else "Perfil de dueño activo",
                            color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun TituloSeccionPremium(titulo: String, subtitulo: String? = null) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(titulo, style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
        if (!subtitulo.isNullOrBlank()) Text(subtitulo, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun PreferenciaPremium(
    icono: ImageVector,
    titulo: String,
    descripcion: String,
    valor: Boolean,
    acento: Color,
    onCambio: (Boolean) -> Unit
) {
    Row(Modifier.fillMaxWidth().heightIn(min = 79.dp).padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Surface(shape = RoundedCornerShape(15.dp), color = acento.copy(alpha = .11f)) {
            Icon(icono, null, tint = acento,
                modifier = Modifier.padding(12.dp).size(23.dp))
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(titulo, fontSize = 16.sp, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface)
            Text(descripcion, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = valor, onCheckedChange = onCambio,
            colors = SwitchDefaults.colors(checkedTrackColor = acento))
    }
}

@Composable
fun IndicadorPremium(
    numero: Int,
    etiqueta: String,
    icono: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.heightIn(min = 119.dp), shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = .115f)) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 13.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(icono, contentDescription = null, tint = color, modifier = Modifier.size(23.dp))
            Text(numero.toString(), color = color, fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
            Text(etiqueta, color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center, maxLines = 2)
        }
    }
}

/** Diálogo de éxito REUTILIZABLE. La animación comienza solo tras confirmar la operación en el ViewModel. */
@Composable
fun DialogoExitoAnimado(
    titulo: String,
    descripcion: String,
    boton: String,
    detalle: String? = null,
    onContinuar: () -> Unit
) {
    var iniciado by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { iniciado = true }
    val escala by animateFloatAsState(
        targetValue = if (iniciado) 1f else 0.45f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "check de confirmación"
    )
    Dialog(onDismissRequest = { }, properties = DialogProperties(dismissOnBackPress = false,
        dismissOnClickOutside = false)) {
        Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(28.dp), shadowElevation = 12.dp) {
            Column(Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)) {
                Box(Modifier.size(116.dp), contentAlignment = Alignment.Center) {
                    Surface(shape = CircleShape, color = Color(0xFFDCFCE7),
                        modifier = Modifier.size(102.dp).graphicsLayer {
                            scaleX = escala; scaleY = escala
                        }) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.Check, contentDescription = "Operación completada",
                                tint = Color(0xFF047857), modifier = Modifier.size(62.dp))
                        }
                    }
                }
                AnimatedVisibility(visible = iniciado,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 3 })) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(titulo, style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center)
                        Text(descripcion, style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center)
                    }
                }
                if (!detalle.isNullOrBlank()) {
                    Surface(shape = RoundedCornerShape(15.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha=.5f)) {
                        Text(detalle, modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 15.dp, vertical = 12.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center)
                    }
                }
                Button(onClick = onContinuar, modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(15.dp)) {
                    Text(boton, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(7.dp))
                    Icon(Icons.Filled.ArrowForwardIos, contentDescription = null,
                        modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}
