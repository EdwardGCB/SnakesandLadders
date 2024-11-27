package com.ud.myapplication.persistence

import com.google.firebase.database.FirebaseDatabase

class Player(
    val idPlayer: String = "",
    val name: String = "",
    var position: Int = 0,
    var state: Boolean = true,
    val img: String = ""
) {
    private val database = FirebaseDatabase.getInstance()
    private val playerRef = database.getReference("players")

    fun syncWithDatabase() {
        val playerData = mapOf(
            "name" to name,
            "position" to position,
            "state" to state,
            "img" to img
        )
        playerRef.child(idPlayer).setValue(playerData)
    }

    fun move(steps: Int, tablero: List<List<Casilla>>) {
        val newPosition = position + steps
        position = if (newPosition > 64) position else newPosition

        val currentCasilla = tablero.flatten().find { it.valor == position }
        currentCasilla?.let {
            when (it.estado) {
                EnumEstado.UP.toString() -> position += 3
                EnumEstado.DOWN.toString() -> position -= 3
                EnumEstado.HEAD.toString() -> position -= 5
            }
        }

        // Sincronizar la nueva posición con Firebase
        syncWithDatabase()
    }
}
