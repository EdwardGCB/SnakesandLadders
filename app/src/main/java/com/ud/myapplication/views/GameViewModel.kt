package com.ud.myapplication.views

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ud.myapplication.persistence.Board
import com.ud.myapplication.persistence.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameViewModel : ViewModel() {
    private val db = Firebase.firestore
    private var boardListener: ListenerRegistration? = null
    private var playersListener: ListenerRegistration? = null

    // Exponemos el estado de los jugadores y errores
    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players

    private val _board = MutableStateFlow<Board?>(null)
    val board: StateFlow<Board?> = _board

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    /**
     * Función para consultar las propiedades del tablero
     */
    fun consultarTablero(idBoard: String) {
        val boardRef = idBoard.let { db.collection("gameBoards").document(it) }

        // Añadir listener para observar cambios en el documento del tablero
        boardListener = boardRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                _errorMessage.value = "Error al escuchar cambios: ${error.message}"
                _isLoading.value = false
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val board = snapshot.toObject(Board::class.java)
                _board.value = board
                _isLoading.value = false
            } else {
                _errorMessage.value = "No se encontró el tablero"
                _isLoading.value = false
            }
        }
    }

    /**
     * Limpia los listeners cuando el ViewModel es destruido
     */
    override fun onCleared() {
        super.onCleared()
        boardListener?.remove()
    }
}
