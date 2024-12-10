package com.ud.myapplication.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
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

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

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
     * Función para escuchar a los jugadores del tablero
     */
    fun listenToPlayers(boardId: String) {
        val boardRef = db.collection("gameBoards").document(boardId)

        // Si ya hay un listener activo, lo removemos
        playersListener?.remove()

        playersListener = boardRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                _errorMessage.value = "Error al escuchar cambios: ${error.message}"
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                try {
                    // Obtenemos la lista de jugadores y la convertimos a objetos Player
                    val players2 = snapshot.get("players") as? List<Map<String, Any>> ?: emptyList()
                    val playerList = players2.mapNotNull { data ->
                        try {
                            Player(
                                idPlayer = data["id"] as? String ?: "",
                                correo = data["correo"] as? String ?: "",
                                position = (data["position"] as? Long)?.toInt() ?: 1,
                                color = data["color"] as? String ?: "default", // Valor por defecto
                                dado = (data["dado"] as? Long)?.toInt() ?: 1,
                                turno = (data["turno"] as? Long)?.toInt() ?: 0
                            )
                        } catch (e: Exception) {
                            null // Ignoramos entradas corruptas
                        }
                    }
                    _players.value = playerList
                } catch (e: Exception) {
                    _errorMessage.value = "Error al procesar datos: ${e.message}"
                }
            } else {
                _errorMessage.value = "No se encontró el tablero con ID $boardId"
            }
        }
    }

    /**
     * Función para actualizar a los jugadores
     */
    fun updatePlayer(boardId: String, updatedPlayer: Player) {
        val boardRef = db.collection("gameBoards").document(boardId)

        boardRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Obtener los jugadores, serpientes y escaleras
                    val players2 = document.get("players") as? List<Map<String, Any>> ?: emptyList()
                    val snakes = document.get("snakes") as? List<Map<String, Any>> ?: emptyList()
                    val ladders = document.get("ladders") as? List<Map<String, Any>> ?: emptyList()

                    // Modificar solo el jugador específico (position y dado)
                    val updatedPlayers = players2.map { player ->
                        if (player["correo"] == updatedPlayer.correo) {
                            // Convertir el jugador en un mapa mutable
                            player.toMutableMap().apply {
                                // Verificar si el jugador pisa una serpiente
                                val snake = snakes.find { it["start"] == updatedPlayer.position }
                                if (snake != null) {
                                    // El jugador pisa una serpiente
                                    val end = snake["end"] as? Int ?: updatedPlayer.position
                                    updatedPlayer.position = end
                                    _errorMessage.value = "El jugador ha pisado una serpiente y se mueve a la casilla $end"
                                }

                                // Verificar si el jugador pisa una escalera
                                val ladder = ladders.find { it["start"] == updatedPlayer.position }
                                if (ladder != null) {
                                    // El jugador pisa una escalera
                                    val end = ladder["end"] as? Int ?: updatedPlayer.position
                                    updatedPlayer.position = end
                                    _errorMessage.value = "El jugador ha subido por una escalera y se mueve a la casilla $end"
                                }

                                // Actualizar la posición y el dado del jugador
                                this["position"] = updatedPlayer.position
                                this["dado"] = updatedPlayer.dado
                            }
                        } else {
                            player
                        }
                    }

                    // Actualizar los jugadores en Firestore
                    boardRef.update("players", updatedPlayers)
                        .addOnSuccessListener {
                            _errorMessage.value = null
                        }
                        .addOnFailureListener { exception ->
                            _errorMessage.value = "Error al actualizar jugador: ${exception.message}"
                        }
                } else {
                    _errorMessage.value = "No se encontró el tablero con el ID especificado."
                }
            }
            .addOnFailureListener { exception ->
                _errorMessage.value = "Error al obtener el tablero: ${exception.message}"
            }
    }


    fun switchTurn(boardId: String) {
        val boardRef = db.collection("gameBoards").document(boardId)

        // Obtenemos el documento del tablero
        boardRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val players = document.toObject(Board::class.java)?.players
                val currentTurn = document.getLong("turn")?.toInt() ?: 1  // Obtener turno actual
                val totalPlayers = players?.size ?: 0

                // Validar si el turno actual es igual al número de jugadores
                val newTurn = if (currentTurn == totalPlayers) {
                    1  // Si el turno es igual al número de jugadores, reinicia a 1
                } else {
                    currentTurn + 1  // Sino, incrementa el turno
                }

                // Actualizamos el turno en el documento
                boardRef.update("turn", newTurn)
                    .addOnSuccessListener {
                        _errorMessage.value = null  // Limpiar mensaje de error
                    }
                    .addOnFailureListener { exception ->
                        _errorMessage.value = "Error al cambiar el turno: ${exception.message}"
                    }
            } else {
                _errorMessage.value = "No se encontró el tablero con el ID especificado"
            }
        }.addOnFailureListener { exception ->
            _errorMessage.value = "Error al obtener el tablero: ${exception.message}"
        }
    }



    val currentUserEmail: String
        get() = firebaseAuth.currentUser?.email ?: ""

    /**
     * Limpia los listeners cuando el ViewModel es destruido
     */
    override fun onCleared() {
        super.onCleared()
        boardListener?.remove()
    }
}
