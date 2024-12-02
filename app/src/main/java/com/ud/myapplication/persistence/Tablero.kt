package com.ud.myapplication.persistence

object Tablero {
    private var idTablero: String = ""
    private var tablero: MutableList<MutableList<Casilla>> = mutableListOf()
    var players1: MutableList<Player> = mutableListOf()

    fun iniciarTablero(board: Board): List<List<Casilla>> {
        idTablero = board.id
        players1 = board.jugadores.toMutableList()
        if (tablero.isEmpty()) {
            var count = board.filas * board.columnas
            for (i in board.filas - 1 downTo 0) {
                val filasCasillas = mutableListOf<Casilla>()
                for (j in board.columnas - 1 downTo 0) {
                    when (count) {
                        // LINE SNAKE
                        3, 5, 22, 43 -> {
                            filasCasillas.add(Casilla(count, EnumEstado.LINE.toString()))
                        }
                        // HEAD SNAKE
                        31, 41, 51, 60 -> {
                            filasCasillas.add(Casilla(count, EnumEstado.HEAD.toString()))
                        }
                        // UP Ladders
                        8, 12, 32, 44 -> {
                            filasCasillas.add(Casilla(count, EnumEstado.UP.toString()))
                        }
                        // DOWN Ladders
                        38, 39, 47, 61 -> {
                            filasCasillas.add(Casilla(count, EnumEstado.DOWN.toString()))
                        }
                        else -> {
                            // Default case
                            filasCasillas.add(Casilla(count))
                        }
                    }
                    if (i % 2 != 0) {
                        count-- // Disminuir el conteo en filas pares
                    } else {
                        count++ // Aumentar el conteo en filas impares
                    }
                }
                tablero.add(filasCasillas)
                if (i % 2 != 0) {
                    count -= 7 // Ajustar el conteo al final de las filas pares
                } else {
                    count -= 9 // Ajustar el conteo al final de las filas impares
                }
            }
        }
        return tablero
    }
}
