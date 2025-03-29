package com.example.ejerciciograficador.controller
import com.example.ejerciciograficador.model.Pila
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
}