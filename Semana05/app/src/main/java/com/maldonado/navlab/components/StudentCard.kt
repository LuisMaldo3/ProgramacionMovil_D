package com.maldonado.navlab.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maldonado.navlab.model.Alumno
import com.maldonado.navlab.ui.theme.Etiquetas
import com.maldonado.navlab.ui.theme.MoradoPrincipal
import com.maldonado.navlab.ui.theme.TarjetasDirectorio
import com.maldonado.navlab.ui.theme.TextoPrincipal

@Composable
fun StudentCard(
    alumno: Alumno,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 84.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = TarjetasDirectorio),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StudentAvatar(
                size = 52.dp,
                iconSize = 30.dp,
                photoResId = alumno.fotoResId
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = alumno.nombreCorto,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = alumno.carrera,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = MoradoPrincipal
                    ),
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Etiquetas,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
