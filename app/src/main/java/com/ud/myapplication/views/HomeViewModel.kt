package com.ud.myapplication.views
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.ud.myapplication.persistence.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameBoardViewModel : ViewModel() {

    private val db = Firebase.firestore
    private var playersListener: ListenerRegistration? = null

    // Exponemos el estado de los jugadores y errores
    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    /**
     * Función para agregar un jugador al tablero
     */
    fun joinBoard(boardId: String, player: Player) {
        val boardRef = db.collection("gameBoards").document(boardId)
        val playerData = mapOf(
            "id" to player.idPlayer,
            "correo" to player.correo,
            "state" to player.state,
            "position" to player.position
        )

        boardRef.update("players", com.google.firebase.firestore.FieldValue.arrayUnion(playerData))
            .addOnSuccessListener {
                _errorMessage.value = null // Limpia errores en caso de éxito
            }
            .addOnFailureListener { exception ->
                _errorMessage.value = exception.message
            }
    }

    /**
     * Escuchar cambios en la lista de jugadores
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

            // Obtenemos la lista de jugadores y la convertimos a una lista de objetos Player
            val players = snapshot?.get("players") as? List<Map<String, Any>> ?: emptyList()
            val playerList = players.map { data ->
                Player(
                    idPlayer = data["id"] as String,
                    correo = data["correo"] as String,
                    state = data["state"] as Boolean,
                    position = (data["position"] as Long).toInt()
                )
            }
            _players.value = playerList
        }
    }

    fun updateBoardState(boardId: String) {
        val boardRef = db.collection("gameBoards").document(boardId)
        boardRef.update("state", true)
            .addOnSuccessListener {
            _errorMessage.value = null
            }
            .addOnFailureListener {
              exception -> _errorMessage.value = exception.message
            }
    }

    /**
     * Limpia el listener cuando el ViewModel es destruido
     */
    override fun onCleared() {
        super.onCleared()
        playersListener?.remove()
    }
}
