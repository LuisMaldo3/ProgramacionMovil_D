
package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.theme.VerdeDueno

private val FondoSplash = Color(0xFFECFDF5)
private val TextoOscuro = Color(0xFF1F2937)

@Composable
fun SplashScreen(onContinuar: () -> Unit) {

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoSplash)
            .safeDrawingPadding()
    ) {
        val altura = maxHeight

        // Logo y nombre
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = altura * 0.15f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(104.dp)
                    .background(
                        VerdeDueno,
                        RoundedCornerShape(25.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                HuellaBlanca()
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = "HuellitasVet",
                color = TextoOscuro,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Cuidamos a tu mejor amigo",
                color = Color(0xFF718096),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }

        // Tres mascotas centrales
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = altura * 0.10f),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            listOf(
                Especie.PERRO to Color(0xFFFBE8C9),
                Especie.GATO to Color(0xFFE2E9F7),
                Especie.PERRO to Color(0xFFFFEACB)
            ).forEach { (especie, fondo) ->

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            fondo,
                            RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    MascotaAvatar(
                        especie = especie,
                        tamano = 64.dp
                    )
                }
            }
        }

        // Botones inferiores
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(
                    horizontal = 14.dp,
                    vertical = 18.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = onContinuar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeDueno
                )
            ) {
                Text(
                    "Comenzar",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }

            TextButton(onClick = onContinuar) {
                Text(
                    "Ya tengo una cuenta",
                    color = VerdeDueno,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun HuellaBlanca() {

    Canvas(Modifier.size(62.dp)) {

        val w = size.width

        drawOval(
            color = Color.White,
            topLeft = Offset(
                w * 0.25f,
                w * 0.43f
            ),
            size = Size(
                w * 0.50f,
                w * 0.39f
            )
        )

        listOf(
            Offset(w * 0.19f, w * 0.32f),
            Offset(w * 0.38f, w * 0.18f),
            Offset(w * 0.63f, w * 0.18f),
            Offset(w * 0.81f, w * 0.32f)
        ).forEach { punto ->

            drawCircle(
                color = Color.White,
                radius = w * 0.095f,
                center = punto
            )
        }
    }
}
