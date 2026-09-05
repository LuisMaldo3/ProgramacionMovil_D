package com.maldonado.lab03tarea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maldonado.lab03tarea.ui.theme.Lab03TareaTheme
import kotlin.math.roundToInt

val Morado = Color(0xFF6A1B9A)
val MoradoOscuro = Color(0xFF4A148C)
val MoradoClaro = Color(0xFFEDE7F6)
val MoradoDeshabilitado = Color(0xFFCE93D8)

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab03TareaTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Registro de Notas", fontWeight = FontWeight.Bold) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Morado,
                                titleContentColor = Color.White
                            )
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    PantallaNotas(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PantallaNotas(modifier: Modifier = Modifier) {

    var notaFundamentos by remember { mutableStateOf(0f) }
    var notaPOO by remember { mutableStateOf(0f) }
    var notaMoviles by remember { mutableStateOf(0f) }
    var notaBD by remember { mutableStateOf(0f) }

    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }
    var mostrarResultado by remember { mutableStateOf(false) }

    val degradado = Brush.verticalGradient(
        colors = listOf(Color(0xFFF3E5F5), Color(0xFFFFFFFF))
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(brush = degradado)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text("Notas del ciclo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("Desliza para asignar cada nota (0 a 20)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)

        Spacer(modifier = Modifier.height(12.dp))

        FilaCurso("Fundamentos de Programaciónn", "20%", notaFundamentos) { notaFundamentos = it }
        FilaCurso("Programación Orintada a Objetos", "25%", notaPOO) { notaPOO = it }
        FilaCurso("Programación en Móviles", "30%", notaMoviles) { notaMoviles = it }
        FilaCurso("Base de Datos", "25%", notaBD) { notaBD = it }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Redondear promedio final", style = MaterialTheme.typography.bodyMedium)
            Switch(
                checked = redondear,
                onCheckedChange = { redondear = it },
                colors = SwitchDefaults.colors(checkedTrackColor = Morado)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = confirmado,
                onCheckedChange = { confirmado = it },
                colors = CheckboxDefaults.colors(checkedColor = Morado)
            )
            Text("Confirmo que las notas son correctas", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { mostrarResultado = true },
            modifier = Modifier.fillMaxWidth(),
            enabled = confirmado,
            colors = ButtonDefaults.buttonColors(
                containerColor = MoradoOscuro,
                disabledContainerColor = MoradoDeshabilitado
            )
        ) {
            Text("CALCULAR PROMEDIO", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (!mostrarResultado) {
            Text(
                text = "Asigna las notas y confirma para calcular",
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.bodySmall
            )
        } else {
            val ponderado = notaFundamentos * 0.20f +
                    notaPOO * 0.25f +
                    notaMoviles * 0.30f +
                    notaBD * 0.25f

            val promedioFinal = if (redondear) ponderado.roundToInt().toFloat() else ponderado

            val (observacion, colorChip) = when {
                promedioFinal >= 17f -> "EXCELENTE" to Color(0xFF1B5E20)
                promedioFinal >= 13f -> "APROBADO" to Color(0xFF4CAF50)
                promedioFinal >= 10f -> "EN RECUPERACIÓN" to Color(0xFFFFC107)
                else -> "DESAPROBADO" to Color(0xFFF44336)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Promedio ponderado:  ${"%.2f".format(ponderado)}", style = MaterialTheme.typography.bodyMedium)
                    if (redondear) {
                        Text(
                            "Promedio final:  ${promedioFinal.toInt()}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MoradoOscuro
                        )
                        Text("(redondeado)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                    } else {
                        Text(
                            "Promedio final:  ${"%.2f".format(promedioFinal)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MoradoOscuro
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(colorChip, shape = RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(observacion, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Promedio calculado correctamente", color = Color(0xFF2E7D32), fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            "Desarrollado por: Luis Maldonado",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilaCurso(nombre: String, peso: String, nota: Float, onNotaChange: (Float) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Text(nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(4.dp))
                Text("($peso)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            }
            Box(
                modifier = Modifier
                    .background(MoradoClaro, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(nota.toInt().toString(), color = MoradoOscuro, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
        Slider(
            value = nota,
            onValueChange = { onNotaChange(it.toInt().toFloat()) },
            valueRange = 0f..20f,
            modifier = Modifier.fillMaxWidth(),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Morado, shape = CircleShape)
                )
            },
            track = { sliderState ->
                val fraccion = (sliderState.value - sliderState.valueRange.start) /
                        (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(MoradoDeshabilitado, shape = CircleShape)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraccion)
                            .fillMaxHeight()
                            .background(Morado, shape = CircleShape)
                    )
                }
            }
        )
    }
}