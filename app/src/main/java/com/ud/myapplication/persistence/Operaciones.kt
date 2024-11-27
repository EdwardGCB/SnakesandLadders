package com.ud.myapplication.persistence

import kotlin.random.Random

open class Operaciones {

    open fun generateRandomString(length: Int = 6): String {
        val charset = ('A'..'Z') + ('a'..'z') + ('0'..'9') // Letras y números
        return (1..length)
            .map { Random.nextInt(0, charset.size) }
            .map(charset::get)
            .joinToString("")
    }

    fun startGame() {
        Tablero.iniciarTablero()

        // Agregar jugadores al tablero
        Tablero.addPlayer(Player(idPlayer = "1", name = "Jugador 1"))
        Tablero.addPlayer(Player(idPlayer = "2", name = "Jugador 2"))

        println("¡Inicia el juego de Snakes and Ladders!")

        while (true) {
            Tablero.startTurn()

            // Verificar si alguien ganó
            val winner = Tablero.getPlayers().find { it.position == 64 }
            if (winner != null) {
                println("🎉 ${winner.name} ganó el juego!")
                break
            }
        }
    }
}
