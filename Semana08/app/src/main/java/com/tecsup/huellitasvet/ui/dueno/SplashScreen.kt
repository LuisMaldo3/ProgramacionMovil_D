package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.R

private val VERDE = Color(0xFF087F6D)

@Composable
fun SplashScreen(onContinuar: () -> Unit) {
    BoxWithConstraints(Modifier.fillMaxSize().background(Color(0xFFF4FCF9)).safeDrawingPadding()) {
        val alto = maxHeight
        // Imagen genuina del paquete: no contiene botones ni textos rasterizados.
        Image(painter = painterResource(R.drawable.splash_fondo), contentDescription = "Perro, gato y conejo",
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        Box(Modifier.fillMaxWidth().height(alto * 0.24f).align(Alignment.TopCenter)
            .background(Brush.verticalGradient(listOf(Color(0xFFF2FCF8), Color(0xFFF2FCF8).copy(alpha = 0f)))))
        Column(
            modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth()
                .padding(top = 28.dp, start = 22.dp, end = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(shape = RoundedCornerShape(23.dp), color = VERDE, shadowElevation = 5.dp) {
                Icon(Icons.Filled.Pets, "HuellitasVet", tint = Color.White,
                    modifier = Modifier.padding(16.dp).size(40.dp))
            }
            Spacer(Modifier.height(11.dp))
            Text("HuellitasVet", fontSize = 29.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF173A36))
            Text("Cuidamos a quienes más amas", fontSize = 14.sp, textAlign = TextAlign.Center,
                color = Color(0xFF506A66))
        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xFFF2FCF8).copy(alpha = .92f))))
                .padding(start = 22.dp, end = 22.dp, bottom = 22.dp, top = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onContinuar,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VERDE)
            ) { Text("Comenzar", fontSize = 17.sp, fontWeight = FontWeight.Bold) }
            OutlinedButton(onClick = onContinuar, modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = VERDE)) {
                Text("Ya tengo una cuenta", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
