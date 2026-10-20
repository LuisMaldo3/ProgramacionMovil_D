package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.theme.VerdeDueno
import kotlinx.coroutines.delay

/** [ENTREGADO] Splash inicial; por ahora lleva a Servicios para probar el proyecto base. */
@Composable
fun SplashScreen(onContinuar: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1500)
        onContinuar()
    }
    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFFECFDF5), Color(0xFFB3F3D9), Color(0xFF85E6C1)))
        ).safeDrawingPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(
                modifier = Modifier.size(128.dp).background(Color.White, RoundedCornerShape(34.dp)),
                contentAlignment = Alignment.Center
            ) { MascotaAvatar(especie = Especie.PERRO, tamano = 110.dp) }
            Text("HuellitasVet", fontSize = 33.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF065F46))
            Text("Cuidamos a quienes más quieres", color = Color(0xFF047857), textAlign = TextAlign.Center, fontSize = 15.sp)
            Spacer(Modifier.height(28.dp))
            Text("Bienvenido", style = MaterialTheme.typography.titleMedium, color = VerdeDueno)
        }
        Text(
            "Laboratorio 08 · MVVM + Room",
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 28.dp),
            color = Color(0xFF047857), fontSize = 12.sp
        )
    }
}
