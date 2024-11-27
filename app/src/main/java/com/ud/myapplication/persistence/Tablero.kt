package com.ud.myapplication.persistence

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object Tablero {
    private var tablero: MutableList<MutableList<Casilla>> = mutableListOf()
    private var players: MutableList<Player> = mutableListOf()
    private var currentPlayerIndex = 0

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val tableroRef: DatabaseReference = database.getReference("tablero")

    fun iniciarTablero(): List<List<Casilla>> {
        if (tablero.isEmpty()) {
            val rows = 8
            val columns = 8
            var count = 64
            for (i in rows - 1 downTo 0) {
                val filasCasillas = mutableListOf<Casilla>()
                for (j in columns - 1 downTo 0) {
                    val casilla = when (count) {
                        // Casillas especiales (serpientes y escaleras)
                        3, 5, 22, 43 -> Casilla(count, EnumEstado.LINE.toString())
                        31, 41, 51, 60 -> Casilla(count, EnumEstado.HEAD.toString())
                        8, 12, 32, 44 -> Casilla(count, EnumEstado.UP.toString())
                        38, 39, 47, 61 -> Casilla(count, EnumEstado.DOWN.toString())
                        else -> Casilla(count)
                    }
                    filasCasillas.add(casilla)
                    count += if (i % 2 == 0) 1 else -1
                }
                tablero.add(filasCasillas)
                count += if (i % 2 == 0) -9 else -7
            }
            // Guardar el tablero en Firebase
            guardarTableroEnFirebase()
        }
        return tablero
    }

    private fun guardarTableroEnFirebase() {
        tableroRef.setValue(tablero)
            .addOnSuccessListener {
                println("Tablero guardado en Firebase exitosamente.")
            }
            .addOnFailureListener { e ->
                println("Error al guardar el tablero en Firebase: ${e.message}")
            }
    }

    fun addPlayer(player: Player) {
        players.add(player)
        val playersRef = database.getReference("players")
        playersRef.child(player.idPlayer).setValue(player)
            .addOnSuccessListener {
                println("Jugador ${player.name} agregado a Firebase.")
            }
            .addOnFailureListener { e ->
                println("Error al agregar jugador: ${e.message}")
            }
    }

    fun getPlayers(): List<Player> = players

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
