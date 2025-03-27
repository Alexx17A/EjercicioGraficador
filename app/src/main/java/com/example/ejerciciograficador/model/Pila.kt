package com.example.ejerciciograficador.model

/**
 * Implementación genérica de una estructura de datos Pila (LIFO)
 * @param T Tipo genérico de elementos que contendrá la pila
 */
class Pila<T> {
    private val elementos = mutableListOf<T>()


    /**
     * Agrega un elemento a la parte superior de la pila
     * @param elemento Elemento a agregar
     */
    fun push(elemento: T) {
        elementos.add(elemento)
    }


    /**
      Elimina y devuelve el elemento en la parte superior de la pila
      @return Elemento en la parte superior o null si la pila está vacía
     */
    fun pop(): T? {
        if (estaVacia()) {
            return null
        }
        return elementos.removeAt(elementos.size - 1)
    }


    /**
      Devuelve el elemento en la parte superior sin eliminarlo
      @return Elemento en la parte superior o null si la pila está vacía
     */
    fun peek(): T? {
        if (estaVacia()) {
            return null
        }
        return elementos.last()
    }


    /**
      Verifica si la pila está vacía
      @return true si la pila está vacía, false de lo contrario
     */
    fun estaVacia(): Boolean {
        return elementos.isEmpty()
    }


    /**
      Devuelve el tamaño actual de la pila
      @return Número de elementos en la pila
     */
    fun tamano(): Int {
        return elementos.size
    }


    /**
     funcion para limpiar/borrar todos los elementos de la pila
     */
    fun limpiar() {
        elementos.clear()
    }
}