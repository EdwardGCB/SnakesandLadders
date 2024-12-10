package com.ud.myapplication.ViewModel
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.ud.myapplication.persistence.Ladder
import com.ud.myapplication.persistence.Operaciones
import com.ud.myapplication.persistence.Player
import com.ud.myapplication.persistence.Snake
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

    fun createGameBoard(): String{
        //Generacion del id
        var boardId = db.collection("gameBoards").document().id

        val gameBoard = hashMapOf(
            "turn" to 1,
            "rows" to 8,
            "columns" to 8,
            "players" to arrayListOf<Player>(),
            "snakes" to arrayListOf<Snake>(),
            "ladders" to arrayListOf<Ladder>(),
            "state" to false,
            "id" to boardId
        )
        db.collection("gameBoards").document(boardId)
            .set(gameBoard)
            .addOnSuccessListener {

            }.addOnSuccessListener {
                boardId=""
            }
        return boardId
    }

    /**
     * Función para agregar un jugador al tablero
     */
    fun joinBoard(boardId: String, player: Player) {
        val boardRef = db.collection("gameBoards").document(boardId)

        boardRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Recupera la lista de jugadores actual en el tablero
                    val currentPlayers = document.get("players") as? List<Map<String, Any>> ?: emptyList()

                    val playerData = mapOf(
                        "idPlayer" to player.idPlayer,
                        "correo" to player.correo,
                        "position" to player.position,
                        "dado" to player.dado,
                        "turno" to currentPlayers.size+1, // Turno asignado automáticamente
                        "color" to player.color
                    )

                    // Añade al jugador usando FieldValue.arrayUnion
                    boardRef.update(
                        "players",
                        com.google.firebase.firestore.FieldValue.arrayUnion(playerData)
                    )
                        .addOnSuccessListener {
                            _errorMessage.value = null // Limpia errores en caso de éxito
                        }
                        .addOnFailureListener { exception ->
                            _errorMessage.value = exception.message
                        }
                } else {
                    _errorMessage.value = "No se encontró el tablero con ID: $boardId"
                }
            }
            .addOnFailureListener { e ->
                _errorMessage.value = "Error al obtener datos del tablero: ${e.message}"
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
                    idPlayer = data["idPlayer"] as String,
                    correo = data["correo"] as String
                )
            }
            _players.value = playerList
        }
    }

    fun updateBoardState(boardId: String, numSnakes: Int, numLadders: Int) {
        val boardRef = db.collection("gameBoards").document(boardId)

        // Generamos las serpientes y escaleras
        val (snakes, ladders) = Operaciones().generateSnakesAndLadders(numSnakes, numLadders, 8, 8)

        val updates = hashMapOf<String, Any>(
            "state" to true,          // Cambiar el estado del tablero a "true"
            "snakes" to snakes,       // Añadir las serpientes
            "ladders" to ladders     // Añadir las escaleras
        )

        // Actualizamos el tablero en Firestore
        boardRef.update(updates)
            .addOnSuccessListener {
                _errorMessage.value = null
            }
            .addOnFailureListener { exception ->
                _errorMessage.value = exception.message
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
