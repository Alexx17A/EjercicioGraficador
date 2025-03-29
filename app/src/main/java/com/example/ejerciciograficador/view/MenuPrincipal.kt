package com.example.ejerciciograficador.view

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
@Composable
fun MenuPrincipal(navController: NavHostController) {

    val context = LocalContext.current

    val integrantes = listOf(
        "pongan aqui su nombre jodido jeje",
        "Jose Manuel Chable Gomez",
        "Osiel Alejandro Perez Barroso",
        "Fernando Gamaliel Rodriguez Torres",
        "pongan aqui su nombre jodido jeje",
        "pongan aqui su nombre jodido jeje"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Título de la aplicación
        Text(
            text = "Graficador de Funciones",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF3F51B5),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        // Lista de integrantes con diseño de tarjetas
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(integrantes) { integrante ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Text(
                        text = integrante,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp),
                        color = Color(0xFF333333)
                    )
                }
            }
        }

        // Botón para ir al graficador
        Button(
            // AQUI HICE CAMBIO PARA QUE APUNTE A LA ACTIVIDAD DE INGRESAR EXPRESION - CHABLE
            onClick = {
                context.startActivity(Intent(context, MenuIngresarExpresion::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Abrir Graficador",
                fontSize = 18.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}