package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.ui.theme.VerdeDueno

private val MENTA = Color(0xFFECFDF5)

@Composable
fun SplashScreen(onContinuar: () -> Unit) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize().background(MENTA).safeDrawingPadding()
    ) {
        val alto = maxHeight
        Column(
            modifier = Modifier.align(Alignment.TopCenter)
                .fillMaxWidth().padding(top = alto * 0.15f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(104.dp)
                    .background(VerdeDueno, RoundedCornerShape(25.dp)),
                contentAlignment = Alignment.Center
            ) { HuellaSplash() }
            Spacer(Modifier.height(17.dp))
            Text("HuellitasVet", color = Color(0xFF1F2937), fontSize = 27.sp,
                fontWeight = FontWeight.Bold)
            Text("Cuidamos a tu mejor amigo", color = Color(0xFF718096),
                fontSize = 12.sp, textAlign = TextAlign.Center)
        }

        // Tres personajes distintos: OSO, PERRO y GATO.
        Row(
            modifier = Modifier.align(Alignment.Center).offset(y = alto * 0.10f),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimalSplash(tipo = 0, fondo = Color(0xFFFCE8C8))  // Oso
            AnimalSplash(tipo = 1, fondo = Color(0xFFE2E8F3))  // Perro
            AnimalSplash(tipo = 2, fondo = Color(0xFFFFE8CD))  // Gato
        }

        Column(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onContinuar,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeDueno)
            ) { Text("Comenzar", fontSize = 15.sp, fontWeight = FontWeight.SemiBold) }
            TextButton(onClick = onContinuar) {
                Text("Ya tengo una cuenta", color = VerdeDueno, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun HuellaSplash() {
    Canvas(Modifier.size(62.dp)) {
        val w = size.width
        drawOval(Color.White, topLeft = Offset(w * 0.25f, w * 0.43f),
            size = Size(w * 0.50f, w * 0.39f))
        listOf(
            Offset(w * 0.19f, w * 0.32f), Offset(w * 0.38f, w * 0.18f),
            Offset(w * 0.63f, w * 0.18f), Offset(w * 0.81f, w * 0.32f)
        ).forEach { drawCircle(Color.White, radius = w * 0.095f, center = it) }
    }
}

@Composable
private fun AnimalSplash(tipo: Int, fondo: Color) {
    val pelaje = when (tipo) {
        0 -> Color(0xFFD69B63) // oso
        1 -> Color(0xFF98A6B8) // perro
        else -> Color(0xFFEDBF83) // gato
    }
    Box(
        modifier = Modifier.size(80.dp).background(fondo, RoundedCornerShape(19.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.size(58.dp)) {
            val w = size.width
            if (tipo == 0) {
                drawCircle(pelaje, w * 0.17f, Offset(w * 0.20f, w * 0.24f))
                drawCircle(pelaje, w * 0.17f, Offset(w * 0.80f, w * 0.24f))
            } else if (tipo == 1) {
                drawOval(pelaje.copy(alpha = 0.9f), Offset(w * 0.04f, w * 0.22f), Size(w * 0.26f, w * 0.43f))
                drawOval(pelaje.copy(alpha = 0.9f), Offset(w * 0.70f, w * 0.22f), Size(w * 0.26f, w * 0.43f))
            } else {
                val left = Path().apply {
                    moveTo(w * 0.17f, w * 0.37f)
                    lineTo(w * 0.18f, w * 0.06f)
                    lineTo(w * 0.43f, w * 0.25f)
                    close()
                }
                val right = Path().apply {
                    moveTo(w * 0.57f, w * 0.25f)
                    lineTo(w * 0.82f, w * 0.06f)
                    lineTo(w * 0.83f, w * 0.37f)
                    close()
                }
                drawPath(left, pelaje)
                drawPath(right, pelaje)
            }
            drawCircle(pelaje, w * 0.36f, Offset(w * 0.50f, w * 0.51f))
            val ojo = Color(0xFF253344)
            drawCircle(ojo, w * 0.035f, Offset(w * 0.38f, w * 0.48f))
            drawCircle(ojo, w * 0.035f, Offset(w * 0.62f, w * 0.48f))
            drawOval(Color(0xFF5A4B4B), Offset(w * 0.455f, w * 0.59f), Size(w * 0.09f, w * 0.055f))
            drawCircle(Color(0xFFFFFAF4).copy(alpha = 0.32f), w * 0.055f, Offset(w * 0.31f, w * 0.60f))
            drawCircle(Color(0xFFFFFAF4).copy(alpha = 0.32f), w * 0.055f, Offset(w * 0.69f, w * 0.60f))
        }
    }
}
