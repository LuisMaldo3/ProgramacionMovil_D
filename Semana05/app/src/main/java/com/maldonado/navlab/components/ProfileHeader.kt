package com.maldonado.navlab.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maldonado.navlab.ui.theme.GradientProfileRight
import com.maldonado.navlab.ui.theme.MoradoPrincipal

@Composable
fun ProfileHeader(
    fullName: String,
    modifier: Modifier = Modifier,
    photoResId: Int? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 160.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(MoradoPrincipal, GradientProfileRight)
                ),
                shape = RectangleShape
            )
            .padding(vertical = 18.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StudentAvatar(
                size = 76.dp,
                iconSize = 42.dp,
                photoResId = photoResId,
                borderWidth = 3.dp,
                borderColor = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = fullName,
                style = TextStyle(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}
