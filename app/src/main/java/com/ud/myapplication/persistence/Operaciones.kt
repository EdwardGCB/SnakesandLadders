package com.ud.myapplication.persistence

import kotlin.random.Random

open class Operaciones {

    open fun generateRandomColor(): String {
        // Generar valores aleatorios para rojo, verde y azul (RGB)
        val red = Random.nextInt(0, 256) // Valor entre 0 y 255
        val green = Random.nextInt(0, 256)
        val blue = Random.nextInt(0, 256)

        // Convertir los valores RGB a formato hexadecimal (#RRGGBB)
        return String.format("#%02X%02X%02X", red, green, blue)
    }

    open fun generateSnakesAndLadders(numSnacke: Int, numLadder: Int, rows: Int, columns: Int): Pair<List<Snake>, List<Ladder>> {
        val snakes = mutableListOf<Snake>()
        val ladders = mutableListOf<Ladder>()

        // Conjunto para almacenar las casillas ya usadas por serpientes y escaleras
        val usedPositions = mutableSetOf<Int>()

        // Total de casillas en el tablero
        val totalCasillas = rows * columns

        // Generar serpientes
        for (i in 1..numSnacke) {
            var start: Int
            var end: Int

            do {
                start = (1..totalCasillas - 1).random()  // Generamos un inicio aleatorio de la serpiente (hasta la penúltima casilla)
                end = (1..start - 1).random()  // Generamos un final aleatorio para la serpiente (menor que start)

            } while (usedPositions.contains(start) || usedPositions.contains(end))  // Verificamos que no coincidan con posiciones ya usadas

            // Agregar serpiente y marcar las posiciones usadas
            snakes.add(Snake(start, end))
            usedPositions.add(start)
            usedPositions.add(end)
        }

        // Generar escaleras
        for (i in 1..numLadder) {
            var start: Int
            var end: Int

            do {
                start = (1..totalCasillas - 1).random()  // Generamos un inicio aleatorio de la escalera
                end = (start + 1..totalCasillas).random()  // Generamos un final aleatorio para la escalera (mayor que start)

            } while (usedPositions.contains(start) || usedPositions.contains(end))  // Verificamos que no coincidan con posiciones ya usadas

            // Agregar escalera y marcar las posiciones usadas
            ladders.add(Ladder(start, end))
            usedPositions.add(start)
            usedPositions.add(end)
        }

        return Pair(snakes, ladders)
    }
}
