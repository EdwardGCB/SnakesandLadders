package com.ud.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.database.FirebaseDatabase
import com.ud.myapplication.navigation.Navigation
import com.ud.myapplication.persistence.Player
import com.ud.myapplication.persistence.Tablero
import com.ud.myapplication.ui.theme.SnakesAndLaddersTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Configurar conexión inicial con Firebase
        initializeFirebaseConnection()

        // Iniciar el juego
        startGame()

        // Configuración de la interfaz
        setContent {
            SnakesAndLaddersTheme {
                Navigation()
            }
        }
    }

    private fun initializeFirebaseConnection() {
        val database = FirebaseDatabase.getInstance()
        val messageRef = database.getReference("mensaje_prueba")

        messageRef.setValue("¡Hola desde Firebase!")
            .addOnSuccessListener {
                Toast.makeText(this, "Conexión exitosa con Firebase", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error en Firebase: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun startGame() {
        Tablero.iniciarTablero()

        // Agregar jugadores al tablero
        Tablero.addPlayer(Player(idPlayer = "1", name = "Jugador 1"))
        Tablero.addPlayer(Player(idPlayer = "2", name = "Jugador 2"))

        // Iniciar el turno de forma controlada
        simulateGame()
    }

    private fun simulateGame() {
        var gameOver = false

        while (!gameOver) {
            Tablero.startTurn()

            // Verificar si alguien ganó
            val winner = Tablero.getPlayers().find { it.position == 64 }
            if (winner != null) {
                println("🎉 ${winner.name} ganó el juego!")
                gameOver = true
            }

            // Pausa simulada para evitar bloquear el hilo principal
            Thread.sleep(1000) // Simulación del tiempo entre turnos
        }
    }
}
