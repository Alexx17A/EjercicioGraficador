package com.example.ejerciciograficador.model

/**
 * Esta clase representa el modelo del graficador
 * @param expresion - Guarda la expresión matemática que se quiere graficar
 * @param puntos - Esta es una lista de puntos (pares de valores x, y) que se calcula
 * a partir de la expresión
 */

data class Graficador(
    val expresion: String, // Aqui guardamos la expresion infija que escribe el usuario, por ejemplo: "x+2"
    val puntos: List<Pair<Double, Double>> // Esta es una lista de puntos (x, y) que representan los puntos que se van a graficar
)
