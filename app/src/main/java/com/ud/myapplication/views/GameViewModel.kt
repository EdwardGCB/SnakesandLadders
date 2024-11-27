package com.ud.myapplication.views

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ud.myapplication.persistence.Board
import com.ud.myapplication.persistence.Player
import com.ud.myapplication.persistence.Tablero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameViewModel : ViewModel() {
    private val db = Firebase.firestore
    private var playersListener: ListenerRegistration? = null

    // Exponemos el estado de los jugadores y errores
    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players

    private val _board = MutableStateFlow<Board?>(null)
    val board: StateFlow<Board?> = _board

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    /**
     * Función para consultar las propiedades del tablero
     */
    fun consultarTablero(idBoard: String) {
        val boardRef = db.collection("gameBoards").document(idBoard)

        boardRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val board = document.toObject(Board::class.java)
                    _board.value = board
                } else {
                    _errorMessage.value = "No se encontró el tablero"
                }
            }
            .addOnFailureListener { exception ->
                _errorMessage.value = "Error al obtener el tablero: ${exception.message}"
            }
    }
}