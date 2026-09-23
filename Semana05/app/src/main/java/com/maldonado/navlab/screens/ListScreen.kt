package com.maldonado.navlab.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maldonado.navlab.components.AcademicTopBar
import com.maldonado.navlab.components.StudentCard
import com.maldonado.navlab.model.AlumnoRepository
import com.maldonado.navlab.navigation.Screen
import com.maldonado.navlab.ui.theme.FondoPrincipal
import com.maldonado.navlab.ui.theme.LilaClaro
import com.maldonado.navlab.ui.theme.MoradoOscuro

@Composable
fun ListScreen(navController: NavController) {
    Scaffold(
        containerColor = FondoPrincipal,
        topBar = {
            AcademicTopBar(
                title = "Directorio de Alumnos",
                onBackClick = { navController.popBackStack() },
                containerColor = LilaClaro,
                contentColor = MoradoOscuro,
                contentHeight = 64.dp
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = AlumnoRepository.alumnos,
                key = { it.id }
            ) { alumno ->
                StudentCard(
                    alumno = alumno,
                    onClick = {
                        navController.navigate(Screen.Detail.createRoute(alumno.id))
                    }
                )
            }
        }
    }
}
