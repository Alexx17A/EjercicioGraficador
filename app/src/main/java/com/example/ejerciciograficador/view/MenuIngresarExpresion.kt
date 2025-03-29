package com.example.ejerciciograficador.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.regex.Pattern
import com.example.ejerciciograficador.controller.GraficadorController

class MenuIngresarExpresion : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MathExpressionScreen { expression ->
                // se crea un Intent para abrir la actividad donde se graficara la expresion
                val intent = Intent(this, GraphActivity::class.java) // TODO: REEMPLAZAR POR LA ACTIVIDAD DE GRAFICAR

                // se pasa la expresion ingresada como extra del intent a la siguiente actividad
                intent.putExtra("expression", expression)
                startActivity(intent)
            }
        }
    }
}


@Composable
fun MathExpressionScreen(onGraphClick: (String) -> Unit) {
    var expression by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf("") }

    // expresion regular para validar una expresion matematica con una sola variable x
    val expressionPattern = Pattern.compile("^[0-9x+\\-*/^() ]+\$")

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Ingresa expresion matematica",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = expression,
            onValueChange = {
                expression = it
                errorMessage = if (expressionPattern.matcher(it.text).matches()) "" else "Expresión no válida"
            },
            label = { Text("Introduce una expresion matematica") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                if (expressionPattern.matcher(expression.text).matches()) {
                    onGraphClick(expression.text)
                } else {
                    errorMessage = "Expresión no válida"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = errorMessage.isEmpty() // seshabilitar si la expresión no es valida
        ) {
            Text(
                text = "Graficar",
                fontSize = 18.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


// TODO: Reemplazar con la actividad correcta
// CUANDO HAGAN SU ACTIVIDAD DE GRAFICAR, PUEDEN IGNORAR ESTO POR COMPLETO Y
// ASEGURARSE QUE EN LA LINEA 35 APUNTEN A LA ACTIVIDAD DE GRAFICAR
class GraphActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val expression = intent.getStringExtra("expression") ?: ""
        setContent { GraphScreen(expression) }
    }
}

@Composable
fun GraphScreen(expression: String) {
    var func = GraficadorController()
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Expresión a graficar: $expression", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Expresion de infija a posfija: ${func.infijaAPostfija(expression)}", style = MaterialTheme.typography.headlineSmall)
    }

}