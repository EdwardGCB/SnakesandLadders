package com.ud.myapplication.persistence

object Tablero {
    private var tablero: MutableList<MutableList<Casilla>> = mutableListOf()
    private var players: MutableList<Player> = mutableListOf()
    private var currentPlayerIndex = 0

    fun iniciarTablero(): List<List<Casilla>> {
        if (tablero.isEmpty()) {
            val rows = 8
            val columns = 8
            var count = 64
            for (i in rows - 1 downTo 0) {
                val filasCasillas = mutableListOf<Casilla>()
                for (j in columns - 1 downTo 0) {
                    when (count) {
                        // Casillas especiales (serpientes y escaleras)
                        3, 5, 22, 43 -> {
                            filasCasillas.add(Casilla(count, EnumEstado.LINE.toString())) // Línea
                        }
                        31, 41, 51, 60 -> {
                            filasCasillas.add(Casilla(count, EnumEstado.HEAD.toString())) // Serpiente
                        }
                        8, 12, 32, 44 -> {
                            filasCasillas.add(Casilla(count, EnumEstado.UP.toString())) // Escalera
                        }
                        38, 39, 47, 61 -> {
                            filasCasillas.add(Casilla(count, EnumEstado.DOWN.toString())) // Escalera hacia abajo
                        }
                        else -> {
                            filasCasillas.add(Casilla(count)) // Casilla vacía
                        }
                    }
                    if (i % 2 != 0) {
                        count-- // Disminuir el conteo en filas impares
                    } else {
                        count++ // Aumentar el conteo en filas pares
                    }
                }
                tablero.add(filasCasillas)
                if (i % 2 != 0) {
                    count -= 7 // Ajustar el conteo al final de las filas impares
                } else {
                    count -= 9 // Ajustar el conteo al final de las filas pares
                }
            }
        }
        return tablero
    }

    fun addPlayer(player: Player) {
        players.add(player)
    }

    fun getPlayers(): List<Player> {
        return players
    }

    fun startTurn() {
        val currentPlayer = players[currentPlayerIndex]
        println("Turno de ${currentPlayer.name}. Lanzando el dado...")
        val diceRoll = (1..6).random()
        println("${currentPlayer.name} lanzó un $diceRoll.")
        currentPlayer.move(diceRoll, tablero)
        nextPlayer()
    }

    private fun nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size
    }
}
