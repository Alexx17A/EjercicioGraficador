package com.example.ejerciciograficador.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MathExpressionScreen(onGraphClick: (String) -> Unit) {
    var sliderValue by remember { mutableStateOf(0f) }
    var expression by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf("") }
    val options = listOf("Punto", "Intervalo")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    var primerPunto by remember { mutableStateOf("") }
    var segundoPunto by remember { mutableStateOf("") }

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
        Spacer(modifier = Modifier.height(5.dp))
        Row(

        ) {
            //dropdown menu para elegir que como evaluar la funcion
            ExposedDropdownMenuBox(
                expanded = expanded,
                modifier = Modifier.padding(horizontal = 0.dp, vertical = 15.dp),
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = selectedOptionText,
                    onValueChange = { },
                    label = { Text("(elige mediante que funcion evaluar)") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(text = selectionOption) },
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        Text(
            text = "Elige el punto entre 0 a 10",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 5.dp),
            value = sliderValue,
            onValueChange = { sliderValue = it },
            valueRange = 0f..10f,
            steps = 9 // Para obtener pasos enteros del 0 al 10
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "[ ",
                fontSize = 20.sp,
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                value = primerNumero,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) firstNumber = it
                },
                modifier = Modifier.width(60.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Text(
                text = ", ",
                fontSize = 20.sp,
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                value = secondNumber,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) secondNumber = it
                },
                modifier = Modifier.width(60.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Text(
                text = " ]",
                fontSize = 20.sp,
                style = MaterialTheme.typography.headlineMedium
            )
        }
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