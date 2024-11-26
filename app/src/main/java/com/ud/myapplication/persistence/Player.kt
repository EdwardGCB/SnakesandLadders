package com.ud.myapplication.persistence

class Player(
    val idPlayer: String = "",
    val name: String = "",
    var position: Int = 0, // Representa la posición actual del jugador en el tablero
    var state: Boolean = true, // Indica si el jugador está activo o inactivo
    val img: String = "" // Puede representar la imagen del jugador si se utiliza una interfaz gráfica
) {

    // Método para mover al jugador en el tablero
    fun move(steps: Int, tablero: List<List<Casilla>>) {
        val newPosition = position + steps

        // Evitar que la posición exceda el límite del tablero (64)
        position = if (newPosition > 64) {
            println("$name no puede avanzar $steps pasos, queda en la posición $position.")
            position // Mantener la posición actual
        } else {
            println("$name avanza $steps pasos. Nueva posición: $newPosition")
            newPosition
        }

        // Verificar si el jugador aterriza en una casilla especial
        val currentCasilla = tablero.flatten().find { it.valor == position }
        currentCasilla?.let {
            when (it.estado) {
                EnumEstado.UP.toString() -> {
                    position += 3 // Ejemplo: sube una escalera
                    println("$name subió una escalera a la posición $position.")
                }

                EnumEstado.DOWN.toString() -> {
                    position -= 3 // Ejemplo: baja por una escalera
                    println("$name bajó por una escalera a la posición $position.")
                }

                EnumEstado.HEAD.toString() -> {
                    position -= 5 // Ejemplo: cayó en una serpiente
                    println("$name fue atrapado por una serpiente y retrocedió a la posición $position.")
                }
            }
        }
    }
}
