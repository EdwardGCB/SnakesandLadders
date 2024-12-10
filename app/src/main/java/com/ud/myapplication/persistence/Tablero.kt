package com.ud.myapplication.persistence

object Tablero {
    private var idTablero: String = ""
    private var tablero: MutableList<MutableList<Casilla>> = mutableListOf()

    fun iniciarTablero(board: Board): List<List<Casilla>> {
        idTablero = board.id
        if (tablero.isEmpty()) {
            var count = board.rows * board.columns
            for (i in board.rows - 1 downTo 0) {
                val filasCasillas = mutableListOf<Casilla>()
                for (j in board.columns - 1 downTo 0) {
                    filasCasillas.add(Casilla(count))
                    if (i % 2 != 0) {
                        count-- // Disminuir el conteo en filas pares
                    } else {
                        count++ // Aumentar el conteo en filas impares
                    }
                }
                tablero.add(filasCasillas)
                count -= if (i % 2 != 0) {
                    7 // Ajustar el conteo al final de las filas pares
                } else {
                    9 // Ajustar el conteo al final de las filas impares
                }
            }
        }
        return tablero
    }
}
