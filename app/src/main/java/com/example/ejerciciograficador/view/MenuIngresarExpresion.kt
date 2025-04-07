package com.example.ejerciciograficador.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
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
            MathExpressionScreen()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MathExpressionScreen() {
    // variables para almacenar los datos de la interfaz
    var puntosGraficar by remember { mutableStateOf<List<Pair<Double, Double>>?>(null) }
    var sliderValue by remember { mutableFloatStateOf(0f) }
    var expression by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf("") }
    val options = listOf("Punto", "Intervalo")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    var primerPunto by remember { mutableStateOf("") }
    var segundoPunto by remember { mutableStateOf("") }

    // patron para validar que la expresion solo contenga caracteres permitidos
    val expressionPattern = Pattern.compile("^[0-9x+\\-*/^() ]+\$")

    val controller = GraficadorController()

    Column(
        modifier = Modifier.fillMaxSize().
        padding(16.dp).
        verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center)
    {

        if (puntosGraficar != null) {
            GraficadorCanvas(puntos = puntosGraficar!!, expresion = expression.text)
        }

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
                Text("[ ", fontSize = 40.sp)
                OutlinedTextField(
                    value = primerPunto,
                    onValueChange = { if (it.all { ch -> ch.isDigit() }) primerPunto = it },
                    modifier = Modifier.width(60.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge
                )
                Text(" , ", fontSize = 30.sp)
                OutlinedTextField(
                    value = segundoPunto,
                    onValueChange = { if (it.all { ch -> ch.isDigit() }) segundoPunto = it },
                    modifier = Modifier.width(60.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge

                )
                Text(" ]", fontSize = 40.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // boton para graficar que valida y procesa la expresion
        Button(
            onClick = {
                if (expressionPattern.matcher(expression.text).matches()) {
                    val infija = expression.text
                    val postfija = controller.infijaAPostfija(expression.text)


                    val intent = Intent()
                    intent.putExtra("infija", infija)
                    intent.putExtra("postfija", postfija)

                    when (selectedOptionText) {
                        "Punto" -> {
                            val puntoCentral = sliderValue.toDouble()
                            // usar multiplicacion implicita nos dejar ingresar la expresion de la sig. manera 2x -> 2*x
                            val infijaConMultiplicacion = controller.insertarMultiplicacionImplicita(infija)
                            val postfija1 = controller.infijaAPostfija(infijaConMultiplicacion)
                            val y = controller.evaluarPostfija(postfija1, puntoCentral)
                            puntosGraficar = listOf(Pair(puntoCentral, y))
                            intent.putExtra("punto", sliderValue)
                        }


                        "Intervalo" -> {
                            val inicio = primerPunto.toDoubleOrNull() ?: 0.0
                            val fin = segundoPunto.toDoubleOrNull() ?: 0.0

                            if (inicio >= fin) {
                                errorMessage = "El intervalo debe ser válido (inicio < fin)"
                                return@Button
                            }

                            val puntos = controller.generarPuntosGrafica(
                                expression.text,
                                inicio,
                                fin,
                                0.1
                            )
                            puntosGraficar = puntos
                        }
                    }
                } else {
                    errorMessage = "Expresión no válida"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = errorMessage.isEmpty()
        ) {
            Text("Graficar", fontSize = 18.sp)
        }


    }
}
@Composable
fun GraficadorCanvas(puntos: List<Pair<Double, Double>>, expresion: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Text(text = "Gráfica de: $expresion")
        Spacer(modifier = Modifier.height(8.dp))

        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)) {

            val ancho = size.width
            val alto = size.height

            var minX = puntos.minOfOrNull { it.first } ?: 0.0
            var maxX = puntos.maxOfOrNull { it.first } ?: 1.0
            var minY = puntos.minOfOrNull { it.second } ?: 0.0
            var maxY = puntos.maxOfOrNull { it.second } ?: 1.0

            // expandir el rango si solo hay un punto
            if (minX == maxX) {
                minX -= 1
                maxX += 1
            }
            if (minY == maxY) {
                minY -= 1
                maxY += 1
            }

            val escalaX = if ((maxX - minX) != 0.0) ancho / (maxX - minX).toFloat() else 1f
            val escalaY = if ((maxY - minY) != 0.0) alto / (maxY - minY).toFloat() else 1f

            fun transformar(x: Double, y: Double): Offset {
                val px = ((x - minX) * escalaX)
                val py = alto - ((y - minY) * escalaY)
                return Offset(px.toFloat(), py.toFloat())
            }

            // ejes
            val ejeX = if (minY <= 0.0 && maxY >= 0.0) transformar(minX, 0.0).y else alto
            val ejeY = if (minX <= 0.0 && maxX >= 0.0) transformar(0.0, minY).x else 0f

            drawLine(Color.Gray, start = Offset(0f, ejeX), end = Offset(ancho, ejeX), strokeWidth = 2f)
            drawLine(Color.Gray, start = Offset(ejeY, 0f), end = Offset(ejeY, alto), strokeWidth = 2f)


            // etiquetas x
            for (i in minX.toInt()..maxX.toInt()) {
                val x = transformar(i.toDouble(), 0.0).x
                drawLine(Color.LightGray, Offset(x, ejeX - 5), Offset(x, ejeX + 5), strokeWidth = 1f)
                drawContext.canvas.nativeCanvas.drawText(
                    "$i", x, ejeX + 20,
                    android.graphics.Paint().apply {
                        textSize = 24f
                        color = android.graphics.Color.BLACK
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }

            // etiquetas y
            for (i in minY.toInt()..maxY.toInt()) {
                val y = transformar(0.0, i.toDouble()).y
                drawLine(Color.LightGray, Offset(ejeY - 5, y), Offset(ejeY + 5, y), strokeWidth = 1f)
                drawContext.canvas.nativeCanvas.drawText(
                    "$i", ejeY - 10, y + 10,
                    android.graphics.Paint().apply {
                        textSize = 24f
                        color = android.graphics.Color.BLACK
                        textAlign = android.graphics.Paint.Align.RIGHT
                    }
                )
            }

            if (puntos.size == 1) {
                val (x, y) = puntos[0]
                drawCircle(
                    color = Color.Red,
                    radius = 8f,
                    center = transformar(x, y)
                )
            } else {
                puntos.zipWithNext { (x1, y1), (x2, y2) ->
                    drawLine(
                        color = Color.Red,
                        start = transformar(x1, y1),
                        end = transformar(x2, y2),
                        strokeWidth = 2f
                    )
                }
            }
        }
    }
}