package com.example.ejerciciograficador.controller

import com.example.ejerciciograficador.model.Pila
import kotlin.math.pow

class GraficadorController {
    fun infijaAPostfija(expresion: String): String {
        val pila = Pila<Char>()
        val postfija = StringBuilder()

        // prioridad de los operadores
        val prioridad = mapOf(
            '+' to 1,
            '-' to 1,
            '*' to 2,
            '/' to 2,
            '^' to 3
        )

        for (caracter in expresion) {
            when {
                caracter.isLetterOrDigit() -> {
                    // Si es operando, agregar a la expresión postfija
                    postfija.append(caracter)
                }

                caracter == '(' -> {
                    //  Si es parentesis izquierdo, meterlo en la pila
                    pila.push(caracter)
                }

                caracter == ')' -> {
                    // Paso 4: Si es paréntesis derecho
                    while (!pila.estaVacia() && pila.peek() != '(') {
                        postfija.append(pila.pop())
                    }
                    if (!pila.estaVacia() && pila.peek() == '(') {
                        pila.pop() // Eliminar el paréntesis izquierdo
                    }
                }

                caracter in prioridad -> {
                    // 3.2  si es operador
                    while (!pila.estaVacia() && pila.peek() != '(' &&
                        prioridad[caracter]!! <= prioridad[pila.peek()!!]!!
                    ) {
                        postfija.append(pila.pop())
                    }
                    pila.push(caracter)
                }
            }
        }

        // Paso 5: Vaciar la pila
        while (!pila.estaVacia()) {
            postfija.append(pila.pop())
        }

        return postfija.toString()
    }

    fun evaluarPostfija(expresionPostfija: String, valorX: Double): Double {
        val pila = Pila<Double>()

        for (caracter in expresionPostfija) {
            when {
                caracter.isDigit() -> {
                    pila.push(caracter.toString().toDouble())
                }
                caracter == 'x' -> {
                    pila.push(valorX)
                }
                else -> {
                    val operando2 = pila.pop() ?: 0.0
                    val operando1 = pila.pop() ?: 0.0
                    val resultado = when (caracter) {
                        '+' -> operando1 + operando2
                        '-' -> operando1 - operando2
                        '*' -> operando1 * operando2
                        '/' -> operando1 / operando2
                        '^' -> operando1.pow(operando2)
                        else -> throw IllegalArgumentException("Operador no válido: $caracter")
                    }
                    pila.push(resultado)
                }
            }
        }

        return pila.pop() ?: 0.0
    }

    //con esta funcion se deben mostrar los puntos en la grafica (que aun no existe)
    fun generarPuntosGrafica(expresionInfija: String, rangoInicio: Double, rangoFin: Double, paso: Double): List<Pair<Double, Double>> {
        val expresionConMultiplicacion = insertarMultiplicacionImplicita(expresionInfija)
        val postfija = infijaAPostfija(expresionConMultiplicacion)
        val puntos = mutableListOf<Pair<Double, Double>>()

        var x = rangoInicio
        while (x <= rangoFin) {
            val y = evaluarPostfija(postfija, x)
            puntos.add(Pair(x, y))
            x += paso
        }

        return puntos
    }


    // ✅ Nueva función para evaluar múltiples puntos y devolver un arreglo bidimensional
    fun evaluarRango(expresionPostfija: String, rangoInicio: Double, rangoFin: Double, paso: Double): Array<Array<Double>> {
        val puntos = mutableListOf<Array<Double>>()
        var x = rangoInicio

        while (x <= rangoFin) {
            val y = evaluarPostfija(expresionPostfija, x)
            puntos.add(arrayOf(x, y))
            x += paso
        }

        return puntos.toTypedArray()
    }

    fun insertarMultiplicacionImplicita(expresion: String): String {
        val resultado = StringBuilder()

        for (i in expresion.indices) {
            val actual = expresion[i]
            resultado.append(actual)

            if (i < expresion.length - 1) {
                val siguiente = expresion[i + 1]

                // Casos comunes de multiplicación implícita:
                if ((actual.isDigit() || actual == 'x' || actual == ')') &&
                    (siguiente == 'x' || siguiente == '(')
                ) {
                    resultado.append('*')
                }
            }
        }

        return resultado.toString()
    }


}