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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ejerciciograficador.controller.GraficadorController
import java.util.regex.Pattern

class MenuIngresarExpresion : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MathExpressionScreen { intent ->
                // TODO: REEMPLZAR POR LA ACTIVIDAD QUE QUIERAS ABRIR DESPUES DE INGRESAR LA EXPRESION
                intent.setClass(this, GraphActivity::class.java)
                startActivity(intent)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MathExpressionScreen(onGraphClick: (Intent) -> Unit) {
    // variables para almacenar los datos de la interfaz
    var sliderValue by remember { mutableFloatStateOf(0f) }
    var expression by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf("") }
    val options = listOf("Punto", "Intervalo")  // TODO: MODIFICAR OPCIONES SEGUN LA ACTIVIDAD DESEADA
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    var primerPunto by remember { mutableStateOf("") }
    var segundoPunto by remember { mutableStateOf("") }
    val context = LocalContext.current

    // patron para validar que la expresion solo contenga caracteres permitidos
    val expressionPattern = Pattern.compile("^[0-9x+\\-*/^() ]+\$")

    val controller = GraficadorController()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
        Text("Ingresa expresión matemática", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // campo para ingresar la expresion matematica con validacion
        OutlinedTextField(
            value = expression,
            onValueChange = {
                expression = it
                errorMessage = if (expressionPattern.matcher(it.text).matches()) "" else "Expresión no válida"
            },
            label = { Text("Introduce una expresión matemática") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // dropdown para elegir entre punto o intervalo
        // TODO: MODIFICAR EL DROPDOWN PARA MOSTRAR OPCIONES RELEVANTES A TU ACTIVIDAD
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedOptionText,
                onValueChange = {},
                readOnly = true,
                label = { Text("Elegir tipo de evaluación") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedOptionText = option
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // elegir un punto especifico con un slider
        if (selectedOptionText == "Punto") {
            Text("Elige el punto entre 0 y 10: (${sliderValue.toInt()})", fontSize = 18.sp)
            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                valueRange = 0f..10f,
                steps = 9,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // ingresar un intervalo con dos campos numericos
        if (selectedOptionText == "Intervalo") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("[ ", fontSize = 30.sp)
                OutlinedTextField(
                    value = primerPunto,
                    onValueChange = { if (it.all { ch -> ch.isDigit() }) primerPunto = it },
                    modifier = Modifier.width(60.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                Text(", ", fontSize = 20.sp)
                OutlinedTextField(
                    value = segundoPunto,
                    onValueChange = { if (it.all { ch -> ch.isDigit() }) segundoPunto = it },
                    modifier = Modifier.width(60.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                Text(" ]", fontSize = 30.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // boton para graficar que valida y procesa la expresion
        Button(
            onClick = {
                if (expressionPattern.matcher(expression.text).matches()) {
                    val infija = expression.text
                    // se convierte la expresion infija a postfija usando el controlador
                    val postfija = controller.infijaAPostfija(infija)

                    // se prepara el intent con los datos
                    val intent = Intent(context, GraphActivity::class.java).apply {
                        putExtra("infija", infija)
                        putExtra("postfija", postfija)

                        // se agregan extras diferentes segun la opcion elegida
                        if (selectedOptionText == "Punto") {
                            putExtra("punto", sliderValue)
                        } else if (selectedOptionText == "Intervalo") {
                            val inicio = primerPunto.toDoubleOrNull() ?: 0.0
                            val fin = segundoPunto.toDoubleOrNull() ?: 0.0
                            putExtra("primerPunto", inicio)
                            putExtra("segundoPunto", fin)
                        }
                    }

                    onGraphClick(intent)
                } else {
                    errorMessage = "Expresión no válida"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = errorMessage.isEmpty()
        ) {
            // TODO: CAMBIAR EL TEXTO DEL BOTON SEGUN TU ACTIVIDAD
            Text("Graficar", fontSize = 18.sp)
        }
    }
}



// Actividad placeholder para mostrar la expresion procesada
// TODO: IMPLEMENTAR TU PROPIA ACTIVIDAD DE GRAFICACION O PROCESAMIENTO DE EXPRESIONES
// CUANDO HAGAS TU ACTIVIDAD, PUEDES IGNORAR ESTO POR COMPLETO Y
// ASEGURARTE QUE EN LA LINEA DE ARRIBA (intent.setClass) APUNTES A TU ACTIVIDAD
class GraphActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // recuperar los datos del intent
        val infija = intent.getStringExtra("infija") ?: ""
        val postfija = intent.getStringExtra("postfija") ?: ""
        val punto = intent.getFloatExtra("punto", -1f)
        val primerPunto = intent.getDoubleExtra("primerPunto", 0.0)
        val segundoPunto = intent.getDoubleExtra("segundoPunto", 0.0)

        setContent {
            GraphScreen(
                infija = infija,
                postfija = postfija,
                punto = punto,
                primerPunto = primerPunto,
                segundoPunto = segundoPunto
            )
        }
    }
}

// pantalla simple que muestra los datos recibidos
// ESTO SOLO ES UN EJEMPLO DE COMO PUEDES LLAMAR LAS VARIABLES MANDADAS DESDE LA VISTA DE INGRESAR EXPRESIONES
// TODO: REEMPLAZAR ESTA PANTALLA CON TU IMPLEMENTACION DE GRAFICO U OTRA FUNCIONALIDAD
@Composable
fun GraphScreen(
    infija: String,
    postfija: String,
    punto: Float,
    primerPunto: Double,
    segundoPunto: Double
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Expresión infija: $infija", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Expresión postfija: $postfija", style = MaterialTheme.typography.headlineSmall)

        if (punto >= 0) {
            Text(text = "Evaluar en punto: $punto", style = MaterialTheme.typography.bodyLarge)
        }

        if (primerPunto >= 0 && segundoPunto >= 0) {
            Text(
                text = "Evaluar en intervalo: [$primerPunto - $segundoPunto]",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}