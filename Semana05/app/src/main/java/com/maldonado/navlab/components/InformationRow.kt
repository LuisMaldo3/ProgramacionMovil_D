package com.maldonado.navlab.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maldonado.navlab.ui.theme.Etiquetas
import com.maldonado.navlab.ui.theme.MoradoPrincipal
import com.maldonado.navlab.ui.theme.TarjetasDirectorio
import com.maldonado.navlab.ui.theme.TextoPrincipal
import com.maldonado.navlab.ui.theme.TextoSecundario

@Composable
fun DetailInformationRow(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MoradoPrincipal,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 10.sp,
                    color = Etiquetas
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextoPrincipal
                )
            )
        }
    }
}

@Composable
fun ProfileInformationRow(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(TarjetasDirectorio, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TextoSecundario,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 10.sp,
                    color = Etiquetas
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextoPrincipal
                ),
                maxLines = 2
            )
        }
    }
}
